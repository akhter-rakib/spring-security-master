package com.rakib.springsecuritybasic.controller;

import org.springframework.web.bind.annotation.*;

@RestController
public class WelcomeController {

    @GetMapping("welcome")
    public String welCome() {
        return "WelCome Bro!";
    }
}
