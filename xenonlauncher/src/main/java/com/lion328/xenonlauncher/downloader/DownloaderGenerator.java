package com.lion328.xenonlauncher.downloader;

import java.io.IOException;
import java.util.List;

public interface DownloaderGenerator
{

    List<Downloader> generateDownloaders() throws IOException;
}
