package com.linkinghack.criminalquery.controller;

import org.springframework.web.bind.annotation.*;

@RestController
public class RootController {

    @GetMapping("/")
    public String index(){
        return "Hello, SpringBoot!";
    }

}
