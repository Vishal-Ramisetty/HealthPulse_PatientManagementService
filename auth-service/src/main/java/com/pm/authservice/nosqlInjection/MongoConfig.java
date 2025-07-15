package com.pm.authservice.nosqlInjection;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.bson.UuidRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

@Configuration
public class MongoConfig {

    @Bean
    public MappingMongoConverter mappingMongoConverter(
            MongoDatabaseFactory factory,
            MongoMappingContext context
    ) {
        DbRefResolver dbRefResolver = new DefaultDbRefResolver(factory);
        MappingMongoConverter converter = new MappingMongoConverter(dbRefResolver, context);
        // This line removes the "_class" field from stored documents:
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));
        return converter;
    }

//    @Bean
//    public MongoClient mongoClient(@Value("${spring.data.mongodb.uri}") String uri) {
//        return MongoClients.create(uri);
//    }


    // This bean is used to create a MongoClient with a specific UUID representation.- Use When Run Locally
//    @Bean
//    public MongoClient mongoClient() {
//        MongoClientSettings settings = MongoClientSettings.builder()
//                .uuidRepresentation(UuidRepresentation.STANDARD)
//                .applyConnectionString(new ConnectionString("mongodb://localhost:27017/mongoAuthdb"))
//                .build();
//        return MongoClients.create(settings);
//    }

    @Bean
    // In Docker Container  uri--> mongodb://admin_viewer:password@auth-service-mongodb:27017/mongoAuthdb
    // In Local uri---> mongodb://localhost:27017/mongoAuthdb
    public MongoClient mongoClient(@Value("${spring.data.mongodb.uri}") String uri) {
//    public MongoClient mongoClient() {
//        String uri = "mongodb://admin_viewer:password@auth-service-mongodb:27017/mongoAuthdb";
        MongoClientSettings settings = MongoClientSettings.builder()
                .uuidRepresentation(UuidRepresentation.STANDARD)
                .applyConnectionString(new ConnectionString(uri))
                .build();
        return MongoClients.create(settings);
    }
}