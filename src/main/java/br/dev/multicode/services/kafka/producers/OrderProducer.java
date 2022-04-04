package br.dev.multicode.services.kafka.producers;

import br.dev.multicode.models.OrderMessage;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

@ApplicationScoped
public class OrderProducer {

  @Inject @Channel("sec-new-order")
  Emitter<OrderMessage> emitter;

  public void sendOrderToKafka(OrderMessage orderMessage)
  {
    emitter.send((orderMessage));
  }
}
