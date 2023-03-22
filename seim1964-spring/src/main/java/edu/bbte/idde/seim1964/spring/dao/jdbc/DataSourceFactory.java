package edu.bbte.idde.seim1964.spring.dao.jdbc;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Configuration
@Profile("!mem")
public class DataSourceFactory {

    @Value("${jdbc.url:jdbc:mysql://localhost:3306/announcements?useSSL=false}")
    private String jdbcUrl;
    @Value("${jdbc.user:user}")
    private String jdbcUser;
    @Value("${jdbc.passwd:godbless123!}")
    private String passwd;
    @Value("${jdbc.poolSize:10}")
    private Integer connectionNumber;
    @Value("${jdbc.driver:com.mysql.cj.jdbc.Driver}")
    private String driver;

    @Bean
    public DataSource getDataSource() {
        HikariConfig hikariConfig = new HikariConfig();

        hikariConfig.setJdbcUrl(jdbcUrl);
        hikariConfig.setUsername(jdbcUser);
        hikariConfig.setPassword(passwd);
        hikariConfig.setDriverClassName(driver);
        hikariConfig.setMaximumPoolSize(connectionNumber);

        return new HikariDataSource(hikariConfig);
    }
}
