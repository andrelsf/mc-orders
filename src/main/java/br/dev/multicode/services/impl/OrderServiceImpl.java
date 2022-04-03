package br.dev.multicode.services.impl;

import br.dev.multicode.api.http.requests.OrderRequest;
import br.dev.multicode.api.http.responses.OrderResponse;
import br.dev.multicode.entities.Order;
import br.dev.multicode.repositories.OrderRepository;
import br.dev.multicode.services.OrderService;
import java.util.UUID;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class OrderServiceImpl implements OrderService {

  @Inject
  OrderRepository orderRepository;

  @Override
  public String create(OrderRequest orderRequest)
  {
    return orderRepository.save(Order.of(orderRequest));
  }

  @Override
  public OrderResponse getOrderById(UUID orderId) {
    final Order order = orderRepository.findOrderById(orderId);
    return order.toResponse();
  }
}
