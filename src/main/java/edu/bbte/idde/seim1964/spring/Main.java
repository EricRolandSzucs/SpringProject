package edu.bbte.idde.seim1964.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
@SpringBootApplication
@Slf4j
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
