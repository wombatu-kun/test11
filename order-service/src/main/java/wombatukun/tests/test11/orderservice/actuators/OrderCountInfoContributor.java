package wombatukun.tests.test11.orderservice.actuators;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;
import wombatukun.tests.test11.orderservice.dao.projections.OrderCount;
import wombatukun.tests.test11.orderservice.services.OrderService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OrderCountInfoContributor implements InfoContributor {

    private final OrderService orderService;

    @Override
    public void contribute(Info.Builder builder) {
        Map<String, Object> statsMap = new HashMap<>();
        List<OrderCount> totals = orderService.countTotalsByStatuses();
        totals.forEach(total -> statsMap.put(total.getStatus().name(), total.getCount()));
        builder.withDetail("order-stats", statsMap);
    }
}
