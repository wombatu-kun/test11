package wombatukun.tests.test11.orderservice.services;

import org.springframework.security.core.Authentication;
import wombatukun.tests.test11.orderservice.dao.projections.OrderCount;
import wombatukun.tests.test11.orderservice.dto.AssignOrderForm;
import wombatukun.tests.test11.orderservice.dto.DetailsDto;
import wombatukun.tests.test11.orderservice.dto.OrderDto;
import wombatukun.tests.test11.orderservice.dto.PageDto;
import wombatukun.tests.test11.orderservice.dto.SearchOrderForm;
import wombatukun.tests.test11.orderservice.dto.UserOrderForm;
import wombatukun.tests.test11.orderservice.enums.Status;

import java.util.List;

public interface OrderService {

    List<OrderCount> countTotalsByStates();
    PageDto<OrderDto> search(Authentication authentication, SearchOrderForm form);
    DetailsDto getOrderDetailsById(Authentication authentication, Long orderId);
    DetailsDto createOrder(Authentication authentication, UserOrderForm form);
    DetailsDto assignOrder(Authentication authentication, Long orderId, AssignOrderForm form);
    DetailsDto updateDestination(Authentication authentication, Long orderId, String destination);
    OrderDto updateStatus(Authentication authentication, Long orderId, Status status);
    OrderDto cancelOrder(Authentication authentication, Long orderId);

}
