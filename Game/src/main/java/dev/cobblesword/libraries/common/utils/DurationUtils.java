package dev.cobblesword.libraries.common.utils;

public class DurationUtils
{
    public static long elapsedMillis(long sinceTime)
    {
        return System.currentTimeMillis() - sinceTime;
    }
}
