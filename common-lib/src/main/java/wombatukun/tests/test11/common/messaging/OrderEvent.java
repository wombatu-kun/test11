package wombatukun.tests.test11.common.messaging;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import wombatukun.tests.test11.common.enums.OrderStatus;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderEvent implements Serializable {

    private String type;
    private Long id;
    private OrderStatus status;
    private Date timestamp;
    private Long userId;
    private Long courierId;
    private BigDecimal cost;
    private String correlationId;

}
