package com.sangria.client.controller;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.sangria.client.Enum.RequestResultEnum;
import com.sangria.client.dto.ResponseDTO;

public class BaseController {

	public ResponseDTO renderOk() {
        return new ResponseDTO(RequestResultEnum.OK.getCode(), RequestResultEnum.OK.getMessage(), null);
    }

    public ResponseDTO renderOk(String message) {
        return new ResponseDTO(RequestResultEnum.OK.getCode(), message, null);
    }

    public ResponseDTO renderOk(Object data) {
        return new ResponseDTO(RequestResultEnum.OK.getCode(), RequestResultEnum.OK.getMessage(), data);
    }

    public ResponseDTO renderOkData(Object data) {
        return new ResponseDTO(RequestResultEnum.OK.getCode(), RequestResultEnum.OK.getMessage(), data);
    }

    public ResponseDTO renderOkData(Object... params) {
        Map<String, Object> data = new HashMap<>();
        for (int i = 0; i < params.length; i = i + 2) {
            data.put(params[i].toString(), params[i + 1]);
        }
        return new ResponseDTO(RequestResultEnum.OK.getCode(), RequestResultEnum.OK.getMessage(), data);
    }

    public ResponseDTO renderOk(String message, Object data) {
        return new ResponseDTO(RequestResultEnum.OK.getCode(), message, data);
    }

    public ResponseDTO renderFail() {
        return new ResponseDTO(RequestResultEnum.FAIL.getCode(), RequestResultEnum.FAIL.getMessage(), null);
    }

    public ResponseDTO renderFail(String message) {
        return new ResponseDTO(RequestResultEnum.FAIL.getCode(), message, null);
    }

    public File renderFile(File file){
        return file;
    }

    public ResponseDTO render(int code) {
        return new ResponseDTO(code, RequestResultEnum.OK.getMessage(), null);
    }

    public ResponseDTO render(int code, String message) {
        return new ResponseDTO(code, message, null);
    }

    public ResponseDTO render(int code, Object data) {
        return new ResponseDTO(code, RequestResultEnum.OK.getMessage(), data);
    }

    public ResponseDTO render(int code, String message, Object data) {
        return new ResponseDTO(code, message, data);
    }

    public HttpServletRequest getRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes.getRequest();
    }

    public HttpServletResponse getResponse() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes.getResponse();
    }
}
