package br.dev.multicode.services;

import br.dev.multicode.api.http.requests.OrderRequest;
import br.dev.multicode.api.http.responses.OrderResponse;
import java.util.UUID;

public interface OrderService {

  String create(OrderRequest orderRequest);

  OrderResponse getOrderById(UUID orderId);

}
