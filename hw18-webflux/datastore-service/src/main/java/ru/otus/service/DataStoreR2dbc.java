package ru.otus.service;

import java.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import ru.otus.domain.Message;
import ru.otus.repository.MessageRepository;

import static java.time.temporal.ChronoUnit.SECONDS;

@Service
public class DataStoreR2dbc implements DataStore {
    private static final Logger log = LoggerFactory.getLogger(DataStoreR2dbc.class);
    private final MessageRepository messageRepository;
    private final Scheduler workerPool;
    @Value("${app.special-room}")
    private String specialRoom;

    public DataStoreR2dbc(Scheduler workerPool, MessageRepository messageRepository) {
        this.workerPool = workerPool;
        this.messageRepository = messageRepository;
    }

    @Override
    public Mono<Message> saveMessage(Message message) {
        log.info("saveMessage:{}", message);
        if (specialRoom.equals(message.getRoomId())) {
            throw new IllegalArgumentException(String.format("Message cannot be saved with room: %s", specialRoom));
        }
        return messageRepository.save(message);
    }

    @Override
    public Flux<Message> loadMessages(String roomId) {
        log.info("loadMessages roomId:{}", roomId);

        Flux<Message> messageFlux;
        if (specialRoom.equals(roomId)) {
            messageFlux = messageRepository.findAllByOrderByIdAsc();
        } else {
            messageFlux = messageRepository.findAllByRoomIdOrderByIdAsc(roomId);
        }

        return messageFlux
                .delayElements(Duration.of(3, SECONDS), workerPool);
    }
}
