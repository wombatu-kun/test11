package wombatukun.tests.test11.shippingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShippingDto {

    private Long courierId;
    private Date wasAt;
    private Double latitude;
    private Double longitude;

}
