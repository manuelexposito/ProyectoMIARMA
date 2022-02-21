package com.salesianostriana.miarma.services;

import io.github.techgnious.exception.VideoException;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.IOException;

public interface ImageScaler {

    BufferedImage simpleResizeImage(BufferedImage originalImage, int targetWidth) throws Exception;

    byte[] resizeVideo(MultipartFile file, int width, int height, String mimeFormat) throws IOException, VideoException;


}
