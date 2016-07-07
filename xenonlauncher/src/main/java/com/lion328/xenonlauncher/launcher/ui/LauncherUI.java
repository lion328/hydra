package com.lion328.xenonlauncher.launcher.ui;

import com.lion328.xenonlauncher.downloader.DownloaderCallback;
import com.lion328.xenonlauncher.launcher.Launcher;

public interface LauncherUI extends DownloaderCallback
{

    void start();

    Launcher getLauncher();

    void setLauncher(Launcher launcher);

    boolean isVisible();

    void setVisible(boolean visible);

    void displayError(String message);
}
