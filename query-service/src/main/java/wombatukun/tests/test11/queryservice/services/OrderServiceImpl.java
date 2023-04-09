package wombatukun.tests.test11.queryservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import wombatukun.tests.test11.common.messaging.OrderEvent;
import wombatukun.tests.test11.queryservice.dao.repositories.OrderRepository;
import wombatukun.tests.test11.queryservice.mappers.OrderMapper;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Override
    public void handleEvent(OrderEvent event) {

    }

}
