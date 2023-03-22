package edu.bbte.idde.seim1964.backend.config;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Config {
    private String daoType;
    private String jdbcUser;
    private String jdbcPassword;
    private String jdbcDatabase;
    private String jdbcDriver;
    private String jdbcUrl;
    private int jdbcPoolSize;
    private Boolean logQueries = false;
    private Boolean logUpdates = true;

    public Config() {
        daoType = "mem";
    }
}
