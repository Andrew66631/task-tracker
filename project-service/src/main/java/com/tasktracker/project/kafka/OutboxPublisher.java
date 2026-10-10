package com.tasktracker.project.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tasktracker.project.entity.OutboxEvent;
import com.tasktracker.project.repository.OutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxPublisher {

    private final OutboxRepository outboxRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Scheduled(fixedDelay = 5000)
    @Transactional
    public void publishPendingEvents() {
        List<OutboxEvent> events = outboxRepository.findByStatusOrderByCreatedAtAsc("PENDING");

        for (OutboxEvent event : events) {
            try {
                Object payload = objectMapper.readValue(event.getPayload(), Object.class);
                kafkaTemplate.send(event.getEventType(), event.getAggregateId().toString(), payload);

                event.setStatus("SENT");
                event.setSentAt(LocalDateTime.now());
                outboxRepository.save(event);
                log.info("Published event: {} for {}", event.getEventType(), event.getAggregateId());
            } catch (Exception e) {
                log.error("Failed to publish event: {}", event.getId(), e);
                event.setStatus("FAILED");
                outboxRepository.save(event);
            }
        }
    }
}