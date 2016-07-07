package com.lion328.xenonlauncher.minecraft.api.authentication;

import com.lion328.xenonlauncher.minecraft.api.authentication.exception.MinecraftAuthenticatorException;

import java.io.IOException;

public interface MinecraftAuthenticator
{

    void login(String username, char[] password) throws IOException, MinecraftAuthenticatorException;

    void logout() throws IOException, MinecraftAuthenticatorException;

    void refresh() throws IOException, MinecraftAuthenticatorException;

    UserInformation getUserInformation();

    String getPlayerName();
}
