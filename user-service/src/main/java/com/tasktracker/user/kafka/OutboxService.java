package com.tasktracker.user.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tasktracker.user.entity.OutboxEvent;
import com.tasktracker.user.repository.OutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OutboxService {

    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    /**
     * Сохранить событие в outbox (в той же транзакции).
     */
    @Transactional
    public void saveEvent(UUID aggregateId, String aggregateType, String eventType, Object payload) {
        try {
            String json = objectMapper.writeValueAsString(payload);
            OutboxEvent event = OutboxEvent.builder()
                    .aggregateId(aggregateId)
                    .aggregateType(aggregateType)
                    .eventType(eventType)
                    .payload(json)
                    .status("PENDING")
                    .build();
            outboxRepository.save(event);
            log.info("Saved outbox event: {} for {}", eventType, aggregateId);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize event", e);
            throw new RuntimeException("Failed to save outbox event", e);
        }
    }
}