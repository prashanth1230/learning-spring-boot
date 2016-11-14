package com.pras.learningspringboot;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by a487037 on 11/14/2016.
 */
@Controller
public class HomeController {

    private static final String BASE_PATH = "/images";
    private static final String FILENAME = "{filename:.+}";

    private final ImageService imageService;

    @Autowired
    public HomeController(ImageService imageService) {
        this.imageService = imageService;
    }

    @RequestMapping(method = RequestMethod.GET, value = BASE_PATH + "/" + FILENAME + "/raw")
    @ResponseBody
    public ResponseEntity<?> oneRawImage(@PathVariable String filename) {
        Resource file = imageService.findOneImage(filename);
        try {
            return ResponseEntity.ok()
                    .contentLength(file.contentLength())
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(new InputStreamResource(file.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest()
                    .body("Could not find the resource!");
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = BASE_PATH)
    @ResponseBody
    public ResponseEntity<?> createFile(@RequestParam("file") MultipartFile file, HttpServletRequest servletRequest) {
        try {
            imageService.createImage(file);
            final URI locationUrl = new URI(servletRequest.getRequestURI().toString() + "/")
                    .resolve(file.getOriginalFilename() + "/raw");
            return ResponseEntity.created(locationUrl)
                    .body("Successfully uploaded " + file.getOriginalFilename());
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload " + file.getOriginalFilename() + " => " + e.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, value = BASE_PATH + "/" + FILENAME)
    @ResponseBody
    public ResponseEntity<?> deleteFile(@PathVariable String filename) {
        try {
            imageService.deleteImage(filename);
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body("Successfully Deleted " + filename);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete " + filename + " => " + e.getMessage());
        }
    }
}
