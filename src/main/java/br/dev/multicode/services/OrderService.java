package br.dev.multicode.services;

import br.dev.multicode.api.http.requests.OrderRequest;

public interface OrderService {

  String create(OrderRequest orderRequest);

}
