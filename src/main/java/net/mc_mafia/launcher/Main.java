package net.mc_mafia.launcher;

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
