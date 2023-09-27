package dev.cobblesword.controllers;

import dev.cobblesword.repository.PlayerDataRepository;
import dev.cobblesword.types.PlayerData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@RestController
public class HomeViewController
{
    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private PlayerDataRepository playerDataRepository;

//    @GetMapping("/")
//    public String home()
//    {
//        Context context = new Context();
//        return templateEngine.process("home", context);
//    }
}
