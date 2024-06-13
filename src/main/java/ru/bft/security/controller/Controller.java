package ru.bft.security.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @GetMapping("/hello")
    public String getHello() {
        return "hello";
    }

    @GetMapping("/myInfo")
    public String getMyInfo() {
        return "There will be insurer info";
    }

    @GetMapping("/contact")
    public String getContact() {
        return "There will be contact info";
    }
}
