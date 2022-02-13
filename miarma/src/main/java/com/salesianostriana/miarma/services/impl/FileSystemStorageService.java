package com.salesianostriana.miarma.services.impl;

import com.salesianostriana.miarma.config.StorageProperties;
import com.salesianostriana.miarma.errors.exceptions.storage.FileNotFoundException;
import com.salesianostriana.miarma.errors.exceptions.storage.StorageException;
import com.salesianostriana.miarma.services.StorageService;
import com.salesianostriana.miarma.utils.mediatype.MediaTypeUrlResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

@Service
public class FileSystemStorageService implements StorageService {

    private final Path rootLocation;

    //TODO: Internacionalizar excepciones



    @Autowired
    public FileSystemStorageService(StorageProperties properties){
        this.rootLocation = Paths.get(properties.getLocation());
    }

    @PostConstruct
    @Override
    public void init() {

        try {
            Files.createDirectories(rootLocation);
        } catch (IOException exception){
            throw new StorageException("No se pudo inicializar el lugar de almacenamiento", exception);
        }

    }

    @Override
    public String store(MultipartFile file) {

        String filename = StringUtils.cleanPath(file.getOriginalFilename());

        String newFilename = "";

        try{
            if(file.isEmpty()) throw new StorageException("El fichero subido está vacío");

            newFilename = filename;

            while(Files.exists(rootLocation.resolve(newFilename))){
                String ext = StringUtils.getFilenameExtension(newFilename);
                String name = newFilename.replace("."+ext,"");
                String suffix = Long.toString(System.currentTimeMillis());
                suffix = suffix.substring(suffix.length()-6);

                newFilename = name + "_" + suffix + "." + ext;
            }
            try(InputStream inputStream = file.getInputStream()){
                Files.copy(inputStream, rootLocation.resolve(newFilename), StandardCopyOption.REPLACE_EXISTING);
            }


        } catch (IOException exception){
            throw new StorageException("Error en el almacenamiento del fichero: " + newFilename, exception);
        }
        return newFilename;

    }

    @Override
    public Stream<Path> loadAll() {
        try{
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(this.rootLocation::relativize);
        } catch(IOException exception){
            throw new StorageException("Error al leer los ficheros almacenados", exception);
        }
    }

    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {

        try{
            Path file = load(filename);

            MediaTypeUrlResource resource = new MediaTypeUrlResource(file.toUri());
            if(resource.exists() || resource.isReadable()){
                return resource;
            }
            else{
                throw new FileNotFoundException("No pudo leerse el fichero "+filename);
            }
        } catch (MalformedURLException exception){
            throw new FileNotFoundException("No pudo leerse el fichero "+ filename);
        }


    }

    //TODO: eliminar ficheros
    @Override
    public void deleteFile(String filename) {


    }

    @Override
    public void deleteAll() {

    }
}