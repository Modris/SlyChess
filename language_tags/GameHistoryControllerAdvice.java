package com.modris.advice;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.modris.exceptions.FenIsInvalidException;
import com.modris.exceptions.GameNotFoundException;
import com.modris.exceptions.HistoryIsEmptyException;
import com.modris.exceptions.StatisticsNotFoundException;

import jakarta.servlet.http.HttpServletResponse;

@RestControllerAdvice
public class GameHistoryControllerAdvice {

	@ExceptionHandler(GameNotFoundException.class)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	void gameNotFoundHandler(GameNotFoundException ex, HttpServletResponse response) {
		response.addHeader("X-SERVER-RESPONSE", ex.getMessage()); 
		// since 204 means no content we return a custom header instead.
	}
	
	@ExceptionHandler(StatisticsNotFoundException.class)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	void statisticsNotFoundHandler(StatisticsNotFoundException ex,  HttpServletResponse response) {
		response.addHeader("X-SERVER-RESPONSE", ex.getMessage()); 
		// since 204 means no content we return a custom header instead.
	}
	
	@ExceptionHandler(HistoryIsEmptyException.class)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	void historyIsEmptyHandler(HistoryIsEmptyException ex, HttpServletResponse response) {
		response.addHeader("X-SERVER-RESPONSE", ex.getMessage());
	}
	
	@ExceptionHandler(FenIsInvalidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST) //400
	String fenIsInvalidHandler(FenIsInvalidException ex) {
		return ex.getMessage();
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
		var errors = new HashMap<String, String>();
		ex.getBindingResult().getAllErrors().forEach(error -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});
		return errors;
	}
	
}
