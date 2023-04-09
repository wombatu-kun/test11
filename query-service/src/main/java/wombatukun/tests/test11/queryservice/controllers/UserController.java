package wombatukun.tests.test11.queryservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wombatukun.tests.test11.common.dto.CommonResponse;
import wombatukun.tests.test11.queryservice.dto.UserStatDto;
import wombatukun.tests.test11.queryservice.exceptions.handlers.GlobalExceptionHandler;
import wombatukun.tests.test11.queryservice.services.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value="v1")
public class UserController extends GlobalExceptionHandler {

    private final UserService userService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/couriers-stats")
    public CommonResponse<List<UserStatDto>> getCouriersStats() {
        return CommonResponse.success(userService.getCouriersStats());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/users-stats")
    public CommonResponse<List<UserStatDto>> getUsersStats() {
        return CommonResponse.success(userService.getUsersStats());
    }

}
