package com.baddary.salesAPI.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String resourceName, Long resourceId) {
        super(resourceName + " with ID " + resourceId + " not found");
    }
    
}
