package org.noname.onestep.chitchat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String LoginPage() {

        return "view/login";
    }



}
