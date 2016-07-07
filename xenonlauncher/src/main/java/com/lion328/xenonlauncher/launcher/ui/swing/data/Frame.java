package com.lion328.xenonlauncher.launcher.ui.swing.data;

import com.google.gson.annotations.SerializedName;
import com.lion328.xenonlauncher.launcher.ui.swing.data.type.ComponentTypeAdapter;

import java.net.URL;

public class Frame extends Component
{

    public static final String TYPE = "frame";

    static
    {
        ComponentTypeAdapter.registerComponent(TYPE, Frame.class);
    }

    @SerializedName("resizable")
    private boolean resizable = false;
    @SerializedName("decorated")
    private boolean decorated = true;
    @SerializedName("panel")
    private Panel panel;
    @SerializedName("iconURL")
    private URL iconURL;

    public Frame(String id, int width, int height, int x, int y, int color, boolean resizable, boolean decorated, Panel panel, URL iconURL)
    {
        this(id, TYPE, width, height, x, y, color, resizable, decorated, panel, iconURL);
    }

    public Frame(String id, String type, int width, int height, int x, int y, int color, boolean resizable, boolean decorated, Panel panel, URL iconURL)
    {
        super(id, type, width, height, x, y, color);

        this.resizable = resizable;
        this.decorated = decorated;
        this.panel = panel;
        this.iconURL = iconURL;
    }

    public boolean isResizable()
    {
        return resizable;
    }

    public boolean isDecorated()
    {
        return decorated;
    }

    public Panel getPanel()
    {
        return panel;
    }

    public URL getIconURL()
    {
        return iconURL;
    }
}
