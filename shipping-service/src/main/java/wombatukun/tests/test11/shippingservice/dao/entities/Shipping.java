package wombatukun.tests.test11.shippingservice.dao.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;
import java.util.Objects;

@Data
@NoArgsConstructor
@Entity
@Table(name = "shipping")
@IdClass(ShippingId.class)
@AllArgsConstructor
public class Shipping {

    @Id
    private Long orderId;
    @Id @Temporal(TemporalType.TIMESTAMP)
    @Column(name="was_at", nullable = false)
    private Date wasAt;
    @Column(name="courier_id", nullable = false)
    private Long courierId;
    @Column(name="latitude", nullable = false)
    private Double latitude;
    @Column(name="longitude", nullable = false)
    private Double longitude;

    @PrePersist
    protected void onCreate() {
        if (wasAt == null) {
            wasAt = new Date();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Shipping shipping = (Shipping) o;
        return orderId.equals(shipping.orderId) &&
                wasAt.equals(shipping.wasAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, wasAt);
    }

}
