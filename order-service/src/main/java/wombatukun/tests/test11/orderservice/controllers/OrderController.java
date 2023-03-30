package wombatukun.tests.test11.orderservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wombatukun.tests.test11.orderservice.dto.AssignOrderForm;
import wombatukun.tests.test11.orderservice.dto.CommonResponse;
import wombatukun.tests.test11.orderservice.dto.DetailsDto;
import wombatukun.tests.test11.orderservice.dto.OrderDto;
import wombatukun.tests.test11.orderservice.dto.PageDto;
import wombatukun.tests.test11.orderservice.dto.SearchOrderForm;
import wombatukun.tests.test11.orderservice.dto.UserOrderForm;
import wombatukun.tests.test11.orderservice.enums.Status;
import wombatukun.tests.test11.orderservice.exceptions.handlers.GlobalExceptionHandler;
import wombatukun.tests.test11.orderservice.services.OrderService;

import javax.validation.Valid;


@RestController
@RequestMapping(value="v1/orders")
@RequiredArgsConstructor
public class OrderController extends GlobalExceptionHandler {

    private final OrderService orderService;

    @GetMapping
    public CommonResponse<PageDto<OrderDto>> search(
            Authentication authentication,
            SearchOrderForm form
    ) {
        return CommonResponse.success(orderService.search(authentication, form));
    }

    @GetMapping(value="/{orderId}")
    public CommonResponse<DetailsDto> getOrder(
            Authentication authentication,
            @PathVariable("orderId") Long orderId
    ) {
        return CommonResponse.success(orderService.getOrderDetailsById(authentication, orderId));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping
    public CommonResponse<DetailsDto> createOrder(
            Authentication authentication,
            @RequestBody @Valid UserOrderForm form
    ) {
        return CommonResponse.success(orderService.createOrder(authentication, form));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value="/{orderId}")
    public CommonResponse<DetailsDto> assignOrder(
            Authentication authentication,
            @PathVariable("orderId") Long orderId,
            @RequestBody @Valid AssignOrderForm form
    ) {
        return CommonResponse.success(orderService.assignOrder(authentication, orderId, form));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_COURIER')")
    @PutMapping(value="/{orderId}/status")
    public CommonResponse<OrderDto> updateOrderStatus(
            Authentication authentication,
            @PathVariable("orderId") Long orderId,
            @RequestBody String status
    ) {
        return CommonResponse.success(orderService.updateStatus(authentication, orderId, Status.valueOf(status)));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping(value="/{orderId}/destination")
    public CommonResponse<DetailsDto> updateDestination(
            Authentication authentication,
            @PathVariable("orderId") Long orderId,
            @RequestBody String destination
    ) {
        return CommonResponse.success(orderService.updateDestination(authentication, orderId, destination));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping(value="/{orderId}")
    public CommonResponse<OrderDto> cancelOrder(
            Authentication authentication,
            @PathVariable("orderId") Long orderId
    ) {
        return CommonResponse.success(orderService.cancelOrder(authentication, orderId));
    }

}
