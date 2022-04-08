package br.dev.multicode.services.kafka.consumers;

import br.dev.multicode.models.CurrentOrderStatus;
import br.dev.multicode.services.OrderService;
import io.smallrye.reactive.messaging.annotations.Blocking;
import io.smallrye.reactive.messaging.kafka.api.IncomingKafkaRecordMetadata;
import java.util.concurrent.CompletionStage;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.jboss.logging.Logger;

@ApplicationScoped
public class OrderConsumer {

  private final Logger logger = Logger.getLogger(this.getClass());

  @Inject
  OrderService orderService;

  @Blocking
  @Incoming("sec-order-status")
  public CompletionStage<Void> receiveOrderStatusFromKafka(Message<CurrentOrderStatus> currentOrderStatusMessage)
  {
    var metadata = currentOrderStatusMessage.getMetadata(IncomingKafkaRecordMetadata.class)
            .orElseThrow();

    CurrentOrderStatus currentOrderStatusReceived = currentOrderStatusMessage.getPayload();
    logger.infof("Topic: %s - Got a order message: orderId=%s", metadata.getTopic(),
        currentOrderStatusReceived.getOrderId());

    orderService.updateStatus(currentOrderStatusReceived);

    return currentOrderStatusMessage.ack();
  }
}
