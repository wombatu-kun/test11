package wombatukun.tests.test11.orderservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserOrderForm {

    private Long userId;
    @NotEmpty
    private String departure;
    @NotEmpty
    private String destination;
    @NotEmpty
    private String recipientName;
    @NotEmpty
    private String recipientPhone;

}
