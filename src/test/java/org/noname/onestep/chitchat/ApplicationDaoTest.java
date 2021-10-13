package org.noname.onestep.chitchat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan("org.noname.onestep.chitchat.domain.entity")
@SpringBootApplication(scanBasePackages = "org.noname.onestep.chitchat.domain")
@EnableJpaRepositories("org.noname.onestep.chitchat.domain.repository")
public class ApplicationDaoTest {

    public static void main(String[] args) {
        SpringApplication.run(ApplicationDaoTest.class,args);
    }
}
