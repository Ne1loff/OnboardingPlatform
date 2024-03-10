package com.example.onboardingservice.jooq;

import lombok.SneakyThrows;
import org.jooq.codegen.GenerationTool;
import org.jooq.meta.jaxb.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@SpringBootTest
public class CodeGenTest {

    @Autowired
    private Configuration config;

    @Test
    @SneakyThrows
    public void generateJooqClassesTest() {
        GenerationTool.generate(config);
    }

    @TestConfiguration
    static class JooqConfig {

        @Primary
        @Bean("codgen-config")
        public Configuration configuration(DataSourceProperties properties) {
            return new Configuration()
                    .withJdbc(new Jdbc()
                            .withDriver(properties.getDriverClassName())
                            .withUrl(properties.getUrl())
                            .withUser(properties.getUsername())
                            .withPassword(properties.getPassword()))
                    .withGenerator(new Generator()
                            .withDatabase(new Database()
                                    .withName("org.jooq.meta.postgres.PostgresDatabase")
                                    .withIncludes(".*")
                                    .withExcludes("")
                                    .withInputSchema("public"))
                            .withTarget(new Target()
                                    .withPackageName("com.example.onboardingservice.jooq")
                                    .withDirectory("src/main/java")));
        }
    }
}
