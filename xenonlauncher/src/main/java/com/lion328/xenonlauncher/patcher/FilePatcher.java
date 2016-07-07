package com.lion328.xenonlauncher.patcher;

public interface FilePatcher
{

    byte[] patchFile(String path, byte[] original) throws Exception;
}
