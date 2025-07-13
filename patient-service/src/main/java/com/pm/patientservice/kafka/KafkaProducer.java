package com.pm.patientservice.kafka;


import com.pm.patientservice.model.Patient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import patient.events.PatientEvent;

@Service
public class KafkaProducer {

    private static final Logger log = LoggerFactory.getLogger(KafkaProducer.class);
    private final KafkaTemplate<String, byte[]> kafkaTemplate;
    public KafkaProducer(KafkaTemplate<String, byte[]> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    public void sendEvent(Patient patient, String eventType) {
        log.info("Sending event for patient: {} ", patient.toString());
        PatientEvent event= PatientEvent.newBuilder()
                .setPatientId(patient.getId().toString())
                .setName(patient.getName())
                .setEmail(patient.getEmail())
                .setEventType(eventType)
                .build();
        try{
            kafkaTemplate.send("patient", event.toByteArray());
            log.info("Event sent successfully for patient: {}", patient.getId());
        } catch (Exception e) {
            log.error("Failed to send event for patient: {}. Error: {}", patient.getId(), e.getMessage());
        }
    }

}
