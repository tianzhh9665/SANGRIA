package com.sangria.client.dto;
import lombok.*;

@Data
@AllArgsConstructor
public class ResponseDTO<T> {
	private int code;
	
	private String meg;
	
	private T data;
}
