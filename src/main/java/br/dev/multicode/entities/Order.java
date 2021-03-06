package br.dev.multicode.entities;

import br.dev.multicode.api.http.requests.OrderRequest;
import br.dev.multicode.api.http.responses.OrderResponse;
import br.dev.multicode.enums.OrderStatus;
import br.dev.multicode.models.ItemMessage;
import br.dev.multicode.models.OrderMessage;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order extends PanacheEntityBase {

  @Id
  @Column(name = "order_id", length = 37)
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  private String id;

  @Column(name = "user_id", nullable = false, length = 37)
  private String userId;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 30)
  private OrderStatus status;

  @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinTable(name = "orders_items",
  joinColumns = @JoinColumn(name = "order_id"),
  inverseJoinColumns = @JoinColumn(name = "item_id"))
  private Set<Item> items = new HashSet<>();

  @Column(nullable = false)
  private BigDecimal price;

  @CreationTimestamp
  @Column(name = "created_at")
  private ZonedDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  private ZonedDateTime updatedAt;

  @PrePersist
  private void prePersist()
  {
    this.id = UUID.randomUUID().toString();
  }

  public static Order of(OrderRequest orderRequest)
  {
    return Order.builder()
        .userId(orderRequest.getUserId().toString())
        .status(OrderStatus.CREATED)
        .items(Item.of(orderRequest.getItems()))
        .price(orderRequest.getPrice())
        .build();
  }

  public OrderResponse toResponse()
  {
    return OrderResponse.builder()
        .orderId(id)
        .userId(userId)
        .status(status.name())
        .items(items.stream()
            .map(Item::toItemResponse)
            .collect(Collectors.toSet()))
        .price(price.toString())
        .build();
  }

  public OrderMessage toOrderMessage()
  {
    return OrderMessage.builder()
        .eventId(UUID.randomUUID())
        .orderId(UUID.fromString(id))
        .userId(UUID.fromString(userId))
        .status(status)
        .items(items.stream()
            .map(item -> new ItemMessage(item.getProductId(), item.getAmount()))
            .collect(Collectors.toSet()))
        .price(price)
        .build();
  }
}