package com.amiracademy.amir_academy.services;

import com.amiracademy.amir_academy.entities.Video;
import com.amiracademy.amir_academy.repositories.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.View;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class VideoService {

    @Value(value = "${upload.path}")
    private String uploadPath;

    @Autowired
    private VideoRepository repository;


    public Video uploadVideo(String title, String description, MultipartFile file) throws IOException {

        // Check if the upload directory exists, if not create it
        Path uploadDirectory = Paths.get(uploadPath).toAbsolutePath().normalize();
        if(!Files.exists(uploadDirectory)){
            Files.createDirectories(uploadDirectory);
        }

        // Generate unique filename
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        String fileExtension = getFileExtension(originalFilename);
        if(!isValidVideoFormat(fileExtension)){
            throw new IllegalArgumentException("Invalid video format "+ fileExtension);
        }

        String filename = System.currentTimeMillis() + "-" + originalFilename;

        // Copy the file to the upload directory
        Path targetLocation = uploadDirectory.resolve(filename);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        // Create Video entity
        Video video = new Video();
        video.setTitle(title);
        video.setDescription(description);
        video.setSize(file.getSize());
        video.setContentType(file.getContentType());
        video.setVideoUrl(filename);
        video.setCreated(new Date());
        video.setModified(new Date());

        return repository.save(video);
    }


    public Resource loadVideoAsResource(String filename) throws MalformedURLException {
        Path filePath = Paths.get(uploadPath).resolve(filename).normalize();
        Resource resource = new UrlResource(filePath.toUri());
        if(resource.exists()){
            return resource;
        }else{
            throw new RuntimeException("Video file not found " + filename);
        }
    }

    public List<Video> getAllVideos(){
        return repository.findAll();
    }

    public List<Video> search(String keyword){
        return repository.findFirstByTitleContainsOrDescriptionContains(keyword);
    }
    public Video getVideoById(Long id){
        Optional<Video> video = repository.findById(id);
        return video.isPresent() ? video.get() : null;
    }
    public void deleteAllVideos(){
        FileSystemUtils.deleteRecursively(Paths.get(uploadPath).toFile());
    }

    public void init(){
        try {
            Files.createDirectories(Paths.get(uploadPath));
        }catch (IOException exception){
            throw new RuntimeException("Could not initialize upload folder: " + exception.getMessage());
        }
    }


    private boolean isValidVideoFormat(String contentType){
        // List of supported video formats
        List<String> supportedFormats = Arrays.asList(
                "video/mp4",
                "video/quicktime",
                "video/x-msvideo", // AVI
                "video/x-flv"    // FLV
                // Add more formats here as needed
        );
        return contentType != null && supportedFormats.contains(contentType);
    }

    private String getFileExtension(String filename){
        return filename.substring(filename.lastIndexOf(".") + 1);
    }
}
