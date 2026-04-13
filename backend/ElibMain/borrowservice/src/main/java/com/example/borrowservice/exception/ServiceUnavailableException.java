package com.example.borrowservice.exception;

public class ServiceUnavailableException extends RuntimeException{
    public ServiceUnavailableException(String msg){super(msg);}
}
