package com.techgentsia.ecomexample.customerms.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends Exception {

    private static final long serialVersionUID = 1L;
    private String id;

    public ResourceNotFoundException(String message, String id) {
        super(message);
        this.id = id;
    }
    public ResourceNotFoundException(String message) {
        this(message,"");
    }
    public String getId() {
        return id;
    }
}
