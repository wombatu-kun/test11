package wombatukun.tests.test11.queryservice.dao.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import wombatukun.tests.test11.common.enums.OrderStatus;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

@Data
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {

    @Id
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status;

    private BigDecimal cost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courier_id")
    private User courier;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "assigned_at")
    private Date assignedAt;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "shipped_at")
    private Date shippedAt;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "delivered_at")
    private Date deliveredAt;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "cancelled_at")
    private Date cancelledAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

}
