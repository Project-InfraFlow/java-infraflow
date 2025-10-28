package school.sptech;
import com.github.britooo.looca.api.core.Looca;
import com.github.britooo.looca.api.group.rede.RedeInterface;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MonitorRede {
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
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        while (true) {
            try {
                String dataHora = LocalDateTime.now().format(formatter);

                double bytesEnviados = ifaceAtiva.getBytesEnviados();
                double bytesRecebidos = ifaceAtiva.getBytesRecebidos();

                template.update("INSERT INTO leitura (fk_id_componente, fk_id_maquina, dados_float, data_hora_captura,id_nucleo) VALUES (?, ?, ?, ?, ?)",
                        1, idMaquina, bytesEnviados, dataHora, idNucleo);

                template.update("INSERT INTO leitura (fk_id_componente, fk_id_maquina, dados_float, data_hora_captura,id_nucleo) VALUES (?, ?, ?, ?, ?)",
                        2, idMaquina, bytesRecebidos, dataHora, idNucleo);

                template.update("INSERT INTO leitura (fk_id_componente, fk_id_maquina, dados_texto, data_hora_captura,id_nucleo) VALUES (?, ?, ?, ?, ?)",
                        3, idMaquina, macAtivo, dataHora, idNucleo);

                template.update("INSERT INTO leitura (fk_id_componente, fk_id_maquina, dados_texto, data_hora_captura,id_nucleo) VALUES (?, ?, ?, ?, ?)",
                        4, idMaquina, ipv4Ativo, dataHora, idNucleo);

                System.out.println("Leituras inseridas para interface " + ifaceAtiva.getNomeExibicao());

                Thread.sleep(4000);

            } catch (Exception e) {
                System.out.println("Erro ao coletar ou inserir dados: " + e.getMessage());
                e.printStackTrace();
                break;
            }
        }
    }
    }

