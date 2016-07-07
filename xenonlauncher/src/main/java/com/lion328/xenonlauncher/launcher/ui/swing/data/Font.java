package com.lion328.xenonlauncher.launcher.ui.swing.data;

import com.google.gson.annotations.SerializedName;

public class Font
{

    public static final Font DEFAULT = new Font();

    @SerializedName("family")
    private String family = java.awt.Font.SANS_SERIF;
    @SerializedName("size")
    private int size = 16;
    @SerializedName("color")
    private int color = 0x000000; // Black

    private Font()
    {

    }

    public Font(String family, int size, int color)
    {
        this.family = family;
        this.size = size;
        this.color = color;
    }

    public String getFamily()
    {
        return family;
    }

    public int getSize()
    {
        return size;
    }

    public int getColorRGB()
    {
        return color & 0xFFFFFF;
    }
}
