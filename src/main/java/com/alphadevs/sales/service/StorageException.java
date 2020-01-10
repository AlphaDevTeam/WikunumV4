package com.alphadevs.sales.service;

public class StorageException extends RuntimeException {

    public StorageException(String errorMessage, Exception e) {
        super(errorMessage != null ? errorMessage + " - " + e : "Initialization Exception!" + e);
    }


}
