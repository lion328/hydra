package com.lion328.hydra;

public class Main
{

    public static void main(String[] args)
    {
        System.setProperty("swing.aatxt", "true");
        System.setProperty("awt.useSystemAAFontSettings", "on");

        LauncherUI ui = new LauncherUI();
        ui.start();
    }
}
