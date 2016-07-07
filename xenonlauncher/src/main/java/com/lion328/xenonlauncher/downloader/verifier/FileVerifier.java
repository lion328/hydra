package com.lion328.xenonlauncher.downloader.verifier;

import java.io.File;
import java.io.IOException;

public interface FileVerifier
{

    boolean isValid(File file) throws IOException;
}
