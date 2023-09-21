package dev.cobblesword.libraries.common.task;

public class Sync
{
    public static BTask get()
    {
        return new BTask().sync();
    }
}
