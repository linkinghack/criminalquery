package com.linkinghack.criminalquerybase.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {

    @GetMapping("/index")
    public String index() {
        return "hello Spring";
    }
}
