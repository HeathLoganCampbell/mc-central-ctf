package dev.cobblesword.config;

import dev.cobblesword.converters.BinaryToUUIDConverter;
import dev.cobblesword.converters.UUIDToBinaryConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.util.Arrays;

@Configuration
public class MongoDBConfig {

    @Bean
    public MongoCustomConversions customConversions() {
        return new MongoCustomConversions(Arrays.asList(
                new BinaryToUUIDConverter(),
                new UUIDToBinaryConverter()
        ));
    }

    @Bean
    public DefaultMongoTypeMapper defaultMongoTypeMapper() {
        return new DefaultMongoTypeMapper(null);
    }
}