package wombatukun.tests.test11.queryservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import wombatukun.tests.test11.common.enums.UserStatus;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserStatDto {

    private Long userId;
    private String name;
    private String email;
    private UserStatus status;
    private Long countDelivered;
    private Long countInProgress;
    private Long countTotal;
    private BigDecimal totalProfit;
    private Date createdAt;
    private Date suspendedAt;
    private Date deletedAt;

}
