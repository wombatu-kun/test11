package wombatukun.tests.test11.orderservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import wombatukun.tests.test11.orderservice.enums.Status;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchOrderForm {

	private Long userId;
	private Long courierId;
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private Date createdFrom;
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private Date createdTo;
	private Status status;

	private Integer page = 0;
	private Integer pageSize = 30;
	private Sort.Direction sortDirection = Sort.Direction.DESC;

}
