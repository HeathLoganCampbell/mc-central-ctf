package dev.cobblesword.controllers;

import dev.cobblesword.repository.PlayerDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@RestController
public class ProfileViewController
{
    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private PlayerDataRepository playerDataRepository;

//    @GetMapping("/profile")
//    public String home()
//    {
//        Context context = new Context();
//        return templateEngine.process("profile", context);
//    }
}
