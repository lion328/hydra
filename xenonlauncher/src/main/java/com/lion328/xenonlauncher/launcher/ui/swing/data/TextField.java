package com.lion328.xenonlauncher.launcher.ui.swing.data;

import com.google.gson.annotations.SerializedName;
import com.lion328.xenonlauncher.launcher.ui.swing.data.type.ComponentTypeAdapter;

public class TextField extends TextComponent
{

    public static final String TYPE = "textField";

    static
    {
        ComponentTypeAdapter.registerComponent(TYPE, TextField.class);
    }

    @SerializedName("decorated")
    private boolean decorated = true;
    @SerializedName("enabled")
    private boolean enabled = true;

    public TextField(String id, int width, int height, int x, int y, int color, String defaultText, Font font, boolean decorated, boolean enabled)
    {
        this(id, TYPE, width, height, x, y, color, defaultText, font, decorated, enabled);
    }

    public TextField(String id, String type, int width, int height, int x, int y, int color, String defaultText, Font font, boolean decorated, boolean enabled)
    {
        super(id, type, width, height, x, y, color, defaultText, font);

        this.decorated = decorated;
        this.enabled = enabled;
    }

    public boolean isDecorated()
    {
        return decorated;
    }

    public boolean isEnabled()
    {
        return enabled;
    }
}
