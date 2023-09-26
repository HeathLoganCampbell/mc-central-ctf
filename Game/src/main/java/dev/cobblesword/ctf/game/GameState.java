package dev.cobblesword.ctf.game;

import lombok.Getter;

public enum GameState
{
    SETTING_UP(-1),
    WAITING_FOR_PLAYERS(-1),
    COUNTDOWN(90),
    PREPARE_GAME(-1),
    IN_PROGRESS(60 * 15),
    CELEBRATE(15),
    ENDED(-1);

    @Getter
    private int seconds;

    GameState(int seconds)
    {
        this.seconds = seconds;
    }

    public boolean WaitingToStart()
    {
        return this == SETTING_UP || this == WAITING_FOR_PLAYERS || this == COUNTDOWN;
    }

    public boolean InProgress()
    {
        return this == PREPARE_GAME || this == IN_PROGRESS || this == CELEBRATE;
    }
}
