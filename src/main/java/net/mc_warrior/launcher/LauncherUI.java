package net.mc_warrior.launcher;

import com.lion328.xenonlauncher.downloader.Downloader;
import com.lion328.xenonlauncher.downloader.DownloaderCallback;
import com.lion328.xenonlauncher.downloader.FileDownloader;
import com.lion328.xenonlauncher.downloader.MultipleDownloader;
import com.lion328.xenonlauncher.downloader.URLFileDownloader;
import com.lion328.xenonlauncher.downloader.VerifiyFileDownloader;
import com.lion328.xenonlauncher.downloader.verifier.MessageDigestFileVerifier;
import com.lion328.xenonlauncher.minecraft.api.authentication.UserInformation;
import com.lion328.xenonlauncher.minecraft.launcher.GameLauncher;
import com.lion328.xenonlauncher.minecraft.launcher.json.JSONGameLauncher;
import com.lion328.xenonlauncher.minecraft.launcher.json.data.GameVersion;
import com.lion328.xenonlauncher.minecraft.launcher.json.data.MergedGameVersion;
import com.lion328.xenonlauncher.minecraft.launcher.json.data.gson.GsonFactory;
import com.lion328.xenonlauncher.minecraft.launcher.json.exception.LauncherVersionException;
import com.lion328.xenonlauncher.util.FileUtil;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LauncherUI
{

    private LaunchSettingsUI launchSettingsUI;

    private JFrame mainFrame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JProgressBar statusProgressBar;
    private JLabel statusLabel;

    private boolean launching = false;
    private boolean updatingStatus = false;

    public LauncherUI(LaunchSettingsUI launchSettingsUI)
    {
        this.launchSettingsUI = launchSettingsUI;
    }

    public void start()
    {
        boolean needUpdate = false;

        try
        {
            needUpdate = needLauncherUpdate();
        }
        catch (IOException e)
        {
            Settings.LOGGER.catching(e);
            JOptionPane.showMessageDialog(null, "ไม่สามารถเชื่อมต่อเซิร์ฟเวอร์ได้", "เกิดข้อผิดพลาด", JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
        }

        if (needUpdate)
        {
            try
            {
                if (!updateSelf())
                {
                    JOptionPane.showMessageDialog(null, "ไม่สามารถปรับปรุง Launcher ได้", "เกิดข้อผิดพลาด", JOptionPane.ERROR_MESSAGE);
                    System.exit(-1);
                }
            }
            catch (InterruptedException e)
            {
                Settings.LOGGER.catching(e);
                JOptionPane.showMessageDialog(null, "ไม่สามารถปรับปรุง Launcher ได้ (InterruptedException)", "เกิดข้อผิดพลาด", JOptionPane.ERROR_MESSAGE);
                System.exit(-1);
            }
            //JOptionPane.showMessageDialog(null, "Launcher ล้าหลัง กรุณาดาวน์โหลดใหม่ได้ที่ " + Settings.WEBSITE_URL.toString(), "Launcher ล้าหลัง", JOptionPane.INFORMATION_MESSAGE);
            //System.exit(0);
        }

        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (ClassNotFoundException | UnsupportedLookAndFeelException | InstantiationException | IllegalAccessException e)
        {
            Settings.LOGGER.catching(e);
        }

        mainFrame = new JFrame();
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel;

        try
        {
            panel = new ImagePanel(ImageIO.read(Settings.class.getResourceAsStream("/net/mc_warrior/launcher/resources/launcher_bg.png")));
        }
        catch (IOException e)
        {
            Settings.LOGGER.catching(e);
            panel = new JPanel();
        }

        panel.setLayout(null);
        //panel.setSize(800, 500);
        panel.setPreferredSize(new Dimension(800, 500));
        //panel.setBorder(new EmptyBorder(0, 0, 0, 0));

        //mainFrame.setResizable(false);
        mainFrame.setLayout(new BorderLayout());
        mainFrame.add(panel, BorderLayout.CENTER);
        mainFrame.setResizable(false);
        mainFrame.setTitle("MC-Warrior Launcher");

        usernameField = new JTextField();
        passwordField = new JPasswordField();
        statusLabel = new JLabel();
        statusProgressBar = new JProgressBar();

        usernameField.setBounds(600, 68, 180, 23);
        passwordField.setBounds(600, 116, 180, 23);
        statusLabel.setBounds(75, 431, 700, 20);
        statusProgressBar.setBounds(15, 460, 545, 30);

        statusLabel.setForeground(Color.WHITE);

        panel.add(usernameField);
        panel.add(passwordField);
        panel.add(statusLabel);
        panel.add(statusProgressBar);

        JPanel launchButton = new JPanel();
        JPanel registerButton = new JPanel();

        launchButton.setBounds(598, 154, 89, 32);
        launchButton.setOpaque(false);
        launchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        registerButton.setBounds(700, 162, 81, 32);
        registerButton.setOpaque(false);
        registerButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        launchButton.addMouseListener(new MouseAdapter()
        {

            @Override
            public void mouseClicked(MouseEvent mouseEvent)
            {
                launchButton();
            }
        });

        registerButton.addMouseListener(new MouseAdapter()
        {

            @Override
            public void mouseClicked(MouseEvent mouseEvent)
            {
                if (Desktop.isDesktopSupported())
                {
                    try
                    {
                        Desktop.getDesktop().browse(Settings.REGISTER_URL.toURI());
                    }
                    catch (IOException | URISyntaxException e)
                    {
                        Settings.LOGGER.catching(e);
                    }
                }
            }
        });

        panel.add(launchButton);
        panel.add(registerButton);

        resetUI();

        mainFrame.pack();
        mainFrame.setLocationRelativeTo(null);

        mainFrame.setVisible(true);
    }

    public void launchButton()
    {
        if (launching)
        {
            return;
        }

        launching = true;

        usernameField.setEnabled(false);
        passwordField.setEnabled(false);

        statusLabel.setText("กำลังตรวจสอบข้อมูลผู้เล่น");

        boolean validAuth = false;

        try
        {
            validAuth = checkAuthentication();
        }
        catch (IOException e)
        {
            Settings.LOGGER.catching(e);
            JOptionPane.showMessageDialog(null, "ไม่สามารถเชื่อมต่อเซิร์ฟเวอร์ได้", "เกิดข้อผิดพลาด", JOptionPane.ERROR_MESSAGE);
        }

        if (!validAuth)
        {
            JOptionPane.showMessageDialog(null, "ชื่อผู้ใช้หรือรหัสผ่านไม่ถูกต้อง กรุณากรอกใหม่", "เกิดข้อผิดพลาด", JOptionPane.ERROR_MESSAGE);
            resetUI();
            return;
        }

        statusLabel.setText("กำลังดาวน์โหลด");

        new Thread() {

            @Override
            public void run()
            {
                try
                {
                    if (!downloadGame())
                    {
                        resetUI();
                        return;
                    }
                }
                catch (IOException e)
                {
                    JOptionPane.showMessageDialog(null, "ไม่สามารถดาวน์โหลดเกมได้ กรุณาลองใหม่ภายหลัง", "เกิดข้อผิดพลาด", JOptionPane.ERROR_MESSAGE);
                    resetUI();
                    return;
                }

                statusLabel.setText("กำลังเปิดเกม");

                final Process process = launchGame();

                if (process == null)
                {
                    resetUI();
                    return;
                }

                mainFrame.setVisible(false);

                try
                {

                    new Thread()
                    {

                        @Override
                        public void run()
                        {
                            int b;
                            try
                            {
                                while ((b = process.getErrorStream().read()) != -1)
                                {
                                    System.err.write(b);
                                }
                            }
                            catch (IOException e)
                            {
                                Settings.LOGGER.catching(e);
                            }
                        }
                    }.start();

                    Thread td = new Thread()
                    {

                        @Override
                        public void run()
                        {
                            int b;
                            try
                            {
                                while ((b = process.getInputStream().read()) != -1)
                                {
                                    System.out.write(b);
                                }
                            }
                            catch (IOException e)
                            {
                                Settings.LOGGER.catching(e);
                            }
                        }
                    };
                    td.start();

                    process.waitFor();
                }
                catch (InterruptedException e)
                {
                    Settings.LOGGER.catching(e);
                }

                System.exit(0);
            }
        }.start();
    }

    public void resetUI()
    {
        usernameField.setEnabled(true);
        passwordField.setEnabled(true);

        statusProgressBar.setValue(0);
        statusLabel.setText("ไม่มีการทำงาน");

        launching = false;
    }

    public boolean checkAuthentication() throws IOException
    {
        if (usernameField.getText().length() == 0 || passwordField.getPassword().length == 0)
        {
            return false;
        }

        String param = "?username=" + usernameField.getText() + "&password=" + new String(passwordField.getPassword());
        URL url = new URL(Settings.AUTHENTICATION_URL.toString() + param);
        return Util.httpGET(url).trim().equals("true");
    }

    public boolean downloadGame() throws IOException
    {
        Map<String, String> remoteFiles = new HashMap<>();

        HttpURLConnection connection = (HttpURLConnection) Settings.FILES_LIST_URL.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader br = new BufferedReader(new InputStreamReader((connection.getInputStream())));
        String s, fileName, hash;

        while ((s = br.readLine()) != null)
        {
            s = s.trim();

            int idx = s.indexOf(':');

            fileName = s.substring(0, idx);
            hash = s.substring(idx + 1);

            remoteFiles.put(fileName, hash);
        }

        if (!Settings.GAME_DIRECTORY.exists() && !Settings.GAME_DIRECTORY.mkdirs())
        {
            Settings.LOGGER.error("Can't create " + Settings.GAME_DIRECTORY);
            JOptionPane.showMessageDialog(null, "ไม่สามารถสร้างโฟลเดอร์เกมได้", "ข้อผิดพลาด", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        else
        {
            Path gameDirectoryPath = Settings.GAME_DIRECTORY.toPath();

            List<Downloader> downloaders = new ArrayList<>();
            Downloader downloader;

            for (Map.Entry<String, String> entry : remoteFiles.entrySet())
            {
                String name = URLDecoder.decode(entry.getKey(), StandardCharsets.UTF_8.name());

                File file = new File(Settings.GAME_DIRECTORY, name);
                URL url = new URL(Settings.FILES_URL.toString() + name);

                try
                {
                    URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
                    url = new URL(uri.toURL().toString().replace("#", "%23"));
                }
                catch (URISyntaxException e)
                {
                    Settings.LOGGER.catching(e);
                    /*JOptionPane.showMessageDialog(null, "ไม่สามารถสร้าง URI ได้", "เกิดข้อผิดพลาด", JOptionPane.ERROR_MESSAGE);
                    return false;*/
                }

                downloader = new URLFileDownloader(url, file);
                downloader = new VerifiyFileDownloader((FileDownloader) downloader, new MessageDigestFileVerifier(MessageDigestFileVerifier.SHA_1, entry.getValue()));

                downloaders.add(downloader);
            }

            downloader = new MultipleDownloader(downloaders);

            downloader.registerCallback(new DownloaderCallback()
            {

                @Override
                public void onPercentageChange(final File file, final int overallPercentage, final long fileSize, final long fileDownloaded)
                {
                    if (updatingStatus)
                    {
                        return;
                    }

                    SwingUtilities.invokeLater(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            updatingStatus = true;

                            String text = "กำลังดาวน์โหลด " + file.getName() + ", " + overallPercentage + "%";

                            if (fileDownloaded > 0 && fileSize > 0)
                            {
                                text += ", " + Util.convertUnit(fileDownloaded) + "B/" + Util.convertUnit(fileSize) + "B";
                            }

                            statusLabel.setText(text);
                            statusProgressBar.setValue(overallPercentage);

                            updatingStatus = false;
                        }
                    });
                }
            });

            downloader.download();

            List<File> localFiles = Util.listFiles(Settings.GAME_DIRECTORY, false);

            if (localFiles == null)
            {
                Settings.LOGGER.error("Can't list " + Settings.GAME_DIRECTORY);
                JOptionPane.showMessageDialog(null, "ไม่สามารถอ่านโฟลเดอร์เกมได้", "ข้อผิดพลาด", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            startWatchDirectoryChanges();

            Path filePathRelativize;

            for (File file : localFiles)
            {
                file = file.getAbsoluteFile();
                filePathRelativize = gameDirectoryPath.relativize(file.toPath());

                if (!remoteFiles.containsKey(filePathRelativize.toString().replace(File.separatorChar, '/')))
                {
                    if (file.exists() && !FileUtil.deleteFileRescursive(file))
                    {
                        Settings.LOGGER.error("Can't delete " + file.toString());
                        JOptionPane.showMessageDialog(null, "ไม่สามารถลบไฟล์ได้", "ข้อผิดพลาด", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public boolean needLauncherUpdate() throws IOException
    {
        return !Util.httpGET(Settings.REMOTE_LAUNCHER_VERSION_URL).trim().equals(Settings.LAUNCHER_VERSION);
    }

    public boolean updateSelf() throws InterruptedException
    {
        SelfUpdaterUI updaterUI = new SelfUpdaterUI(Settings.getWorkingJar(), new URLFileDownloader(Settings.REMOTE_LAUNCHER_URL, Settings.getWorkingJar()));
        updaterUI.start();
        return updaterUI.waitFor();
    }

    public GameVersion getGameVersion(String version)
    {
        File versionFile = new File(Settings.GAME_DIRECTORY, "versions/" + version + "/" + version + ".json");
        GameVersion gameVersion;

        try
        {
            gameVersion = GsonFactory.create().fromJson(new FileReader(versionFile), GameVersion.class);
        }
        catch (FileNotFoundException e)
        {
            Settings.LOGGER.catching(e);
            JOptionPane.showMessageDialog(null, "ไม่มีข้อมูลการเปิดเกม กรุณาติดต่อผู้ดูแลระบบ", "เกิดข้อผิดพลาด", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        if (gameVersion.getParentID() != null)
        {
            gameVersion = new MergedGameVersion(gameVersion, getGameVersion(gameVersion.getParentID()));
        }

        return gameVersion;
    }

    public Process launchGame()
    {
        File versionFile = new File(Settings.GAME_DIRECTORY, "versions/" + Settings.GAME_VERSION_NAME + "/" + Settings.GAME_VERSION_NAME + ".json");
        GameLauncher gameLauncher;

        try
        {
            gameLauncher = new JSONGameLauncher(getGameVersion(Settings.GAME_VERSION_NAME), Settings.GAME_DIRECTORY);
        }
        catch (LauncherVersionException e)
        {
            Settings.LOGGER.catching(e);
            JOptionPane.showMessageDialog(null, "ไม่สามารถอ่านข้อมูลในการเปิดเกมได้ กรุณาติดต่อผู้ดูแลระบบ", "เกิดข้อผิดพลาด", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        gameLauncher.addJVMArgument("-XX:HeapDumpPath=MojangTricksIntelDriversForPerformance_javaw.exe_minecraft.exe.heapdump");
        gameLauncher.addJVMArgument("-XX:+UseConcMarkSweepGC");
        gameLauncher.addJVMArgument("XX:+CMSIncrementalMode");
        gameLauncher.addJVMArgument("-XX:-UseAdaptiveSizePolicy");

        gameLauncher.setMaxMemorySize(1024);
        gameLauncher.setUserInformation(new UserInformation("1234", usernameField.getText(), "1234", "1234"));

        try
        {
            return gameLauncher.launch();
        }
        catch (Exception e)
        {
            Settings.LOGGER.catching(e);
            JOptionPane.showMessageDialog(null, "ไม่สามาถเปิดเกมได้", "เกิดข้อผิดพลาด", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public void startWatchDirectoryChanges()
    {

    }
}
