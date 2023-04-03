package wombatukun.tests.test11.authservice.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import wombatukun.tests.test11.authservice.enums.Status;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEvent implements Serializable {
    private String type;
    private Long id;
    private Status status;
    private Date timestamp;
    private String correlationId;
}
