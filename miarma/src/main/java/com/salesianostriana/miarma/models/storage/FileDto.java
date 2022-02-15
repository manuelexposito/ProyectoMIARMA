package com.salesianostriana.miarma.models.storage;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FileDto {

    private String name;
    private String uri;
    private String type;
    private long size;

}
