package dev.cobblesword.ctf.game.stats;

import dev.cobblesword.ctf.game.GameState;

import java.util.ArrayList;
import java.util.List;

public class GameStats
{
    private int id;
    private String mapName;
    private long startTimestamp = Long.MIN_VALUE;
    private long endTimestamp = Long.MIN_VALUE;
    private GameState gameState;

    private List<PlayerGameStats> players = new ArrayList<>();
}
