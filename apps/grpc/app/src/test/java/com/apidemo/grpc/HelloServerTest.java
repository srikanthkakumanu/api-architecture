package com.apidemo.grpc;

import static org.junit.Assert.assertEquals;

import com.apidemo.grpc.HelloServer.HelloImpl;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.testing.GrpcCleanupRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Unit tests for {@link HelloServer}.
 * For demonstrating how to write gRPC unit test only.
 *
 * directExecutor() makes it easier to have deterministic tests.
 * However, if your implementation uses another thread and uses streaming it is better to use
 * the default executor, to avoid hitting bug #3084.
 *
 */
@RunWith(JUnit4.class)
public class HelloServerTest {
  /**
   * This rule manages automatic graceful shutdown for the registered servers and channels at the
   * end of test.
   */
  @Rule
  public final GrpcCleanupRule grpcCleanup = new GrpcCleanupRule();

  /**
   * To test the server, make calls with a real stub using the in-process channel, and verify
   * behaviors or state changes from the client side.
   */
  @Test
  public void greeterImpl_replyMessage() throws Exception {
    // Generate a unique in-process server name.
    String serverName = InProcessServerBuilder.generateName();

    // Create a server, add service, start, and register for automatic graceful shutdown.
    grpcCleanup.register(InProcessServerBuilder
        .forName(serverName).directExecutor().addService(new HelloImpl()).build().start());

    HelloGrpc.HelloBlockingStub blockingStub = HelloGrpc.newBlockingStub(
        // Create a client channel and register for automatic graceful shutdown.
        grpcCleanup.register(InProcessChannelBuilder.forName(serverName).directExecutor().build()));


    HelloReply reply =
        blockingStub.sayHello(HelloRequest.newBuilder().setName( "test name").build());

    assertEquals("Hello test name", reply.getMessage());
  }
}

