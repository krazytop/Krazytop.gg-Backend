package com.krazytop;

import com.krazytop.controller.lol.LOLNomenclatureController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;

@ConfigurationPropertiesScan
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class KrazytopBack {

    private final LOLNomenclatureController lolNomenclatureController;

    @Autowired
    public KrazytopBack(LOLNomenclatureController lolNomenclatureController){
        this.lolNomenclatureController = lolNomenclatureController;
    }

    public static void main(String[] args) {
        SpringApplication.run(KrazytopBack.class, args);
    }

    @Bean
    public ApplicationRunner startup() {
        return args -> lolNomenclatureController.updateNomenclatures();
    }
}
