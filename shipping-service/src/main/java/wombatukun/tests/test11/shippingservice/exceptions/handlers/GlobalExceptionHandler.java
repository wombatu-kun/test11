package wombatukun.tests.test11.shippingservice.exceptions.handlers;

import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.ClientAbortException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import wombatukun.tests.test11.common.dto.CommonResponse;
import wombatukun.tests.test11.common.exceptions.OperationNotPermittedException;
import wombatukun.tests.test11.common.exceptions.ResourceNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public CommonResponse handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
		List<String> errors = exception.getBindingResult()
				.getFieldErrors()
				.stream()
				.map(e -> e.getField() + "=" + e.getRejectedValue() + ": " + e.getDefaultMessage())
				.collect(Collectors.toList());
		String errorMessage = Joiner.on("; ").join(errors);
		return CommonResponse.failure(errorMessage);
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public CommonResponse handleResourceNotFound(ResourceNotFoundException exception) {
		return CommonResponse.failure(exception.getMessage());
	}

	@ExceptionHandler(AuthenticationException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public CommonResponse handleAuthenticationException(AuthenticationException exception) {
		return CommonResponse.failure(exception.getMessage());
	}

	@ExceptionHandler(AccessDeniedException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public CommonResponse handleAccessDeniedException(AccessDeniedException exception) {
		return CommonResponse.failure(exception.getMessage());
	}

	@ExceptionHandler(OperationNotPermittedException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public CommonResponse handleOperationNotPermitted(OperationNotPermittedException exception) {
		return CommonResponse.failure(exception.getMessage());
	}

	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public CommonResponse handleIllegalArgument(IllegalArgumentException exception) {
		return CommonResponse.failure(exception.getMessage());
	}

	@ExceptionHandler(ClientAbortException.class)
	public CommonResponse handleClientAbortException(ClientAbortException exception) {
		return null;
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public CommonResponse handleHttpMessageNotReadable(HttpMessageNotReadableException exception) {
		log.error(exception.getMessage());
		return CommonResponse.failure(exception.getMessage());
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public CommonResponse handleException(Exception exception) {
		exception.printStackTrace();
		return CommonResponse.failure(exception.getClass().getName() + ": " + exception.getMessage());
	}

}
