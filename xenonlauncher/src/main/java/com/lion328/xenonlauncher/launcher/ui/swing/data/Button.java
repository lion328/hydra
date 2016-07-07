package com.lion328.xenonlauncher.launcher.ui.swing.data;

import com.google.gson.annotations.SerializedName;
import com.lion328.xenonlauncher.launcher.ui.swing.data.type.ComponentTypeAdapter;

import java.net.URL;

public class Button extends ImageComponent
{

    public static final String TYPE = "button";

    static
    {
        ComponentTypeAdapter.registerComponent(TYPE, Button.class);
    }

    @SerializedName("hoverURL")
    private URL hoverURL;

    public Button(String id, int width, int height, int x, int y, int color, URL imageURL, URL hoverURL)
    {
        super(id, TYPE, width, height, x, y, color, imageURL);
    }

    public Button(String id, String type, int width, int height, int x, int y, int color, URL imageURL, URL hoverURL)
    {
        super(id, type, width, height, x, y, color, imageURL);
    }

    public URL getImageHoverURL()
    {
        return hoverURL;
    }
}
