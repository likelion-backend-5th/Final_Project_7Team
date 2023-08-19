package com.likelion.catdogpia.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String home() {
        return "page/index.html";
    }

//    테스트용
    @GetMapping("/test")
    public String test() {
        return "page/content-test.html";
    }

}
