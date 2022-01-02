package com.invocify.invoice.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.Getter;

@Getter
public class BaseResponse {

	private Object data;
	private List<String> errors;

	public BaseResponse() {
		this.errors = new ArrayList<>();
	}

	public BaseResponse(Object data) {
		this.data = data;
		this.errors = new ArrayList<>();
	}

	public void appendErrors(String error) {
		this.errors.add(error);
	}

	public List<String> getErrors() {
		Collections.sort(this.errors);
		return this.errors;
	}
}
