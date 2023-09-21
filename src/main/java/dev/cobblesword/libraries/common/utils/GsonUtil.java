package dev.cobblesword.libraries.common.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonUtil
{
    private static Gson g = new Gson();
    private static Gson prettyGson;

    public static Gson getGson ()
    {
        if (g == null)
            g = new GsonBuilder()
                    .disableHtmlEscaping()
                    .create();
        return g;
    }

    public static Gson getPrettyGson ()
    {
        if (prettyGson == null)
            prettyGson = new GsonBuilder()
                    .setPrettyPrinting()
                    .disableHtmlEscaping()
                    .create();
        return prettyGson;
    }

    public static Gson getSimplePrettyGson ()
    {
        if (prettyGson == null)
            prettyGson = new GsonBuilder()
                    .setPrettyPrinting()
                    .disableHtmlEscaping()
                    .create();
        return prettyGson;
    }
}