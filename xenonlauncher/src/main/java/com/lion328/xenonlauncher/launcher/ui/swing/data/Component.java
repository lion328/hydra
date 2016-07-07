package com.lion328.xenonlauncher.launcher.ui.swing.data;

import com.google.gson.annotations.SerializedName;

public class Component
{

    @SerializedName("id")
    private String id;
    @SerializedName("type")
    private String type;
    @SerializedName("width")
    private int width;
    @SerializedName("height")
    private int height;
    @SerializedName("x")
    private int x;
    @SerializedName("y")
    private int y;
    @SerializedName("color")
    private int color;

    public Component(String id, String type, int width, int height, int x, int y, int color)
    {
        this.id = id;
        this.type = type;
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public String getId()
    {
        return id;
    }

    public String getType()
    {
        return type;
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public int getColor()
    {
        return color;
    }
}
