package wombatukun.tests.test11.authservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wombatukun.tests.test11.authservice.dto.CommonResponse;
import wombatukun.tests.test11.authservice.dto.UserDto;
import wombatukun.tests.test11.authservice.dto.UserForm;
import wombatukun.tests.test11.authservice.enums.Role;
import wombatukun.tests.test11.authservice.exceptions.handlers.GlobalExceptionHandler;
import wombatukun.tests.test11.authservice.services.UserService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(value="v1")
public class UserController extends GlobalExceptionHandler {

    private final UserService userService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/couriers")
    public CommonResponse<UserDto> registerCourier(@RequestBody @Valid UserForm form) {
        form.setRole(Role.ROLE_COURIER);
        return CommonResponse.success(userService.register(form));
    }

    @PostMapping(value = "/users")
    public CommonResponse<UserDto> registerUser(@RequestBody @Valid UserForm form) {
        form.setRole(Role.ROLE_USER);
        return CommonResponse.success(userService.register(form));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/auth")
    public CommonResponse<Map<String, Object>> me(Authentication authentication) {
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("user", authentication.getPrincipal());
        userInfo.put("authorities", AuthorityUtils.authorityListToSet(authentication.getAuthorities()));
        return CommonResponse.success(userInfo);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/users/{userId}")
    public CommonResponse<UserDto> getAny(@PathVariable("userId") Long userId) {
        return CommonResponse.success(userService.getById(userId));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/users/{userId}")
    public CommonResponse<UserDto> suspendAny(Authentication authentication, @PathVariable("userId") Long userId) {
        return CommonResponse.success(userService.suspend(authentication, userId));
    }

}