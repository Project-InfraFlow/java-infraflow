package school.sptech;
import org.springframework.jdbc.core.JdbcTemplate;
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Conexao conexao = new Conexao();
        JdbcTemplate template = new JdbcTemplate(conexao.getConexao());

        System.out.println("Conectado com o banco Infraflow com sucesso!");
        Integer qtdEmpresas = template.queryForObject(
                "SELECT COUNT(*) FROM empresa", Integer.class);

        System.out.println("Empresas cadastradas: " + qtdEmpresas);

//        template.execute("DROP TABLE IF EXISTS componente ");
//        template.execute("DROP TABLE IF EXISTS leitura");

//        template.execute("CREATE TABLE componente" +
//                "(id_componente INT NOT NULL," +
//                "fk_id_maquina INT NOT NULL," +
//                "CONSTRAINT pk_componente PRIMARY KEY (id_componente, fk_id_maquina),"+
//                "fk_token_empresa INT NOT NULL," +
//                "nome_componente VARCHAR(45)," +
//                "unidade_de_medida VARCHAR(10)," +
//                "CONSTRAINT fk_componente_maquina FOREIGN KEY (fk_id_maquina) REFERENCES maquina(id_maquina)," +
//                "CONSTRAINT fk_componente_empresa FOREIGN KEY (fk_token_empresa) REFERENCES empresa(token_empresa))");
//
//        template.execute("CREATE TABLE leitura (" +
//                "id_leitura INT NOT NULL AUTO_INCREMENT," +
//                "CONSTRAINT pk_leitura PRIMARY KEY (id_leitura)," +
//                "fk_id_componente INT NOT NULL," +
//                "fk_id_maquina INT NOT NULL," +
//                "fk_token_empresa INT NOT NULL, " +
//                "dados_float DOUBLE," +
//                "dados_texto VARCHAR(100)," +
//                "data_hora_captura DATETIME," +
//                "fk_id_nucleo INT,"+
//                "CONSTRAINT fk_leitura_componente FOREIGN KEY (fk_id_componente, fk_id_maquina) REFERENCES componente(id_componente, fk_id_maquina),"+
//                "CONSTRAINT fk_leitura_nucleo_cpu FOREIGN KEY (id_nucleo, fk_id_componente, fk_id_maquina) REFERENCES nucleo_cpu(id_nucleo, fk_id_componente, fk_id_maquina),"+
//                "CONSTRAINT fk_leitura_empresa FOREIGN KEY (fk_token_empresa) REFERENCES empresa(token_empresa))"
//        );

//        template.update("INSERT INTO componente VALUES(" +
//                "DEFAULT,?,?,?,?)",1, "Bytes Enviados", "bytes", null );
//
//        template.update("INSERT INTO componente VALUES (DEFAULT, ?, ?, ?, ?)",
//                1, "Bytes Recebidos", "bytes", null);
//
//        template.update("INSERT INTO componente VALUES (DEFAULT, ?, ?, ?, ?)",
//                1, "Endereço MAC", "texto", null);
//
//        template.update("INSERT INTO componente VALUES (DEFAULT, ?, ?, ?, ?)",
//                1, "Endereço IPv4", "texto", null);
//
//        System.out.println("Tabela criada e componente de rede inseridos com sucesso!");

    }
}