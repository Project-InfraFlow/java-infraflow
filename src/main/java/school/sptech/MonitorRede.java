package school.sptech;
import com.github.britooo.looca.api.core.Looca;
import com.github.britooo.looca.api.group.rede.RedeInterface;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;

public class MonitorRede {
    private static double bytesEnviadosAnterior = 0;
    private static double bytesRecebidosAnterior = 0;
    private static final String SLACK_WEBHOOK_URL = System.getenv("SLACK_WEBHOOK_URL");

    public static void main(String[] args) {
        Conexao conexao = new Conexao();
        JdbcTemplate template = new JdbcTemplate(conexao.getConexao());
        Looca looca = new Looca();

        System.out.println("Conectado com o banco Infraflow com sucesso!");

        Integer idEmpresa = 1;

        Integer idMaquina = template.queryForObject(
                "SELECT id_maquina FROM maquina WHERE fk_empresa_maquina = ? LIMIT 1", new Object[]{idEmpresa},
                Integer.class);
        System.out.println("ID da máquina: " + idMaquina);

        Integer idNucleo = null;

        String ipv4Ativo = null;
        String macAtivo = null;
        RedeInterface ifaceAtiva = null;

        List<RedeInterface> interfaces = looca.getRede().getGrupoDeInterfaces().getInterfaces();
        System.out.println();
        for (RedeInterface iface : interfaces) {
            for (String ip : iface.getEnderecoIpv4()) {
                if (!ip.startsWith("127.") && !ip.startsWith("0.")) {
                    ipv4Ativo = ip;
                    macAtivo = iface.getEnderecoMac();
                    ifaceAtiva = iface;
                    break;
                }
            }
            if (ipv4Ativo != null)
                break;
        }

        if (ipv4Ativo == null) {
            System.out.println("Nenhuma interface de rede ativa encontrada.");
        } else {
            System.out.println("\n Interface ativa: " + ifaceAtiva.getNomeExibicao());
            System.out.println("IPv4 ativo: " + ipv4Ativo);
            System.out.println("MAC: " + macAtivo);

            bytesEnviadosAnterior = ifaceAtiva.getBytesEnviados();
            bytesRecebidosAnterior = ifaceAtiva.getBytesRecebidos();
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        while (true) {
            try {
                String dataHora = LocalDateTime.now().format(formatter);

                double bytesEnviadosAtual = ifaceAtiva.getBytesEnviados();
                double bytesRecebidosAtual = ifaceAtiva.getBytesRecebidos();

                double bytesEnviadosDiff = bytesEnviadosAtual - bytesEnviadosAnterior;
                double bytesRecebidosDiff = bytesRecebidosAtual - bytesRecebidosAnterior;

                double mbpsEnviados = (bytesEnviadosDiff * 8) / 1000000;
                double mbpsRecebidos = (bytesRecebidosDiff * 8) / 1000000;

                double mbpsTotal = mbpsEnviados + mbpsRecebidos;

                template.update("INSERT INTO leitura (fk_id_componente, fk_id_maquina, dados_float, data_hora_captura, id_nucleo) VALUES (?, ?, ?, ?, ?)",
                        4, idMaquina, mbpsTotal, dataHora, idNucleo);

                System.out.println("Rede: " + String.format("%.2f", mbpsTotal) + " Mbps");

                if (mbpsTotal > 100.0) {
                    enviarAlertaSlack(String.format(
                            "Alerta: tráfego de rede elevado na máquina %d. Valor atual: %.2f Mbps.",
                            idMaquina, mbpsTotal
                    ));
                }

                bytesEnviadosAnterior = bytesEnviadosAtual;
                bytesRecebidosAnterior = bytesRecebidosAtual;

                Thread.sleep(2000);

            } catch (Exception e) {
                System.out.println("Erro ao coletar ou inserir dados: " + e.getMessage());
                e.printStackTrace();
                break;
            }
        }
    }

    private static void enviarAlertaSlack(String mensagem) {
        if (SLACK_WEBHOOK_URL == null || SLACK_WEBHOOK_URL.isBlank()) {
            return;
        }
        try {
            URL url = new URL(SLACK_WEBHOOK_URL);
            HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
            conexao.setRequestMethod("POST");
            conexao.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            conexao.setDoOutput(true);

            String payload = "{\"text\":\"" + mensagem.replace("\"", "\\\"") + "\"}";

            try (OutputStream os = conexao.getOutputStream()) {
                byte[] input = payload.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int status = conexao.getResponseCode();
            if (status < 200 || status >= 300) {
                System.out.println("Falha ao enviar alerta ao Slack. Código HTTP: " + status);
            }

            conexao.disconnect();
        } catch (Exception e) {
            System.out.println("Erro ao enviar alerta ao Slack: " + e.getMessage());
        }
    }
}
