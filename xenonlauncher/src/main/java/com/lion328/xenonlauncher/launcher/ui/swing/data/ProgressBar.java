package com.lion328.xenonlauncher.launcher.ui.swing.data;

import com.lion328.xenonlauncher.launcher.ui.swing.data.type.ComponentTypeAdapter;

public class ProgressBar extends Component
{

    public static final String TYPE = "progressBar";

    static
    {
        ComponentTypeAdapter.registerComponent(TYPE, ProgressBar.class);
    }

    public ProgressBar(String id, int width, int height, int x, int y, int color)
    {
        this(id, TYPE, width, height, x, y, color);
    }

    public ProgressBar(String id, String type, int width, int height, int x, int y, int color)
    {
        super(id, type, width, height, x, y, color);
    }
}
