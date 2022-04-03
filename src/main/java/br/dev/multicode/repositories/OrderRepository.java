package br.dev.multicode.repositories;

import br.dev.multicode.entities.Order;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import java.util.UUID;
import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.ws.rs.NotFoundException;

@ApplicationScoped
public class OrderRepository implements PanacheRepository<Order> {

  @Transactional // Event here for put in Kafka
  public String save(final Order order)
  {
    this.persistAndFlush(order);
    return order.getId();
  }

  public Order findOrderById(UUID orderId) {
    return this.find("order_id=:orderId", Parameters.with("orderId", orderId.toString())).firstResultOptional()
        .orElseThrow(() ->
            new NotFoundException("Order not found by id=".concat(orderId.toString())));
  }
}
