package com.amiracademy.amir_academy.controllers;

import com.amiracademy.amir_academy.entities.Video;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("video")
@Tag(name = "Video", description = "All methods for video")
public class VideoController {

    @PostMapping("/upload")
    public Video uploadVideo(Video video){
        return new Video() ;
    }
}
