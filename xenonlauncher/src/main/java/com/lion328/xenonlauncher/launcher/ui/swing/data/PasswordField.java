package com.lion328.xenonlauncher.launcher.ui.swing.data;

import com.lion328.xenonlauncher.launcher.ui.swing.data.type.ComponentTypeAdapter;

public class PasswordField extends TextField
{

    public static final String TYPE = "passwordField";

    static
    {
        ComponentTypeAdapter.registerComponent(TYPE, PasswordField.class);
    }

    public PasswordField(String id, int width, int height, int x, int y, int color, String defaultText, Font font, boolean decorated, boolean enabled)
    {
        super(id, TYPE, width, height, x, y, color, defaultText, font, decorated, enabled);
    }

    public PasswordField(String id, String type, int width, int height, int x, int y, int color, String defaultText, Font font, boolean decorated, boolean enabled)
    {
        super(id, type, width, height, x, y, color, defaultText, font, decorated, enabled);
    }
}
