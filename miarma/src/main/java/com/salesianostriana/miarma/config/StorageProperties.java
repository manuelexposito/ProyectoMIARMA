package com.salesianostriana.miarma.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "storage")
@Getter
@Setter
public class StorageProperties {

    private String location;

}
