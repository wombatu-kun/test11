package wombatukun.tests.test11.shippingservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wombatukun.tests.test11.shippingservice.dto.CommonResponse;
import wombatukun.tests.test11.shippingservice.dto.ShippingDto;
import wombatukun.tests.test11.shippingservice.exceptions.handlers.GlobalExceptionHandler;
import wombatukun.tests.test11.shippingservice.services.ShippingService;

import java.util.List;


@RestController
@RequestMapping(value="v1/orders")
@RequiredArgsConstructor
public class ShippingController extends GlobalExceptionHandler {

    private final ShippingService shippingService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{orderId}/shipping")
    public CommonResponse<List<ShippingDto>> getOrderShipping(
            Authentication authentication,
            @PathVariable("orderId") Long orderId
    ) {
        return CommonResponse.success(shippingService.getShippingTrack(authentication, orderId));
    }

    @PreAuthorize("hasRole('ROLE_COURIER')")
    @PostMapping("/shipping")
    public void addTrackingPoint(
            Authentication authentication,
            @RequestBody ShippingDto shippingDto
    ) {
        shippingService.addShipping(authentication, shippingDto);
    }

}