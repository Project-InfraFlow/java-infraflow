package school.sptech;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;


public class Conexao {

    private DataSource dataSource;

    public Conexao() {
        BasicDataSource dataSource = new BasicDataSource();

        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://ec2-34-236-140-100.compute-1.amazonaws.com:3306/Infraflow?useSSL=false&serverTimezone=America/Sao_Paulo&allowPublicKeyRetrieval=true");
        // dataSource.setUrl("jdbc:mysql://localhost:3306/Infraflow?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true");
        dataSource.setUsername("edson_admin");
        dataSource.setPassword("urubu100");
        // dataSource.setPassword("8880");

        dataSource.setInitialSize(5);
        dataSource.setMaxTotal(10);

        this.dataSource = dataSource;
    }

    public DataSource getConexao() {
        return dataSource;
    }
}
