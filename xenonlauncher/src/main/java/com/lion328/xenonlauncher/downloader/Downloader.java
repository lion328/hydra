package com.lion328.xenonlauncher.downloader;

import java.io.IOException;

public interface Downloader extends DownloaderCallbackHandler
{

    void download() throws IOException;

    void stop();

    boolean isRunning();
}
