package org.noname.onestep.chitchat.controller;

import org.noname.onestep.chitchat.domain.entity.User;
import org.noname.onestep.chitchat.domain.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/create")
    public String getCreateUser(Model model) {

        model.addAttribute("user",new User());
        return "/view/user";
    }

    @PostMapping("/create")
    public String postCreateUser(@ModelAttribute User user, Model model) {

        if(user.getId() == 0 &&
                userService.userExits(user.getEmailAddress())){
            model.addAttribute("message",
                    "すでに登録されているメールアドレスです。");
            model.addAttribute("user",new User());
            return "/view/user";
        }

        userService.save(user);
        model.addAttribute("message",
                "ユーザー登録が完了しました。");

        return "/view/login";
    }

    @GetMapping("/update")
    public String getUpdateUser(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        model.addAttribute("user",
                this.userService.findUser(authentication.getName()));

        return "/view/user";

    }
}
