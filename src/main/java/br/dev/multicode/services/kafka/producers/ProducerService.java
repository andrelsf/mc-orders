package br.dev.multicode.services.kafka.producers;

import br.dev.multicode.models.OrderMessage;
import io.smallrye.mutiny.Uni;

public interface ProducerService {

  Uni<Void> sendToKafka(OrderMessage orderMessage);

}
