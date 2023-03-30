package wombatukun.tests.test11.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;


@Data
@AllArgsConstructor
public class PageDto<T> {

	private List<T> page;
	private long totalElements;
	private long totalPages;
	private long pageSize;
	private long pageNumber;

	public static <T> PageDto<T> of(Page<T> page) {
		return new PageDto<>(page.getContent(), page.getTotalElements(), page.getTotalPages(), page.getSize(), page.getNumber());
	}

	public static <T, U> PageDto<U> of(Page<T> page, Function<? super T, ? extends U> converter) {
		return new PageDto<>(
				page.getContent().stream().map(converter).collect(toList()),
				page.getTotalElements(),
				page.getTotalPages(),
				page.getSize(),
				page.getNumber()
		);
	}

}
