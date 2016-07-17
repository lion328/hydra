package net.mc_warrior.launcher;

import com.lion328.xenonlauncher.downloader.Downloader;
import com.lion328.xenonlauncher.downloader.DownloaderCallback;
import com.lion328.xenonlauncher.downloader.URLFileDownloader;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.text.NumberFormatter;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;

public class SelfUpdaterUI
{

    private Downloader downloader;
    private JProgressBar progressBar;
    private Thread thread;
    private JFrame frame;
    private boolean state;
    private boolean forceStop;

    public SelfUpdaterUI(Downloader downloader)
    {
        this.downloader = downloader;

        init();
    }

    private void init()
    {
        forceStop = false;

        progressBar = new JProgressBar();
        progressBar.setPreferredSize(new Dimension(400, 25));
        progressBar.setStringPainted(true);
        progressBar.setValue(0);

        JLabel label = new JLabel("กำลังดาวน์โหลด...");
        label.setForeground(Color.LIGHT_GRAY);

        JButton cancelButton = new JButton("ยกเลิก");
        cancelButton.addMouseListener(new MouseAdapter()
        {

            @Override
            public void mouseClicked(MouseEvent e)
            {
                stop();
            }
        });

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(Color.DARK_GRAY);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 2;
        panel.add(progressBar, constraints);

        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.anchor = GridBagConstraints.WEST;
        panel.add(label, constraints);

        constraints.gridx = 1;
        constraints.anchor = GridBagConstraints.EAST;
        panel.add(cancelButton, constraints);

        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        frame.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                stop();
            }
        });

        frame.setLocationRelativeTo(null);
        frame.setSize(400, 100);
        frame.setContentPane(panel);
        frame.setResizable(false);
        frame.setTitle("MC-Warrior - กำลังปรับปรุง Launcher");
        frame.pack();
        frame.setVisible(true);

        downloader.registerCallback(new DownloaderCallback()
        {

            private boolean updatingStatus = false;

            @Override
            public void onPercentageChange(File file, final int overallPercentage, final long fileSize, final long fileDownloaded)
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
                        progressBar.setValue(overallPercentage);
                        progressBar.setString(overallPercentage + "%, " + Util.convertUnit(fileDownloaded) + "B/" + Util.convertUnit(fileSize) + "B");
                    }
                });
            }
        });
    }

    public void start()
    {
        thread = new Thread()
        {

            @Override
            public void run()
            {
                try
                {
                    downloader.download();
                }
                catch (IOException e)
                {
                    Settings.LOGGER.catching(e);
                    JOptionPane.showMessageDialog(null, "ไม่สามารถปรับปรุง Launcher ได้", "เกิดข้อผิดพลาด", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                frame.setVisible(false);
                state = !forceStop;
            }
        };

        thread.start();
    }

    public void stop()
    {
        if (!isRunning())
        {
            return;
        }

        forceStop = true;
        downloader.stop();
    }

    public JFrame getFrame()
    {
        return frame;
    }

    public boolean isRunning()
    {
        return downloader.isRunning();
    }

    public boolean waitFor() throws InterruptedException
    {
        thread.join();
        boolean state = this.state;
        this.state = false;
        return state;
    }
}
