package com.baddary.salesAPI.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String resourceName, Object resourceValue) {
        super(resourceName + " " + resourceValue + " not found");
    }
    
}
