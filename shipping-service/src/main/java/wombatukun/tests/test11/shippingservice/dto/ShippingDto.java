package wombatukun.tests.test11.shippingservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShippingDto {

    private Set<Long> orderIds; //todo ???
    private Date wasAt;
    private Double latitude;
    private Double longitude;

}
