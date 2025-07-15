package com.pm.authservice.nosqlInjection;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.repository.init.Jackson2RepositoryPopulatorFactoryBean;


@Configuration
public class MongoInitRunner {

    // This class is used to initialize the MongoDB with data from a JSON file.
    // The JSON file should be placed in the resources directory of the project.
    // The data will be loaded into the MongoDB when the application starts.

    @Bean
    public Jackson2RepositoryPopulatorFactoryBean repositoryPopulator() {
        Jackson2RepositoryPopulatorFactoryBean factory = new Jackson2RepositoryPopulatorFactoryBean();
        factory.setResources(new Resource[]{
                new ClassPathResource("users.json")
        });
        return factory;

    }
}
