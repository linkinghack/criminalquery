package com.linkinghack.criminalquerybase.controller;

import com.linkinghack.criminalquerybase.service.FileService;
import com.linkinghack.criminalquerymodel.transfer_model.UniversalResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@Controller
@RequestMapping(value = "/oss", method = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.PATCH})
public class FileController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private FileService service;

    @Autowired
    public FileController(FileService service) {
        this.service = service;
    }

    @PostMapping("/file")
    @ResponseBody
    public UniversalResponse uploadFile(@RequestParam("file") MultipartFile file) {
        String fn = "<POST>[/oss/file]";
        logger.info("@Request{} {}", fn, file.getOriginalFilename());
        UniversalResponse response = service.uploadFile(file);
        logger.info("@Response{} {}", fn, response);
        return response;
    }

    @DeleteMapping("/file/{fileID}")
    @ResponseBody
    public UniversalResponse deleteFile(@PathVariable("fileID") String fileID) {
        String fn = "<DELETE>[/oss/file/{fileID}]";
        logger.info("@Request{} fileID={}", fn, fileID);
        UniversalResponse response = service.deleteFile(fileID);
        logger.info("@Response{} response={}", fn, response);
        return response;
    }

}
