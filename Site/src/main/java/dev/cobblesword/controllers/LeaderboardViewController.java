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
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class LeaderboardViewController {

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private PlayerDataRepository playerDataRepository;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;

    @Autowired
    private ServletContext servletContext;

    @GetMapping("/")
    public String leaderboard(@RequestParam(name = "page", defaultValue = "0") int page,
                              @RequestParam(name = "size", defaultValue = "10") int size)
    {
        Pageable pageable = PageRequest.of(page, size);

        WebContext context = new WebContext(request, response, servletContext);
        Page<PlayerData> playersPage = playerDataRepository.findAllByOrderByTotalWinsDesc(pageable);

        int rankStart = page * size; // calculate the starting rank for this page
        int rank = rankStart + 1;
        for (PlayerData player : playersPage.getContent())
        {
            player.setCurrentPlacement(rank++);
        }

        context.setVariable("playersPage", playersPage);

        return templateEngine.process("leaderboard", context);
    }
}