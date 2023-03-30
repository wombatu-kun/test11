package wombatukun.tests.test11.orderservice.dao.projections;

import wombatukun.tests.test11.orderservice.enums.Status;

public interface OrderCount {
    Status getStatus();
    Long getCount();
}
