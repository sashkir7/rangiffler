package api;

import com.google.protobuf.Empty;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.qameta.allure.grpc.AllureGrpc;
import sashkir7.grpc.UsernameRequest;

abstract class BaseGrpcApi {

    private final static int MAX_INBOUND_MESSAGE_SIZE = 10 * 1024 * 1024; // 10MB

    protected final ManagedChannel channel;
    protected final Empty defaultEmptyInstance = Empty.getDefaultInstance();

    public BaseGrpcApi(String address, int port) {
        channel = ManagedChannelBuilder.forAddress(address, port)
                .maxInboundMessageSize(MAX_INBOUND_MESSAGE_SIZE)
                .intercept(new AllureGrpc())
                .usePlaintext()
                .build();
    }

    protected UsernameRequest getUsernameRequest(String username) {
        return UsernameRequest.newBuilder().setUsername(username).build();
    }

}
