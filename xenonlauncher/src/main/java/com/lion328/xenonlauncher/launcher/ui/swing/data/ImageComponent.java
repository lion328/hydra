package com.lion328.xenonlauncher.launcher.ui.swing.data;

import com.google.gson.annotations.SerializedName;

import java.net.URL;

public class ImageComponent extends Component
{

    @SerializedName("imageURL")
    private URL imageURL;

    public ImageComponent(String id, String type, int width, int height, int x, int y, int color, URL imageURL)
    {
        super(id, type, width, height, x, y, color);

        this.imageURL = imageURL;
    }

    public URL getImageURL()
    {
        return imageURL;
    }
}
