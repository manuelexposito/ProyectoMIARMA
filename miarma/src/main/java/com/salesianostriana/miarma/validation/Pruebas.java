package com.salesianostriana.miarma.validation;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Pruebas {

    public static void main(String[] args) {

        String url = "http://localhost:8080/download/gato.jpg";

        String name = Arrays.stream(url.split("/")).filter(s -> s.contains(".")).collect(Collectors.toList()).get(0);

        System.out.println(name);

    }


}
