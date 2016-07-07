package com.lion328.xenonlauncher.launcher.ui.swing.data;

import com.google.gson.annotations.SerializedName;
import com.lion328.xenonlauncher.launcher.ui.swing.data.type.ComponentTypeAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Panel extends Component
{

    public static final String TYPE = "panel";

    static
    {
        ComponentTypeAdapter.registerComponent(TYPE, Panel.class);
    }

    @SerializedName("backgroundMovable")
    private boolean backgroundMovable = false;
    @SerializedName("font")
    private Font font = Font.DEFAULT;
    @SerializedName("children")
    private List<Component> children;

    public Panel(String id, int width, int height, int x, int y, int color, boolean backgroundMovable, Font font, List<Component> children)
    {
        this(id, TYPE, width, height, x, y, color, backgroundMovable, font, children);
    }

    public Panel(String id, String type, int width, int height, int x, int y, int color, boolean backgroundMovable, Font font, List<Component> children)
    {
        super(id, type, width, height, x, y, color);

        this.backgroundMovable = backgroundMovable;
        this.font = font;
        this.children = Collections.unmodifiableList(new ArrayList<>(children));
    }

    public boolean isBackgroundMovable()
    {
        return backgroundMovable;
    }

    public Font getFont()
    {
        return font;
    }

    public List<Component> getChildren()
    {
        return children;
    }
}
