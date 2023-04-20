package wombatukun.tests.test11.shippingservice.dao.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("assignment")
public class AssignmentCache {

    @Id
    private Long orderId;
    private Long courierId;

}
