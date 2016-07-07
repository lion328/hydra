package com.lion328.xenonlauncher.minecraft.downloader.verifier;

import com.lion328.xenonlauncher.downloader.repository.DependencyName;
import com.lion328.xenonlauncher.downloader.repository.Repository;
import com.lion328.xenonlauncher.downloader.verifier.FileVerifier;
import com.lion328.xenonlauncher.downloader.verifier.MergedFileVerifier;
import com.lion328.xenonlauncher.downloader.verifier.MessageDigestFileVerifier;
import com.lion328.xenonlauncher.downloader.verifier.RepositoryFileVerifier;
import com.lion328.xenonlauncher.downloader.verifier.SizeFileVerifier;
import com.lion328.xenonlauncher.minecraft.launcher.json.data.DownloadInformation;

import java.io.File;
import java.io.IOException;

public class MinecraftFileVerifier implements FileVerifier
{

    private final FileVerifier verifier;

    public MinecraftFileVerifier(Repository repo, DependencyName name, String classifier) throws IOException
    {
        verifier = new RepositoryFileVerifier(repo, name, classifier, MessageDigestFileVerifier.SHA_1);
    }

    public MinecraftFileVerifier(DownloadInformation info)
    {
        FileVerifier tmp = new MessageDigestFileVerifier(MessageDigestFileVerifier.SHA_1, info.getSHA1Hash());
        verifier = new MergedFileVerifier(new SizeFileVerifier(info.getSizeInBytes()), tmp);
    }

    @Override
    public boolean isValid(File file) throws IOException
    {
        return verifier.isValid(file);
    }
}
