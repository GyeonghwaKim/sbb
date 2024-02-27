package com.mysite.sbb;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//예외가 발생하면 클라이언트에게 보여질 메세지
@ResponseStatus(value= HttpStatus.NOT_FOUND,reason="entity not found")
public class DataNotFoundException extends RuntimeException{
    private static final long serialVersionUID=1L;

    public DataNotFoundException(String message) {
        super(message);
    }
}
