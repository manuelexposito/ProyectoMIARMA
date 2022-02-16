package com.salesianostriana.miarma.errors.exceptions.following;

public class RequestAlreadySentException extends RuntimeException{

    public RequestAlreadySentException(String message){
        super(message);
    }

}
