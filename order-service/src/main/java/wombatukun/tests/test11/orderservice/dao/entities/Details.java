package wombatukun.tests.test11.orderservice.dao.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Objects;

@Data
@NoArgsConstructor
@Entity
@Table(name = "details")
public class Details {

    @Id
    @SequenceGenerator(name = "pk_seq_orders", sequenceName = "order_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pk_seq_orders")
    @Column(name = "order_id")
    private Long id;
    private BigDecimal cost;
    private String departure;
    private String destination;
    private String recipientName;
    private String recipientPhone;

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        Details details = (Details) o;
        return Objects.equals(id, details.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

}
