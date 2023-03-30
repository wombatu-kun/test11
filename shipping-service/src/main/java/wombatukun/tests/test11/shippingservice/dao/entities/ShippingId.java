package wombatukun.tests.test11.shippingservice.dao.entities;

import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@NoArgsConstructor
public class ShippingId implements Serializable {

    private Long orderId;
    private Date wasAt;

}
