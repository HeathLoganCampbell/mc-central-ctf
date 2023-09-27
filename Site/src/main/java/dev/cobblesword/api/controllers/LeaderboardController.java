package dev.cobblesword.api.controllers;

import dev.cobblesword.repository.PlayerDataRepository;
import dev.cobblesword.types.PlayerData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RequestMapping("/api/v1/leaderboard")
@RestController()
public class LeaderboardController
{
    @Autowired
    private PlayerDataRepository playerDataRepository;

    @GetMapping()
    public Page<PlayerData> getPlayers(@RequestParam(required = false) String name,
                                       @RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "10") int size)
    {
        Pageable pageable = PageRequest.of(page, size, Sort.by("totalWins").descending());

        if (name != null) {
            System.out.println("fetch by name");
            return playerDataRepository.findByNameContainingIgnoreCase(name, pageable);
        }

        System.out.println("fetch all");
        return playerDataRepository.findAll(pageable);
    }
}
