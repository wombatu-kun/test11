package wombatukun.tests.test11.orderservice.dao.queries;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import wombatukun.tests.test11.orderservice.enums.Status;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderQuery implements Serializable {

	private Long userId;
	private Long courierId;
	private Date createdFrom;
	private Date createdTo;
	private Status status;

}
