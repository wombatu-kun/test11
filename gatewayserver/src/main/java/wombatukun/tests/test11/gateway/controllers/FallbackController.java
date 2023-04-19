package wombatukun.tests.test11.gateway.controllers;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import wombatukun.tests.test11.common.dto.CommonResponse;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @GetMapping
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public CommonResponse fallbackResponse() {
        return CommonResponse.failure("service unavailable, try again later");
    }

}
