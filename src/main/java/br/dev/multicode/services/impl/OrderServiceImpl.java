package br.dev.multicode.services.impl;

import br.dev.multicode.api.http.requests.OrderRequest;
import br.dev.multicode.api.http.responses.OrderResponse;
import br.dev.multicode.entities.Order;
import br.dev.multicode.models.CurrentOrderStatus;
import br.dev.multicode.repositories.OrderRepository;
import br.dev.multicode.services.NotificationService;
import br.dev.multicode.services.OrderService;
import br.dev.multicode.services.kafka.producers.OrderProducer;
import java.util.UUID;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class OrderServiceImpl implements OrderService {

  @Inject OrderProducer orderProducer;
  @Inject OrderRepository orderRepository;
  @Inject NotificationService notificationService;

  @Override
  public String create(OrderRequest orderRequest)
  {
    Order order = orderRepository.save(Order.of(orderRequest));
    notificationService.doNotification(order.toOrderMessage(), orderProducer);
    return order.getId();
  }

  @Override
  public OrderResponse getOrderById(UUID orderId) {
    final Order order = orderRepository.findOrderById(orderId);
    return order.toResponse();
  }

  @Override
  public void updateStatus(CurrentOrderStatus currentOrderStatus)
  {
    orderRepository.updateStatusById(currentOrderStatus.getOrderId(), currentOrderStatus.getStatus());
  }
}
