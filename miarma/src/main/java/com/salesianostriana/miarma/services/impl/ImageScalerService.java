package com.salesianostriana.miarma.services.impl;

import com.salesianostriana.miarma.services.ImageScaler;
import io.github.techgnious.IVCompressor;
import io.github.techgnious.dto.IVSize;
import io.github.techgnious.dto.VideoFormats;
import io.github.techgnious.exception.VideoException;
import org.imgscalr.Scalr;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.IOException;

@Service
public class ImageScalerService implements ImageScaler {

    @Override
    public BufferedImage simpleResizeImage(BufferedImage originalImage, int targetWidth) throws Exception {

        return Scalr.resize(originalImage, targetWidth);
    }

    @Override
    public byte[] resizeVideo(MultipartFile file, int width, int height, String mimeFormat) throws IOException, VideoException {
        IVCompressor compressor = new IVCompressor();
        IVSize customRes = new IVSize();

        customRes.setWidth(width);
        customRes.setHeight(height);

        return compressor.reduceVideoSizeWithCustomRes(file.getBytes(), VideoFormats.valueOf(mimeFormat.toUpperCase()), customRes);


    }

}
