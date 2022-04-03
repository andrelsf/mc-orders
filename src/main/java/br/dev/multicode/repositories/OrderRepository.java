package br.dev.multicode.repositories;

import br.dev.multicode.entities.Order;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

@ApplicationScoped
public class OrderRepository implements PanacheRepository<Order> {

  @Transactional // Event here for put in Kafka
  public String save(final Order order)
  {
    this.persistAndFlush(order);
    return order.getId();
  }
}
