package wombatukun.tests.test11.authservice.actuators;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;
import wombatukun.tests.test11.authservice.dao.projections.UserCount;
import wombatukun.tests.test11.authservice.services.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class UserCountInfoContributor implements InfoContributor {

    private final UserService userService;

    @Override
    public void contribute(Info.Builder builder) {
        Map<String, Object> statsMap = new HashMap<>();
        /*statsMap.put("adminCount", userService.count(Role.ROLE_ADMIN));
        statsMap.put("userCount", userService.count(Role.ROLE_USER));
        statsMap.put("courierCount", userService.count(Role.ROLE_COURIER));*/
        List<UserCount> totals = userService.countTotalsByRoles();
        totals.forEach(total -> statsMap.put(total.getRole().name(), total.getCount()));
        builder.withDetail("user-stats", statsMap);
    }
}
