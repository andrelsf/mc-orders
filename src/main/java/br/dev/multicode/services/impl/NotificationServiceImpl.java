package br.dev.multicode.services.impl;

import br.dev.multicode.models.OrderMessage;
import br.dev.multicode.services.NotificationService;
import br.dev.multicode.services.kafka.producers.ProducerService;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class NotificationServiceImpl implements NotificationService {

  @Override
  public void doNotification(OrderMessage orderMessage, ProducerService producerService)
  {
    Uni.createFrom()
        .item(orderMessage)
        .emitOn(Infrastructure.getDefaultWorkerPool())
        .subscribe()
        .with(producerService::sendToKafka, Throwable::getMessage);
  }
}
