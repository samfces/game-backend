package es.samfc.gamebackend.events.producer.types;

import es.samfc.gamebackend.events.Event;
import es.samfc.gamebackend.events.producer.EventProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaEventProducer implements EventProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaEventProducer.class);

    @Autowired private KafkaTemplate<String, Object> kafkaTemplate;
    @Value("${app.kafka.topic.main}")
    private String mainTopic;

    @Override
    public void callEvent(Event event) {
        kafkaTemplate.send(mainTopic, event).thenAccept(recordMetadata -> {
            LOGGER.info("Evento {} enviado correctamente", event.getEventType());
        }).exceptionally(throwable -> {
            LOGGER.error("Error enviando evento {}", event.getEventType(), throwable);
            return null;
        });
    }
}
