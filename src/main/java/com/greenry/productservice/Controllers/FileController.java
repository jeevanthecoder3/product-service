package com.greenry.productservice.Controllers;

import com.greenry.productservice.PayLoad.FileResponse;
import com.greenry.productservice.Service.FileService;
import com.greenry.productservice.Service.UserService;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/{userName}/file")
public class FileController {

    @Autowired
    private FileService fileService;

    @Autowired
    private UserService userService;

    @Value("${project.image}")
    private String path;

    public String getPath() {
        return path;
    }

    @PostMapping("/{pTitle}/upload")
    public ResponseEntity<FileResponse> fileUpload(
            @RequestParam("image")MultipartFile image,
            @PathVariable("userName")String userName,
            @PathVariable("pTitle")String pTitle
            ) {
        if(this.userService.checkValidity(userName)==true) {
            try {

                String fileName = this.fileService.uploadImage(path, image,userName,pTitle);

                return new ResponseEntity<>(new FileResponse(fileName, "Image is successfully uploaded!!!"), HttpStatus.OK);
            } catch (Exception ex) {
                return new ResponseEntity<>(new FileResponse(null, "Image is not uploaded!!!"), HttpStatus.INTERNAL_SERVER_ERROR);

            }
        }
        else
            return new ResponseEntity<>(new FileResponse(null, "User Not LoggedIn!!!"), HttpStatus.UNAUTHORIZED);

    }

    //method to serve files
    @GetMapping(value="/images/{imageName}")
    public ResponseEntity<Void> downloadImage(
            @PathVariable("imageName") String imageName,
            @PathVariable("userName")String userName,
            HttpServletResponse response
    )throws IOException{
if(this.userService.checkValidity(userName)==true) {
    InputStream resource = this.fileService.getResource(path, imageName);
    response.setContentType(MediaType.IMAGE_PNG_VALUE);
    StreamUtils.copy(resource, response.getOutputStream());
    return ResponseEntity.status(HttpStatus.OK).build();
}else{
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
}
    }


}
