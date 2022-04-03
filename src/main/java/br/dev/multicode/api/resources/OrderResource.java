package br.dev.multicode.api.resources;

import br.dev.multicode.api.http.requests.OrderRequest;
import br.dev.multicode.api.http.responses.OrderResponse;
import br.dev.multicode.services.OrderService;
import java.net.URI;
import java.util.UUID;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("/api/orders")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class OrderResource {

  @Inject
  OrderService orderService;

  @POST
  public Response postOrder(@Valid OrderRequest orderRequest)
  {
    final String orderId = orderService.create(orderRequest);
    return Response.created(URI.create("/api/orders/".concat(orderId)))
        .header("id", orderId)
        .build();
  }

  @GET
  @Path("/{orderId}")
  public Response getOrder(@PathParam("orderId") UUID orderId)
  {
    final OrderResponse orderResponse = orderService.getOrderById(orderId);
    return Response.status(Status.OK)
        .entity(orderResponse)
        .build();
  }
}
