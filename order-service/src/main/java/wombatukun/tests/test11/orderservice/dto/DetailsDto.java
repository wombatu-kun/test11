package wombatukun.tests.test11.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import wombatukun.tests.test11.orderservice.enums.Status;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetailsDto {

    private Long id;
    private Date createdAt;
    private Status status;
    private BigDecimal cost;
    private String departure;
    private String destination;
    private String recipientName;
    private String recipientPhone;
    private Long userId;
    private Long courierId;

}
