package com.lion328.hydra;

import com.google.gson.Gson;

import java.io.InputStreamReader;
import java.util.Map;

public class Language
{

    private static Map table;

    public static String get(String key)
    {
        if (table == null)
        {
            table = new Gson().fromJson(new InputStreamReader(Language.class.getResourceAsStream("/com/lion328/hydra/resources/lang.json")), Map.class);
        }

        String ret = (String) table.get(key);

        if (ret != null)
        {
            return ret;
        }

        return key;
    }
}
