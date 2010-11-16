package com.mattinsler.guiceymongo;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: Sep 26, 2010
 * Time: 8:26:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class GuiceyMongoException extends RuntimeException {
    public GuiceyMongoException() {
    }
    public GuiceyMongoException(String message) {
        super(message);
    }
    public GuiceyMongoException(String message, Throwable throwable) {
        super(message, throwable);
    }
    public GuiceyMongoException(Throwable throwable) {
        super(throwable);
    }
}
