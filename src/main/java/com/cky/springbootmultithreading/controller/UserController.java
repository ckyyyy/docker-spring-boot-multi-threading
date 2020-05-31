package com.cky.springbootmultithreading.controller;

import com.cky.springbootmultithreading.entity.User;
import com.cky.springbootmultithreading.service.UserService;
import net.sf.jasperreports.engine.JRException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    Logger logger = LoggerFactory.getLogger(UserService.class);


    @PostMapping(value = "/users", consumes = (MediaType.MULTIPART_FORM_DATA_VALUE), produces = "application/json")
    public ResponseEntity saveUser(@RequestParam(value = "files") MultipartFile[] files) throws Exception {
//        logger.info("post /users file {}", files.length);
        for (MultipartFile file : files) {
            userService.saveUser(file);
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping(value = "/users", produces = "application/json")
    public CompletableFuture<ResponseEntity> findAllUsers() {
        return userService.findAllUsers().thenApply(ResponseEntity::ok);
    }

    @GetMapping(value = "/getUsersByThread", produces = "application/json")
    public ResponseEntity getUsers() {
        CompletableFuture<List<User>> users1 = userService.findAllUsers();
        CompletableFuture<List<User>> users2 = userService.findAllUsers();
        CompletableFuture<List<User>> users3 = userService.findAllUsers();
        CompletableFuture.allOf(users1, users2, users3).join();
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/getUsersReportPdf")
    public HttpEntity<byte[]> generateReport() throws FileNotFoundException, JRException {

        try
        {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment;filename=\"usersReport.pdf\"");
            headers.add("Content-Type", "application/pdf");

            byte[] report = userService.exportUsersReport();
            return new HttpEntity<>(report, headers);
        }
        catch (Exception e)
        {
            throw e;
        }
//        return userService.exportUsersReport(format);
    }
}
