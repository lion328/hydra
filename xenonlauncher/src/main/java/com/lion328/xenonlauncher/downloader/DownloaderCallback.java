package com.lion328.xenonlauncher.downloader;

import java.io.File;

public interface DownloaderCallback
{

    void onPercentageChange(File file, int overallPercentage, long fileSize, long fileDownloaded);
}
