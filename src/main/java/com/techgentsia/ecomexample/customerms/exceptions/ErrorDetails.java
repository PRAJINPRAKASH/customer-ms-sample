package com.techgentsia.ecomexample.customerms.exceptions;

import lombok.Data;

import java.util.Date;

@Data
public class ErrorDetails {
    private Date timestamp;
    private String message;
    private Object details;

    public ErrorDetails(Date timestamp, String message, Object details) {
        super();
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
    }
}
