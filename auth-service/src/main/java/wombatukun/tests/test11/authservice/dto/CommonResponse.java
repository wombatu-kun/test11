package wombatukun.tests.test11.authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResponse<T> {

	private T content;
	private String error;

	public static <T> CommonResponse<T> success(T content) {
		return new CommonResponse<>(content, null);
	}

	public static <T> CommonResponse<T> failure(String error) {
		return new CommonResponse<>(null, error);
	}

}
