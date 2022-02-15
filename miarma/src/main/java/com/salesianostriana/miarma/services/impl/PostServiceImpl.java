package com.salesianostriana.miarma.services.impl;

import com.salesianostriana.miarma.errors.exceptions.storage.WrongFormatException;
import com.salesianostriana.miarma.models.post.Post;
import com.salesianostriana.miarma.models.post.dto.CreatePostDto;
import com.salesianostriana.miarma.models.user.UserEntity;
import com.salesianostriana.miarma.repositories.PostRepository;
import com.salesianostriana.miarma.services.PostService;
import com.salesianostriana.miarma.services.StorageService;
import io.github.techgnious.IVCompressor;
import io.github.techgnious.dto.IVSize;
import io.github.techgnious.dto.VideoFormats;
import io.github.techgnious.exception.VideoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;


@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final StorageService storageService;

    @Override
    public Post save(CreatePostDto post, MultipartFile file, UserEntity currentUser) throws Exception {

        //TODO : Arreglar la reescalada de vídeos
        String filename = storageService.store(file);
        String ext = StringUtils.getFilenameExtension(filename);
        String fileResized =  filename.replace("."+ext,"") + "-resize." + ext;

        byte[] videoBytes = new byte[0];
        BufferedImage original = ImageIO.read(file.getInputStream());
        BufferedImage resized;

        List<String> validImgFormat = List.of("png", "jpg", "jpeg");
        List<String> validVidFormat = List.of("avi", "mp4");

        if (validVidFormat.contains(ext)) {

           videoBytes = storageService.resizeVideo(file, 400, 300, ext);

           resized = ImageIO.read(new ByteArrayInputStream(videoBytes));

           ImageIO.write(resized, ext, Files.newOutputStream(storageService.load(fileResized)));
           ImageIO.write(original, ext, Files.newOutputStream(storageService.load(filename)));

        } else if (validImgFormat.contains(ext)) {

            resized = storageService.simpleResizeImage(original, 128);

            ImageIO.write(resized, ext, Files.newOutputStream(storageService.load(fileResized)));
            ImageIO.write(original, ext, Files.newOutputStream(storageService.load(filename)));

        } else {
            throw new WrongFormatException("Hubo un error. El formato no es válido.");
        }


        String originalUri = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/download/")
                .path(filename)
                .toUriString();

        String resizedUri = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/download/")
                .path(fileResized)
                .toUriString();

        Post newPost = Post.builder()
                .file(originalUri)
                .resizedFile(resizedUri)
                .message(post.getMessage())
                .owner(currentUser)
                .isNotVisible(currentUser.isPrivate())
                .build();

        currentUser.setUserToPost(newPost);

        return postRepository.save(newPost);

    }


}
