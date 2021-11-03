package org.noname.onestep.chitchat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/chatspace")
public class ChatSpaceController {

    @GetMapping("/top")
    public String getChatSpaceTop() {

        return "view/chatspace-top";
    }
}
