package com.sessionknip.socialnet.web.controller;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @PostMapping("/register")
    @ResponseBody
    public String register() {
        return "register";
    }


    @PostMapping("/auth_success")
    @ResponseBody
    public String authSuccess() {
//        HashMap<String, Object> result = new HashMap<>();
//        result.put("", )
        return null;
    }

    @PostMapping("/auth_fail")
    @ResponseBody
    public String authFail() {
        return null;
    }


}
