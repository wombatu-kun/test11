package wombatukun.tests.test11.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import wombatukun.tests.test11.orderservice.enums.Status;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDto {

    private Long id;
    private Date createdAt;
    private Status status;
    private Long userId;
    private Long courierId;

}
