package com.sangria.operation.Enum;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RequestResultEnum {
	
    OK(200,"Operation Success"),

    FAIL(500,"Unknown Error"),

    AUTH_TIMEOUT(800,"Authentication Fail");

    int code;

    String message;

}
