package net.mc_warrior.launcher;

import com.google.gson.Gson;
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
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;
import javax.swing.plaf.FontUIResource;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
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
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

public class LauncherUI
{

    private final File settingsFile = new File(Settings.GAME_DIRECTORY, "launcher_settings.json");
    private PlayerSettings settings;

    private JFrame mainFrame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JProgressBar statusProgressBar;
    private JLabel statusLabel;

    private boolean launching = false;
    private boolean updatingStatus = false;

    public LauncherUI()
    {
        if (settingsFile.isFile())
        {
            Gson gson = new Gson();
            try
            {
                settings = gson.fromJson(new FileReader(settingsFile), PlayerSettings.class);

                if (settings != null)
                {
                    if (settings.getMaximumMemory() == 0)
                    {
                        settings.setMaximumMemory(1024);
                    }

                    if (settings.getPlayerName() == null)
                    {
                        settings.setPlayerName("");
                    }

                    return;
                }
            }
            catch (FileNotFoundException ignore)
            {

            }
        }

        settings = new PlayerSettings("", 1024);
    }

    public void start()
    {
        try
        {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels())
            {
                if (info.getName().equals("Metal"))
                {
                    UIManager.setLookAndFeel(info.getClassName());
                }
            }

            //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (ClassNotFoundException | UnsupportedLookAndFeelException | InstantiationException | IllegalAccessException e)
        {
            Settings.LOGGER.catching(e);
        }

        try
        {
            Font defaultFont = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/net/mc_warrior/launcher/resources/CSChatThaiUI.ttf"));
            FontUIResource fontUIResource = new FontUIResource(defaultFont.deriveFont(Font.PLAIN, 14));

            /*for (Map.Entry<Object, Object> entry : UIManager.getDefaults().entrySet())
            {
                if (entry.getValue() instanceof FontUIResource)
                {
                    UIManager.put(entry.getKey(), fontUIResource);
                }
            }*/
            UIManager.put("Button.font", fontUIResource);
            UIManager.put("ToggleButton.font", fontUIResource);
            UIManager.put("RadioButton.font", fontUIResource);
            UIManager.put("CheckBox.font", fontUIResource);
            UIManager.put("ColorChooser.font", fontUIResource);
            UIManager.put("ComboBox.font", fontUIResource);
            UIManager.put("Label.font", fontUIResource);
            UIManager.put("List.font", fontUIResource);
            UIManager.put("MenuBar.font", fontUIResource);
            UIManager.put("MenuItem.font", fontUIResource);
            UIManager.put("RadioButtonMenuItem.font", fontUIResource);
            UIManager.put("CheckBoxMenuItem.font", fontUIResource);
            UIManager.put("Menu.font", fontUIResource);
            UIManager.put("PopupMenu.font", fontUIResource);
            UIManager.put("OptionPane.font", fontUIResource);
            UIManager.put("Panel.font", fontUIResource);
            UIManager.put("ProgressBar.font", fontUIResource);
            UIManager.put("ScrollPane.font", fontUIResource);
            UIManager.put("Viewport.font", fontUIResource);
            UIManager.put("TabbedPane.font", fontUIResource);
            UIManager.put("Table.font", fontUIResource);
            UIManager.put("TableHeader.font", fontUIResource);
            UIManager.put("TextField.font", fontUIResource);
            UIManager.put("PasswordField.font", fontUIResource);
            UIManager.put("TextArea.font", fontUIResource);
            UIManager.put("TextPane.font", fontUIResource);
            UIManager.put("EditorPane.font", fontUIResource);
            UIManager.put("TitledBorder.font", fontUIResource);
            UIManager.put("ToolBar.font", fontUIResource);
            UIManager.put("ToolTip.font", fontUIResource);
            UIManager.put("Tree.font", fontUIResource);
        }
        catch (FontFormatException | IOException e)
        {
            Settings.LOGGER.catching(e);
        }

        UIManager.put("ProgressBar.selectionBackground", Color.GRAY);
        UIManager.put("ProgressBar.foreground", new Color(0x0EB600));

        if (!Settings.IGNORE_LAUNCHER_UPDATER)
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
                updateSelf();
                //JOptionPane.showMessageDialog(null, "Launcher ล้าหลัง กรุณาดาวน์โหลดใหม่ได้ที่ " + Settings.WEBSITE_URL.toString(), "Launcher ล้าหลัง", JOptionPane.INFORMATION_MESSAGE);
                //System.exit(0);
            }
        }

        mainFrame = new JFrame();
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel tmpPanel;

        try
        {
            tmpPanel = new ImagePanel(ImageIO.read(Settings.class.getResourceAsStream("/net/mc_warrior/launcher/resources/launcher_bg.png")));
        }
        catch (IOException e)
        {
            Settings.LOGGER.catching(e);
            tmpPanel = new JPanel();
        }

        final JPanel panel = tmpPanel;

        panel.setLayout(null);
        //panel.setSize(800, 500);
        panel.setPreferredSize(new Dimension(800, 500));
        //panel.setBorder(new EmptyBorder(0, 0, 0, 0));

        //mainFrame.setResizable(false);
        mainFrame.setLayout(new BorderLayout());
        mainFrame.add(panel, BorderLayout.CENTER);
        mainFrame.setResizable(false);
        mainFrame.setTitle("MC-Warrior Launcher");

        try
        {
            mainFrame.setIconImage(ImageIO.read(this.getClass().getResourceAsStream("/net/mc_warrior/launcher/resources/favicon.png")));
        }
        catch (IOException e)
        {
            Settings.LOGGER.catching(e);
        }

        usernameField = new JTextField();
        passwordField = new JPasswordField();
        statusLabel = new JLabel();
        statusProgressBar = new JProgressBar();

        usernameField.setBounds(599, 70, 180, 32);
        passwordField.setBounds(599, 135, 180, 32);
        statusLabel.setBounds(75, 431, 700, 20);
        statusProgressBar.setBounds(15, 460, 545, 27);

        Border border = BorderFactory.createEmptyBorder(0, 5, 0, 5);
        usernameField.setBorder(border);
        passwordField.setBorder(border);

        KeyListener keyListener = new KeyAdapter()
        {

            @Override
            public void keyPressed(KeyEvent e)
            {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    launchButton();
                }
            }
        };
        usernameField.addKeyListener(keyListener);
        passwordField.addKeyListener(keyListener);

        statusLabel.setForeground(Color.WHITE);
        statusProgressBar.setStringPainted(true);
        usernameField.setText(settings.getPlayerName());

        panel.add(usernameField);
        panel.add(passwordField);
        panel.add(statusLabel);
        panel.add(statusProgressBar);

        JPanel launchButton = new JPanel();
        JPanel registerButton = new JPanel();
        JPanel settingsButton = new JPanel();

        launchButton.setBounds(599, 184, 75, 32);
        launchButton.setOpaque(false);
        launchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        registerButton.setBounds(701, 192, 81, 32);
        registerButton.setOpaque(false);
        registerButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        settingsButton.setBounds(674, 184, 13, 33);
        settingsButton.setOpaque(false);
        settingsButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

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

        settingsButton.addMouseListener(new MouseAdapter()
        {

            @Override
            public void mouseClicked(MouseEvent e)
            {
                settingsButton();
            }
        });

        panel.add(launchButton);
        panel.add(registerButton);
        panel.add(settingsButton);

        JLabel versionLabel = new JLabel(Settings.LAUNCHER_VERSION, SwingConstants.RIGHT);
        versionLabel.setBounds(600, 475, 190, 20);
        panel.add(versionLabel);

        SwingUtilities.invokeLater(new Runnable()
        {

            @Override
            public void run()
            {
                try
                {
                    JPanel newsPanel = new ImagePanel(ImageIO.read(Settings.NEWS_IMAGE_URL));
                    newsPanel.setBounds(28, 20, 525, 375);
                    panel.add(newsPanel);
                }
                catch (IOException e)
                {
                    Settings.LOGGER.catching(e);
                }
            }
        });

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
            JOptionPane.showMessageDialog(mainFrame, "ไม่สามารถเชื่อมต่อเซิร์ฟเวอร์ได้", "เกิดข้อผิดพลาด", JOptionPane.ERROR_MESSAGE);
        }

        if (!validAuth)
        {
            JOptionPane.showMessageDialog(mainFrame, "ชื่อผู้ใช้หรือรหัสผ่านไม่ถูกต้อง กรุณากรอกใหม่", "เกิดข้อผิดพลาด", JOptionPane.ERROR_MESSAGE);
            resetUI();
            passwordField.grabFocus();
            return;
        }

        settings.setPlayerName(usernameField.getText());
        saveSettings();

        statusLabel.setText("กำลังดาวน์โหลด");

        new Thread()
        {

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
                    Settings.LOGGER.catching(e);
                    JOptionPane.showMessageDialog(mainFrame, "ไม่สามารถดาวน์โหลดเกมได้ กรุณาลองใหม่ภายหลัง", "เกิดข้อผิดพลาด", JOptionPane.ERROR_MESSAGE);
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

    public void settingsButton()
    {
        LaunchSettingsUI ui = new LaunchSettingsUI(settings, mainFrame);
        ui.getDialog().setLocationRelativeTo(mainFrame);
        ui.start();
    }

    public void resetUI()
    {
        usernameField.setEnabled(true);
        passwordField.setEnabled(true);
        passwordField.setText("");

        statusProgressBar.setString("0%");
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

        HttpURLConnection connection = (HttpURLConnection) Settings.AUTHENTICATION_URL.openConnection();
        connection.setRequestMethod("POST");

        String param = "username=" + usernameField.getText() + "&password=" + URLEncoder.encode(new String(passwordField.getPassword()), StandardCharsets.UTF_8.name());
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

    public boolean downloadGame() throws IOException
    {
        List<String> whitelistFileList = new ArrayList<>();

        HttpURLConnection connection = (HttpURLConnection) Settings.WHITELIST_FILES_URL.openConnection();
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

        Map<String, String> remoteFiles = new HashMap<>();

        connection = (HttpURLConnection) Settings.COMPRESSED_FILES_LIST_URL.openConnection();
        connection.setRequestMethod("GET");

        br = new BufferedReader(new InputStreamReader(new GZIPInputStream(connection.getInputStream())));
        String fileName, hash;

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

        if (!Settings.GAME_DIRECTORY.exists() && !Settings.GAME_DIRECTORY.mkdirs())
        {
            Settings.LOGGER.error("Can't create " + Settings.GAME_DIRECTORY);
            JOptionPane.showMessageDialog(mainFrame, "ไม่สามารถสร้างโฟลเดอร์เกมได้", "ข้อผิดพลาด", JOptionPane.ERROR_MESSAGE);
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
                URL url = new URL(Settings.FILES_URL.toString() + name + ".gz");

                try
                {
                    URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
                    url = new URL(uri.toURL().toString().replace("#", "%23"));
                }
                catch (URISyntaxException e)
                {
                    Settings.LOGGER.catching(e);
                    /*JOptionPane.showMessageDialog(mainFrame, "ไม่สามารถสร้าง URI ได้", "เกิดข้อผิดพลาด", JOptionPane.ERROR_MESSAGE);
                    return false;*/
                }

                downloader = new GZIPFileDownloader(url, file);
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

                            String text = "กำลังดาวน์โหลด " + file.getName();
                            String progressBarText = overallPercentage + "%";

                            if (fileDownloaded > 0 && fileSize > 0)
                            {
                                progressBarText += ", " + Util.convertUnit(fileDownloaded) + "B/" + Util.convertUnit(fileSize) + "B";
                            }

                            statusLabel.setText(text);
                            statusProgressBar.setString(progressBarText);
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
                JOptionPane.showMessageDialog(mainFrame, "ไม่สามารถอ่านโฟลเดอร์เกมได้", "ข้อผิดพลาด", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            startWatchDirectoryChanges();

            String filePathRelativize;
            String filePathRelativizeURI;

            statusLabel.setText("กำลังตรวจสอบความถูกต้องของไฟล์เกม");

            for (File file : localFiles)
            {
                file = file.getAbsoluteFile();
                filePathRelativize = gameDirectoryPath.relativize(file.toPath()).toString();
                filePathRelativizeURI = filePathRelativize.replace(File.separatorChar, '/');

                if (whitelistFileList.contains(filePathRelativizeURI + (file.isDirectory() ? File.separator : "")))
                {
                    continue;
                }

                if (!remoteFiles.containsKey(filePathRelativizeURI))
                {
                    if (file.exists() && !FileUtil.deleteFileRescursive(file))
                    {
                        Settings.LOGGER.error("Can't delete " + file.toString());
                        JOptionPane.showMessageDialog(mainFrame, "ไม่สามารถลบไฟล์ได้ (" + filePathRelativizeURI + ")", "ข้อผิดพลาด", JOptionPane.ERROR_MESSAGE);
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

    public void updateSelf()
    {
        try
        {
            File workingJar = Settings.getWorkingJar();
            File tmpFile = File.createTempFile(workingJar.getName(), ".tmp");
            tmpFile.deleteOnExit();

            SelfUpdaterUI updaterUI = new SelfUpdaterUI(new URLFileDownloader(Settings.REMOTE_LAUNCHER_URL, tmpFile));
            updaterUI.start();

            if (!updaterUI.waitFor())
            {
                JOptionPane.showMessageDialog(null, "ไม่สามารถปรับปรุง Launcher ได้", "เกิดข้อผิดพลาด", JOptionPane.ERROR_MESSAGE);
                System.exit(-1);
            }

            InputStream in = new FileInputStream(tmpFile);
            OutputStream out = new FileOutputStream(workingJar);

            byte[] buffer = new byte[8192];
            int length;

            while ((length = in.read(buffer)) != -1)
            {
                out.write(buffer, 0, length);
            }

            out.close();
            in.close();

            ProcessBuilder pb = new ProcessBuilder();
            pb.directory(workingJar.getParentFile());
            pb.command(new File(System.getProperty("java.home"), "bin/java").getAbsolutePath(), "-jar", workingJar.getAbsolutePath());
            pb.start();

            System.exit(0);
        }
        catch (InterruptedException e)
        {
            Settings.LOGGER.catching(e);
            JOptionPane.showMessageDialog(null, "ไม่สามารถปรับปรุง Launcher ได้ (InterruptedException)", "เกิดข้อผิดพลาด", JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
        }
        catch (IOException e)
        {
            Settings.LOGGER.catching(e);
            JOptionPane.showMessageDialog(null, "ไม่สามารถปรับปรุง Launcher ได้ (IOException)", "เกิดข้อผิดพลาด", JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
        }
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
            JOptionPane.showMessageDialog(mainFrame, "ไม่มีข้อมูลการเปิดเกม กรุณาติดต่อผู้ดูแลระบบ", "เกิดข้อผิดพลาด", JOptionPane.ERROR_MESSAGE);
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
            JOptionPane.showMessageDialog(mainFrame, "ไม่สามารถอ่านข้อมูลในการเปิดเกมได้ กรุณาติดต่อผู้ดูแลระบบ", "เกิดข้อผิดพลาด", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        gameLauncher.addJVMArgument("-XX:HeapDumpPath=MojangTricksIntelDriversForPerformance_javaw.exe_minecraft.exe.heapdump");
        gameLauncher.addJVMArgument("-XX:+UseConcMarkSweepGC");
        gameLauncher.addJVMArgument("-XX:+CMSIncrementalMode");
        gameLauncher.addJVMArgument("-XX:-UseAdaptiveSizePolicy");

        gameLauncher.setMaxMemorySize(settings.getMaximumMemory());
        gameLauncher.setUserInformation(new UserInformation("1234", usernameField.getText(), "1234", "1234"));

        try
        {
            return gameLauncher.launch();
        }
        catch (Exception e)
        {
            Settings.LOGGER.catching(e);
            JOptionPane.showMessageDialog(mainFrame, "ไม่สามาถเปิดเกมได้", "เกิดข้อผิดพลาด", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public void startWatchDirectoryChanges()
    {

    }

    public void saveSettings()
    {
        try
        {
            FileWriter writer = new FileWriter(settingsFile);
            new Gson().toJson(settings, writer);
            writer.close();
        }
        catch (IOException e)
        {
            Settings.LOGGER.catching(e);
        }
    }
}
