package io.tpi.currency.exception;

public class ServiceException extends BaseException {
    public ServiceException(String errorCode, Object... args) {
        super(errorCode, args);
    }
}
