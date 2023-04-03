package wombatukun.tests.test11.orderservice.mappers;

import wombatukun.tests.test11.orderservice.dao.entities.Order;
import wombatukun.tests.test11.orderservice.dao.specifications.OrderSpec;
import wombatukun.tests.test11.orderservice.dto.DetailsDto;
import wombatukun.tests.test11.orderservice.dto.OrderDto;
import wombatukun.tests.test11.orderservice.dto.SearchOrderForm;
import wombatukun.tests.test11.orderservice.dto.UserOrderForm;
import wombatukun.tests.test11.orderservice.events.OrderEvent;

public interface OrderMapper {

    OrderDto mapEntityToOrderDto(Order order);
    DetailsDto mapEntityToDetailsDto(Order order);
    Order mapFormToEntity(UserOrderForm form);
    OrderSpec mapSearchFormToSpec(SearchOrderForm form);
    OrderEvent mapEntityToEvent(Order order);

}
