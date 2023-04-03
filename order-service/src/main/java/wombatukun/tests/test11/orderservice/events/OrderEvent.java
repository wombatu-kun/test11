package wombatukun.tests.test11.orderservice.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import wombatukun.tests.test11.orderservice.enums.Status;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderEvent implements Serializable {
    private String type;
    private Long id;
    private Status status;
    private Date timestamp;
    private Long courierId;
    private String correlationId;
}
