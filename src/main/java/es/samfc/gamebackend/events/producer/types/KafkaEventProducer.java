package es.samfc.gamebackend.events.producer.types;

import es.samfc.gamebackend.events.Event;
import es.samfc.gamebackend.events.producer.EventProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaEventProducer implements EventProducer {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;
    @Value("${app.kafka.topic.main}")
    private String mainTopic;

    @Override
    public void callEvent(Event event) {
        kafkaTemplate.send(mainTopic, event);
    }
}
