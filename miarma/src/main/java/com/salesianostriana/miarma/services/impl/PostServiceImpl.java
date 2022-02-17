package com.salesianostriana.miarma.services.impl;

import com.salesianostriana.miarma.errors.exceptions.entitynotfound.EntityNotFoundException;
import com.salesianostriana.miarma.errors.exceptions.following.PrivateProfileException;
import com.salesianostriana.miarma.errors.exceptions.storage.FileNotFoundException;
import com.salesianostriana.miarma.errors.exceptions.storage.WrongFormatException;
import com.salesianostriana.miarma.models.follow.Follow;
import com.salesianostriana.miarma.models.post.Post;
import com.salesianostriana.miarma.models.post.dto.CreatePostDto;
import com.salesianostriana.miarma.models.user.UserEntity;
import com.salesianostriana.miarma.repositories.PostRepository;
import com.salesianostriana.miarma.repositories.UserEntityRepository;
import com.salesianostriana.miarma.services.PostService;
import com.salesianostriana.miarma.services.StorageService;
import com.salesianostriana.miarma.utils.mediatype.MediaTypeUrlResource;
import io.github.techgnious.IVCompressor;
import io.github.techgnious.dto.IVSize;
import io.github.techgnious.dto.VideoFormats;
import io.github.techgnious.exception.VideoException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final StorageService storageService;
    private final UserEntityRepository userRepository;

    @Override
    public Post save(CreatePostDto post, MultipartFile file, UserEntity currentUser) throws Exception {

        //TODO : Arreglar la reescalada de vídeos
        String filename = storageService.store(file);
        String ext = StringUtils.getFilenameExtension(filename);
        String fileResized = filename.replace("." + ext, "") + "-resize." + ext;

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

        return postRepository.save(newPost);

    }

    @Override
    public Post editPost(UUID id, MultipartFile file, UserEntity currentUser, CreatePostDto editPost) throws Exception {
        String filename;
        String ext;
        String fileResized;
        BufferedImage original;
        BufferedImage resized;
        String originalUri;
        String resizedUri;

        Optional<Post> post = postRepository.findById(id);

        if(post.isPresent()){

            Post foundPost = post.get();
            originalUri = foundPost.getFile();
            resizedUri = foundPost.getResizedFile();

            foundPost = Post.builder()
                    .id(id)
                    .message(editPost.getMessage())
                    .owner(currentUser)
                    .file(originalUri)
                    .resizedFile(resizedUri)
                    .build();

            if(!file.isEmpty()){
                storageService.deleteFile(post.get().getFile());

                filename = storageService.store(file);
                ext = StringUtils.getFilenameExtension(filename);
                fileResized = filename.replace("." + ext, "") + "-resize." + ext;
                original = ImageIO.read(file.getInputStream());

                resized = storageService.simpleResizeImage(original, 128);

                ImageIO.write(resized, ext, Files.newOutputStream(storageService.load(fileResized)));
                ImageIO.write(original, ext, Files.newOutputStream(storageService.load(filename)));

                originalUri = ServletUriComponentsBuilder
                        .fromCurrentContextPath()
                        .path("/download/")
                        .path(filename)
                        .toUriString();

                resizedUri = ServletUriComponentsBuilder
                        .fromCurrentContextPath()
                        .path("/download/")
                        .path(fileResized)
                        .toUriString();

                foundPost.setFile(originalUri);
                foundPost.setResizedFile(resizedUri);
            }

            return postRepository.save(foundPost);
        } throw new EntityNotFoundException("No se encontró la publicación");


    }

    @Override
    public List<Post> getPublicPosts() {

        return postRepository.findAll().stream()
                .filter(post -> !post.isNotVisible())
                .collect(Collectors.toList());

    }

    @Override
    public List<Post> getPostsByUsername(String username, UserEntity currentUser) {

        Optional<UserEntity> user = userRepository.findFirstByUsername(username);

        if(user.isPresent()){

            UserEntity foundUser = user.get();
            Optional<Follow> optFollow = foundUser.getRequests().stream()
                    .filter(f -> f.getUserFollowing().equals(currentUser)).findFirst();

            if(foundUser.isPrivate() && optFollow.isPresent() || !foundUser.isPrivate())
            {
                return postRepository.findAllPostByOwner(foundUser.getId());

            } else throw new PrivateProfileException("No puede ver las publicaciones de este perfil porque es privado.");




        } else throw new EntityNotFoundException("No se encontró al usuario");


    }

    @Override
    public List<Post> getMyPosts(UserEntity currentUser) {
        return postRepository.findAllPostByOwner(currentUser.getId());
    }

    @Override
    public Post getOnePost(UUID id, UserEntity currentUser) {

        Optional<Post> post = postRepository.findById(id);

        if(post.isPresent()){

            Post foundPost = post.get();
            UserEntity owner = foundPost.getOwner();
            Optional <Follow> relationship = owner.getRequests().stream()
                    .filter(follow -> follow.getUserFollowing() == currentUser).findFirst();

            //if(foundPost.isNotVisible() && relationship.isEmpty() || foundPost.isNotVisible() && !relationship.get().isAccepted()) throw new PrivateProfileException("No puedes ver este post porque pertenece a un perfil privado.");

            if(foundPost.isNotVisible()){

                if (relationship.isEmpty() || !relationship.get().isAccepted()) throw new PrivateProfileException("No puedes ver este post porque pertenece a un perfil privado.");

                if (relationship.get().isAccepted()) return foundPost;

            } else{

                return foundPost;
            }



        } else throw new EntityNotFoundException("No se encontró la publicación");

        return null;
    }



    @Override
    public void delete(UUID id) throws IOException {

        try {
            Optional<Post> post = postRepository.findById(id);
            if (post.isPresent()) {

                storageService.deleteFile(post.get().getFile());
                postRepository.delete(post.get());
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }


}
