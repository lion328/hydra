package com.lion328.xenonlauncher.launcher.ui.swing.data;

import com.google.gson.annotations.SerializedName;

public class TextComponent extends Component
{

    @SerializedName("defaultText")
    private String defaultText = "";
    @SerializedName("font")
    private Font font = Font.DEFAULT;

    public TextComponent(String id, String type, int width, int height, int x, int y, int color, String defaultText, Font font)
    {
        super(id, type, width, height, x, y, color);

        this.defaultText = defaultText;
        this.font = font;
    }

    public String getDefaultText()
    {
        return defaultText;
    }

    public Font getFont()
    {
        return font;
    }
}
