package ru.otus.protobuf;

import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.protobuf.generated.GenerateSequenceMessage;
import ru.otus.protobuf.generated.SequenceMessage;
import ru.otus.protobuf.generated.SequenceServiceGrpc;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static ru.otus.protobuf.GRPCServer.SERVER_PORT;

public class GRPCClient {
    private static final Logger log = LoggerFactory.getLogger(GRPCClient.class);
    private static final String SERVER_HOST = "localhost";
    public static final int MAX_VALUE = 50;
    private static final AtomicInteger value = new AtomicInteger(0);

    public static void main(String[] args) {
        var channel = ManagedChannelBuilder.forAddress(SERVER_HOST, SERVER_PORT)
                .usePlaintext()
                .build();
        var stub = SequenceServiceGrpc.newBlockingStub(channel);

        new Thread(GRPCClient::increaseValueWithTimeout).start();

        stub.generateSequence(createGenerateSequenceMessage(0, 10))
                .forEachRemaining(GRPCClient::processSequenceMessage);

        channel.shutdown();
    }

    private static void increaseValueWithTimeout() {
        while (value.get() < MAX_VALUE) {
            log.info("Increment value to: {}", value.incrementAndGet());
            SleepUtils.sleep(TimeUnit.SECONDS, 1);
        }
    }

    private static GenerateSequenceMessage createGenerateSequenceMessage(int from, int to) {
        return GenerateSequenceMessage.newBuilder()
                .setFrom(from)
                .setTo(to)
                .build();
    }

    private static void processSequenceMessage(SequenceMessage sequenceMessage) {
        var updatedValue = value.updateAndGet(i -> i + sequenceMessage.getNumber());
        log.info("Received server-side number: {}. Set value to {}",
                sequenceMessage.getNumber(),
                updatedValue);
    }
}
