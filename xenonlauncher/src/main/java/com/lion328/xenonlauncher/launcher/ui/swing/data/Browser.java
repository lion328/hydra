package com.lion328.xenonlauncher.launcher.ui.swing.data;

import com.google.gson.annotations.SerializedName;

import java.net.URL;

public class Browser extends Component
{

    @SerializedName("url")
    private URL url;

    public Browser(String type, int width, int height, int x, int y, int color, URL url)
    {
        this("browser", type, width, height, x, y, color, url);
    }

    public Browser(String id, String type, int width, int height, int x, int y, int color, URL url)
    {
        super(id, type, width, height, x, y, color);

        this.url = url;
    }

    public URL getURL()
    {
        return url;
    }
}
