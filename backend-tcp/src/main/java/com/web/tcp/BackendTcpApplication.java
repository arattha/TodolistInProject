package com.web.tcp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;

@SpringBootApplication
public class BackendTcpApplication {

    public static void main(String[] args) {

        //SpringApplication.run(BackendApplication.class, args);
        SpringApplication app = new SpringApplication(BackendTcpApplication.class);
        app.addListeners(new ApplicationPidFileWriter());
        app.run(args);

    }

}
