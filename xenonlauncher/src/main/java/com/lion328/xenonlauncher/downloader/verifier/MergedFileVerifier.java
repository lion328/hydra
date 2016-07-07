package com.lion328.xenonlauncher.downloader.verifier;

import java.io.File;
import java.io.IOException;

public class MergedFileVerifier implements FileVerifier
{

    private final FileVerifier child;
    private final FileVerifier parent;

    public MergedFileVerifier(FileVerifier child, FileVerifier parent)
    {
        this.child = child;
        this.parent = parent;
    }

    public FileVerifier getChild()
    {
        return child;
    }

    public FileVerifier getParent()
    {
        return parent;
    }

    @Override
    public boolean isValid(File file) throws IOException
    {
        return child.isValid(file) && parent.isValid(file);
    }
}
