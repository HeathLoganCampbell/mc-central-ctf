package dev.cobblesword.libraries.common.task;

public class Async
{
    public static BTask get()
    {
        return new BTask().async();
    }
}
