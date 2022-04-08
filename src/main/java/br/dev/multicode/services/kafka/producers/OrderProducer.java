package br.dev.multicode.services.kafka.producers;

import br.dev.multicode.models.OrderMessage;
import io.smallrye.mutiny.Uni;
import java.util.concurrent.CompletableFuture;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.OnOverflow;
import org.eclipse.microprofile.reactive.messaging.OnOverflow.Strategy;
import org.jboss.logging.Logger;

@ApplicationScoped
public class OrderProducer implements ProducerService {

  private final Logger logger = Logger.getLogger(this.getClass());

  @Inject
  @Channel("sec-new-order")
  @OnOverflow(value = Strategy.BUFFER, bufferSize = 20)
  Emitter<OrderMessage> emitter;

  @Override
  public Uni<Void> sendToKafka(OrderMessage orderMessage)
  {
    logger.infof("Start of send message to Kafka topic sec-new-order");

    emitter.send(Message.of(orderMessage)
        .withAck(() -> {
          logger.infof("Message sent successfully. eventId=%s", orderMessage.getEventId());
            return CompletableFuture.completedFuture(null);
        })
        .withNack(throwable -> {
          logger.errorf("Message sent failed. ERROR: %s", throwable.getMessage());
          return CompletableFuture.completedFuture(null);
        }));

    return Uni.createFrom().voidItem();
  }
}
