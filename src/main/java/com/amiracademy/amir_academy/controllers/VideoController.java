package com.amiracademy.amir_academy.controllers;

import com.amiracademy.amir_academy.entities.Video;
import com.amiracademy.amir_academy.services.VideoService;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("video")
@Validated
@Tag(name = "Video", description = "All methods for video")
public class VideoController {

    @Autowired
   private VideoService videoService;

    @PostMapping("/upload")
    @ApiOperation(value = "upload a video", notes = "Uploads a video with title, description, and the actual video file")
    public ResponseEntity<Video> uploadVideo(
            @ApiParam(value = "Title of the video", required = true) @RequestParam("title") @NotBlank(message = "Video title is required") String title,
            @ApiParam(value = "Description of the video") @RequestParam("description") String description,
            @ApiParam(value = "Video file", required = true) @RequestParam("file") MultipartFile file) throws IOException
    {
        Video uploadVideo = videoService.uploadVideo(title, description, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(uploadVideo);
    }

}
