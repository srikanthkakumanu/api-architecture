package com.apidemo.grpc;

import static org.junit.Assert.assertEquals;
import static org.mockito.AdditionalAnswers.delegatesTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import io.grpc.ManagedChannel;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.stub.StreamObserver;
import io.grpc.testing.GrpcCleanupRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;

/**
 * Unit tests for {@link HelloClient}.
 * For demonstrating how to write gRPC unit test only.
 *
 * directExecutor() makes it easier to have deterministic tests.
 * However, if your implementation uses another thread and uses streaming it is better to use
 * the default executor, to avoid hitting bug #3084.
 *
 */
@RunWith(JUnit4.class)
public class HelloClientTest {
  /**
   * This rule manages automatic graceful shutdown for the registered servers and channels at the
   * end of test.
   */
  @Rule
  public final GrpcCleanupRule grpcCleanup = new GrpcCleanupRule();

  private final HelloGrpc.HelloImplBase serviceImpl =
      mock(HelloGrpc.HelloImplBase.class, delegatesTo(
          new HelloGrpc.HelloImplBase() {
          // By default the client will receive Status.UNIMPLEMENTED for all RPCs.
          // You might need to implement necessary behaviors for your test here, like this:
          //
          // @Override
          // public void sayHello(HelloRequest request, StreamObserver<HelloReply> respObserver) {
          //   respObserver.onNext(HelloReply.getDefaultInstance());
          //   respObserver.onCompleted();
          // }
          }));

  private HelloClient client;

  @Before
  public void setUp() throws Exception {
    // Generate a unique in-process server name.
    String serverName = InProcessServerBuilder.generateName();

    // Create a server, add service, start, and register for automatic graceful shutdown.
    grpcCleanup.register(InProcessServerBuilder
        .forName(serverName).directExecutor().addService(serviceImpl).build().start());

    // Create a client channel and register for automatic graceful shutdown.
    ManagedChannel channel = grpcCleanup.register(
        InProcessChannelBuilder.forName(serverName).directExecutor().build());

    // Create a HelloWorldClient using the in-process channel;
    client = new HelloClient(channel);
  }

  /**
   * To test the client, call from the client against the fake server, and verify behaviors or state
   * changes from the server side.
   */
  @Test
  public void greet_messageDeliveredToServer() {
    ArgumentCaptor<HelloRequest> requestCaptor = ArgumentCaptor.forClass(HelloRequest.class);

    client.hello("test name");

    verify(serviceImpl)
        .sayHello(requestCaptor.capture(), ArgumentMatchers.<StreamObserver<HelloReply>>any());
    assertEquals("test name", requestCaptor.getValue().getName());
  }
}