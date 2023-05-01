package wombatukun.tests.test11.orderservice.dao.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import wombatukun.tests.test11.orderservice.enums.Status;

import jakarta.persistence.*;
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
    @Column(name="created_at", nullable = false)
    private Date createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name="status", nullable = false)
    private Status status;

    @Column(name="user_id", nullable = false)
    private Long userId;

    @Column(name="courier_id")
    private Long courierId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    private Details details;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
        status = Status.CREATED;
    }

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
