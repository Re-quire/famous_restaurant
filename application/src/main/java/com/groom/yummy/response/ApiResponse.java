package com.groom.yummy.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ApiResponse<T> {
	private T data;
	private String message;

	public ApiResponse(T data, String message) {
		this.data = data;
		this.message = message;
	}

}