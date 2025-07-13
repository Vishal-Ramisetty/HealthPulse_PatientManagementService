package com.pm.analyticsservice.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import patient.events.PatientEvent;

@Service
public class KafkaConsumer {

    private static final Logger log = LoggerFactory.getLogger(KafkaConsumer.class);

    @KafkaListener(topics = "patient", groupId = "analytics-service-group")
    public void consumeEvent(byte[] event) {
        // Logic to consume the event from Kafka
        // This could involve deserializing the event, processing it, etc.
        try {
            PatientEvent patientEvent = PatientEvent.parseFrom(event);
            // Add Business logic to handle the patient event and process it to perform analytics
            log.info("Received patient event: ID={}, Name={}, Email={}, EventType={}",
                    patientEvent.getPatientId(),
                    patientEvent.getName(),
                    patientEvent.getEmail(),
                    patientEvent.getEventType());
        }
        catch (Exception e) {
            log.error("Error while parsing patient event in Analytics Service", e);
        }
    }
}
