package ru.otus.protobuf;


import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.protobuf.generated.GenerateSequenceMessage;
import ru.otus.protobuf.generated.SequenceMessage;
import ru.otus.protobuf.generated.SequenceServiceGrpc;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class GRPCServer {
    private static final Logger log = LoggerFactory.getLogger(GRPCServer.class);
    public static final int SERVER_PORT = 8190;

    public static void main(String[] args) throws IOException, InterruptedException {
        var server = ServerBuilder.forPort(SERVER_PORT)
                .addService(new SequenceService())
                .build()
                .start();
        log.info("Server is up!. Waiting for termination...");
        server.awaitTermination();
    }

    private static class SequenceService extends SequenceServiceGrpc.SequenceServiceImplBase {
        @Override
        public void generateSequence(GenerateSequenceMessage request,
                                     StreamObserver<SequenceMessage> responseObserver) {

            for (int i = request.getFrom(); i <= request.getTo(); i++) {
                responseObserver.onNext(SequenceMessage.newBuilder().setNumber(i).build());
                log.info("Sequence number has been sent: {}", i);
                SleepUtils.sleep(TimeUnit.SECONDS, 2);
            }
            responseObserver.onCompleted();
        }
    }
}
