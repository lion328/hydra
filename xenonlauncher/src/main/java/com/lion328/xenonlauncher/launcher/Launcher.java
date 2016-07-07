package com.lion328.xenonlauncher.launcher;

import com.lion328.xenonlauncher.launcher.ui.LauncherUI;
import com.lion328.xenonlauncher.minecraft.api.authentication.UserInformation;

import java.io.IOException;

public interface Launcher
{

    void start();

    UserInformation getCacheUser() throws IOException;

    void setCacheUser(UserInformation userInfo) throws IOException;

    void clearCacheUser() throws IOException;

    LauncherUI getLauncherUI();

    void setLauncherUI(LauncherUI ui);

    boolean loginAndLaunch(String username, char[] password);

    void exit();
}
