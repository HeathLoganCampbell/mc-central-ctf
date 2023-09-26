package dev.cobblesword.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RequestMapping("/v1/playerdata")
@RestController()
public class PlayerDataController
{
    @GetMapping()
    public String getPlayerData(@RequestParam(required = false) UUID uuid, @RequestParam(required = false) String name)
    {
        return "Greetings from Spring Boot!" + name;
    }
}
