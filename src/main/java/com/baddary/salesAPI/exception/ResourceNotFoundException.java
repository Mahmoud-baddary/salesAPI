package com.baddary.salesAPI.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String resourceName, Object resource) {
        super(resourceName + " : " + resource + " not found");
    }
    
}
