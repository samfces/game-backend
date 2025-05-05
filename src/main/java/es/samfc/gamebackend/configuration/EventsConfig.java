package es.samfc.gamebackend.configuration;

import es.samfc.gamebackend.events.producer.EventProducer;
import es.samfc.gamebackend.events.producer.types.KafkaEventProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventsConfig {

    @Value("${app.events.producer.type}")
    private String producerType;

    @Bean
    public EventProducer eventProducer() {
        if (producerType.equals("kafka")) {
            return new KafkaEventProducer();
        }
        return null;
    }


}
