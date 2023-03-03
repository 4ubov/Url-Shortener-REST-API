package com.chubov.urlshortener.util;

public class ErrorResponse {
    //  Custom Error Response class

    //  Fields
    private int statusCode;
    private String message;
    private long timestamp;

    public ErrorResponse(int statusCode, String message, long timestamp) {
        this.statusCode = statusCode;
        this.message = message;
        this.timestamp = timestamp;
    }

    //  Getters and Setters
    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
