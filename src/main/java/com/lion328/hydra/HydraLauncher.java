package com.lion328.hydra;

import com.google.gson.Gson;
import com.lion328.xenonlauncher.downloader.Downloader;
import com.lion328.xenonlauncher.downloader.FileDownloader;
import com.lion328.xenonlauncher.downloader.MultipleDownloader;
import com.lion328.xenonlauncher.downloader.VerifiyFileDownloader;
import com.lion328.xenonlauncher.downloader.verifier.FileVerifier;
import com.lion328.xenonlauncher.downloader.verifier.MessageDigestFileVerifier;
import com.lion328.xenonlauncher.downloader.verifier.MultipleFileVerifier;
import com.lion328.xenonlauncher.launcher.Launcher;
import com.lion328.xenonlauncher.launcher.ui.LauncherUI;
import com.lion328.xenonlauncher.minecraft.api.authentication.UserInformation;
import com.lion328.xenonlauncher.minecraft.launcher.GameLauncher;
import com.lion328.xenonlauncher.minecraft.launcher.json.JSONGameLauncher;
import com.lion328.xenonlauncher.minecraft.launcher.json.data.GameVersion;
import com.lion328.xenonlauncher.minecraft.launcher.json.data.MergedGameVersion;
import com.lion328.xenonlauncher.minecraft.launcher.json.data.gson.GsonFactory;
import com.lion328.xenonlauncher.minecraft.launcher.json.exception.LauncherVersionException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.JFrame;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

public class HydraLauncher implements Launcher
{

    public static final String VERSION = "0.1";
    public static final String LAUNCHER_DIRECTORY_NAME = "HydraLauncherMC";
    public static final String PLAYER_SETTINGS_FILENAME = "clientsettings.json";

    private static Logger logger;

    private final File gameDirectory;
    private final File playerSettingsFile;
    private LauncherUI ui;
    private HydraSettings settings;
    private PlayerSettings playerSettings;
    private boolean streamGameOutput;
    private boolean passPassword;

    public HydraLauncher(HydraSettings settings, boolean streamGameOutput, boolean passPassword)
    {
        this.settings = settings;
        this.streamGameOutput = streamGameOutput;
        this.passPassword = passPassword;

        gameDirectory = Util.getGameDirectory(settings.getApplicationDirectoryName());
        playerSettingsFile = new File(gameDirectory, PLAYER_SETTINGS_FILENAME);

        loadPlayerSettings();
    }

    public PlayerSettings getPlayerSettings()
    {
        return playerSettings;
    }

    public void openSettingsDialog()
    {
        JFrame mainFrame = null;

        if (ui instanceof HydraLauncherUI)
        {
            mainFrame = ((HydraLauncherUI) ui).getJFrame();
        }

        LaunchSettingsUI ui = new LaunchSettingsUI(playerSettings, mainFrame);
        ui.getDialog().setLocationRelativeTo(mainFrame);
        ui.start();
    }

    public void exit(int status)
    {
        savePlayerSettings();

        System.exit(status);
    }

    private void loadPlayerSettings()
    {
        if (playerSettingsFile.isFile())
        {
            Gson gson = new Gson();

            try
            {
                playerSettings = gson.fromJson(new FileReader(playerSettingsFile), PlayerSettings.class);
            }
            catch (FileNotFoundException ignore)
            {

            }
        }

        if (playerSettings == null)
        {
            playerSettings = new PlayerSettings();
        }
    }

    private void savePlayerSettings()
    {
        try
        {
            if (!playerSettingsFile.exists() && !playerSettingsFile.getParentFile().mkdirs())
            {
                getLogger().info("Can't create parent directory of playerSettingsFile");
            }

            FileWriter writer = new FileWriter(playerSettingsFile);
            new Gson().toJson(playerSettings, writer);
            writer.close();
        }
        catch (IOException e)
        {
            getLogger().catching(e);
        }
    }

    private boolean checkAuthentication(String username, char[] password) throws IOException
    {
        if (settings.getAuthenticationURL() == null)
        {
            return true;
        }

        if (username == null || username.length() == 0 || password == null || password.length == 0)
        {
            return false;
        }

        HttpURLConnection connection = (HttpURLConnection) settings.getAuthenticationURL().openConnection();
        connection.setRequestMethod("POST");

        String param = "username=" + URLEncoder.encode(username, StandardCharsets.UTF_8.name()) + "&password=" + URLEncoder.encode(new String(password), StandardCharsets.UTF_8.name());
        byte[] paramBytes = param.getBytes(StandardCharsets.UTF_8);

        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("Content-Length", String.valueOf(paramBytes.length));

        connection.setDoOutput(true);
        connection.getOutputStream().write(paramBytes);

        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String s;
        StringBuilder sb = new StringBuilder();

        while ((s = br.readLine()) != null)
        {
            sb.append(s);
        }

        return sb.toString().trim().equals("true");
    }

    private List<String> getFileWhitelist() throws IOException
    {
        List<String> whitelistFileList = new ArrayList<>();

        if (settings.getWhitelistFileListURL() != null)
        {
            HttpURLConnection connection = (HttpURLConnection) settings.getWhitelistFileListURL().openConnection();
            connection.setRequestMethod("GET");

            BufferedReader br = new BufferedReader(new InputStreamReader((connection.getInputStream())));
            String s;

            while ((s = br.readLine()) != null)
            {
                s = s.trim();

                if (!s.isEmpty())
                {
                    whitelistFileList.add(s);
                }
            }
        }

        whitelistFileList.add(PLAYER_SETTINGS_FILENAME); // Avoid error
        whitelistFileList.add("launcher.jar");
        whitelistFileList.add("updater.exe");

        return whitelistFileList;
    }

    private Map<String, String> getFileList() throws IOException
    {
        Map<String, String> remoteFiles = new HashMap<>();

        HttpURLConnection connection = (HttpURLConnection) settings.getCompressedFileListURL().openConnection();
        connection.setRequestMethod("GET");

        BufferedReader br = new BufferedReader(new InputStreamReader(new GZIPInputStream(connection.getInputStream())));
        String fileName, hash, s;

        while ((s = br.readLine()) != null)
        {
            s = s.trim();

            if (s.isEmpty())
            {
                continue;
            }

            int idx = s.indexOf(':');

            fileName = s.substring(0, idx);
            hash = s.substring(idx + 1);

            remoteFiles.put(fileName, hash);
        }

        return remoteFiles;
    }

    private Downloader getDownloader() throws IOException
    {
        List<String> whitelistFileList = getFileWhitelist();
        Map<String, String> remoteFiles = getFileList();
        Path gameDirectoryPath = gameDirectory.toPath();

        Map<File, Downloader> downloaders = new LinkedHashMap<>();
        Downloader downloader;
        FileVerifier verifier;

        WhitelistFileVerifier whitelistFileVerifier = new WhitelistFileVerifier(whitelistFileList, gameDirectoryPath);

        for (Map.Entry<String, String> entry : remoteFiles.entrySet())
        {
            String name = URLDecoder.decode(entry.getKey(), StandardCharsets.UTF_8.name());

            File file = new File(gameDirectory, name);
            URL url = new URL(settings.getFileBaseURL() + name + ".gz");

            try
            {
                URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
                url = new URL(uri.toURL().toString().replace("#", "%23"));
            }
            catch (URISyntaxException e)
            {
                getLogger().catching(e);
            }

            verifier = new MessageDigestFileVerifier(MessageDigestFileVerifier.SHA_1, entry.getValue());
            verifier = new MultipleFileVerifier(whitelistFileVerifier, verifier, MultipleFileVerifier.LOGIC_OR);

            downloader = new GZIPFileDownloader(url, file);
            downloader = new VerifiyFileDownloader((FileDownloader) downloader, verifier);

            downloaders.put(file, downloader);
        }

        List<File> localFiles = Util.listFiles(gameDirectory, false);

        if (localFiles == null)
        {
            throw new IOException("Can't list directory (" + gameDirectory.getAbsolutePath() + ")");
        }

        for (File file : localFiles)
        {
            if (downloaders.containsKey(file) || file.isDirectory())
            {
                continue;
            }

            downloaders.put(file, new VerifiyFileDownloader(new DeleteFileDownloader(file, false), whitelistFileVerifier));
        }

        return new MultipleDownloader(new ArrayList<>(downloaders.values()));
    }

    private GameVersion getGameVersion() throws IOException
    {
        return getGameVersion(settings.getGameVersionName());
    }

    private GameVersion getGameVersion(String version) throws IOException
    {
        return getGameVersion(version, 8);
    }

    private GameVersion getGameVersion(String version, int recursiveDepth) throws IOException
    {
        if (recursiveDepth < 0)
        {
            throw new GameVersionRecursiveException("Too much deep");
        }

        File versionFile = new File(gameDirectory, "versions/" + version + "/" + version + ".json");
        GameVersion gameVersion;

        gameVersion = GsonFactory.create().fromJson(new FileReader(versionFile), GameVersion.class);

        if (gameVersion.getParentID() != null)
        {
            gameVersion = new MergedGameVersion(gameVersion, getGameVersion(gameVersion.getParentID(), --recursiveDepth));
        }

        return gameVersion;
    }

    private GameLauncher getGameLauncher(GameVersion version, String username, String loginToken) throws LauncherVersionException
    {
        GameLauncher gameLauncher;

        gameLauncher = new JSONGameLauncher(version, gameDirectory);

        for (String arg : settings.getVMArguments())
        {
            gameLauncher.addJVMArgument(arg);
        }

        if (loginToken != null)
        {
            gameLauncher.addJVMArgument("-Dcom.lion328.hydra.loginToken=" + loginToken);
        }

        gameLauncher.setMaxMemorySize(playerSettings.getMaximumMemory());
        gameLauncher.setUserInformation(new UserInformation("1234", username, "1234", "1234"));

        return gameLauncher;
    }

    private void displayErrorLang(String key)
    {
        displayErrorLang(key, null);
    }

    private void displayErrorLang(String key, String errorDetails)
    {
        StringBuilder sb = new StringBuilder();

        sb.append(Language.get(key));

        if (errorDetails != null)
        {
            sb.append(" (");
            sb.append(errorDetails);
            sb.append(')');
        }

        ui.displayError(sb.toString());
    }

    private void streamGameOutput(final Process process)
    {
        if (!streamGameOutput)
        {
            return;
        }

        new Thread("Game Output Monitor")
        {

            @Override
            public void run()
            {
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String s;

                try
                {
                    while ((s = reader.readLine()) != null)
                    {
                        System.out.println(s);
                    }
                }
                catch (IOException e)
                {
                    getLogger().catching(e);
                }
            }
        }.start();

        new Thread("Game Error Monitor")
        {

            @Override
            public void run()
            {
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                String s;

                try
                {
                    while ((s = reader.readLine()) != null)
                    {
                        System.out.println(s);
                    }
                }
                catch (IOException e)
                {
                    getLogger().catching(e);
                }
            }
        }.start();
    }

    @Override
    public void start()
    {
        ui.start();
        ui.setVisible(true);
    }

    @Override
    public UserInformation getCacheUser() throws IOException
    {
        return null;
    }

    @Override
    public void setCacheUser(UserInformation userInfo) throws IOException
    {

    }

    @Override
    public void clearCacheUser() throws IOException
    {

    }

    @Override
    public LauncherUI getLauncherUI()
    {
        return ui;
    }

    @Override
    public void setLauncherUI(LauncherUI ui)
    {
        this.ui = ui;
        ui.setLauncher(this);
    }

    @Override
    public boolean loginAndLaunch(String username, char[] password)
    {
        // auth

        try
        {
            if (!checkAuthentication(username, password))
            {
                displayErrorLang("authFailed");

                return false;
            }
        }
        catch (IOException e)
        {
            getLogger().catching(e);
            displayErrorLang("authNetworkError", e.getMessage());

            return false;
        }

        playerSettings.setPlayerName(username);

        // update

        Downloader downloader = null;

        try
        {
            downloader = getDownloader();
        }
        catch (IOException e)
        {
            getLogger().catching(e);
            displayErrorLang("gameDownloadError", e.getMessage());

            return false;
        }

        downloader.registerCallback(ui);

        try
        {
            downloader.download();
        }
        catch (IOException e)
        {
            getLogger().catching(e);
            displayErrorLang("gameDownloadError", e.getMessage());

            return false;
        }

        // launch

        GameVersion gameVersion = null;

        try
        {
            gameVersion = getGameVersion();
        }
        catch (IOException e)
        {
            getLogger().catching(e);
            displayErrorLang("gameLaunchInfoNotFound", e.getMessage());

            return false;
        }

        GameLauncher launcher = null;

        try
        {
            launcher = getGameLauncher(gameVersion, username, null);

            if (passPassword)
            {
                launcher.addJVMArgument("-Dcom.lion328.autochatlogin.password=" + new String(password));
            }
        }
        catch (LauncherVersionException e)
        {
            getLogger().catching(e);
            displayErrorLang("gameLaunchInfoReadingError", e.getMessage());

            return false;
        }

        try
        {
            streamGameOutput(launcher.launch());
        }
        catch (Exception e)
        {
            getLogger().catching(e);
            displayErrorLang("gameLaunchError", e.getMessage());
        }

        return true;
    }

    @Override
    public void exit()
    {
        exit(0);
    }

    public static Logger getLogger()
    {
        if (logger == null)
        {
            logger = LogManager.getLogger("hydralauncher");
        }

        return logger;
    }
}
