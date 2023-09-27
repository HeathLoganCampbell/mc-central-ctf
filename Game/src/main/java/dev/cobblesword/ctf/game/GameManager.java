package dev.cobblesword.ctf.game;

import dev.cobblesword.ctf.CaptureTheFlagPlugin;
import dev.cobblesword.ctf.game.commands.GameCommands;
import lombok.Getter;

public class GameManager
{
    @Getter
    private Game game;

    public GameManager()
    {
        this.game = new Game(CaptureTheFlagPlugin.getInstance());

        CaptureTheFlagPlugin.getInstance().getCommandFramework().registerCommands(new GameCommands());
    }
}
