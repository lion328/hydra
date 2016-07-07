package com.lion328.xenonlauncher.launcher.ui.swing.data;

import com.lion328.xenonlauncher.launcher.ui.swing.data.type.ComponentTypeAdapter;

public class Label extends TextComponent
{

    public static final String TYPE = "label";

    static
    {
        ComponentTypeAdapter.registerComponent(TYPE, Label.class);
    }

    public Label(String id, int width, int height, int x, int y, int color, String defaultText, Font font)
    {
        super(id, TYPE, width, height, x, y, color, defaultText, font);
    }

    public Label(String id, String type, int width, int height, int x, int y, int color, String defaultText, Font font)
    {
        super(id, type, width, height, x, y, color, defaultText, font);
    }
}
