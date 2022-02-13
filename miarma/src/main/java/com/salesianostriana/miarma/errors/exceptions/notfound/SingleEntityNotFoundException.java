package com.salesianostriana.miarma.errors.exceptions.notfound;

public class SingleEntityNotFoundException extends EntityNotFoundException{

    public SingleEntityNotFoundException(Class clase, Long id) {
        super(String.format("No se ha podido encontrar el elemento del tipo %s con el ID %s",clase, id));
    }
}