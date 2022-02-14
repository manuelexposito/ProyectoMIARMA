package com.salesianostriana.miarma.services;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {

    void init();

    String store(MultipartFile file);

    Stream<Path> loadAll();

    Path load(String filename);

    Resource loadAsResource(String filename);

    void deleteFile(String filename);

    void deleteAll();

    BufferedImage simpleResizeImage(BufferedImage originalImage, int targetWidth) throws Exception;



}
