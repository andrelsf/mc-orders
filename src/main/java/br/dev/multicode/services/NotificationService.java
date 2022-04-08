package br.dev.multicode.services;

import br.dev.multicode.models.OrderMessage;
import br.dev.multicode.services.kafka.producers.ProducerService;

public interface NotificationService {

  void doNotification(OrderMessage orderMessage, ProducerService producerService);

}
