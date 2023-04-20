package wombatukun.tests.test11.queryservice.dao.projections;

import wombatukun.tests.test11.common.enums.UserStatus;

import java.math.BigDecimal;
import java.util.Date;

public interface UserStat {

    Long getUserId();
    String getName();
    String getEmail();
    UserStatus getStatus();
    Date getCreatedAt();
    Date getSuspendedAt();
    Date getDeletedAt();
    Long getCountDelivered();
    Long getCountInProgress();
    Long getCountTotal();
    BigDecimal getTotalProfit();

}
