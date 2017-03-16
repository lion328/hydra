package com.lion328.hydra;

import com.lion328.xenonlauncher.minecraft.logging.CrashReportHandler;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class CrashReportUI implements CrashReportHandler
{

    private JFrame frame;
    private JTextArea reportText;
    private boolean running;

    public CrashReportUI()
    {
        running = false;

        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                init();
            }
        });
    }

    public synchronized boolean isRunning()
    {
        return running;
    }

    private void init()
    {
        reportText = new JTextArea();
        reportText.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        reportText.setLineWrap(true);
        reportText.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(reportText);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(800, 450));

        frame = new JFrame();
        frame.add(scrollPane);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.addWindowListener(new WindowAdapter()
        {

            @Override
            public void windowClosing(WindowEvent windowEvent)
            {
                running = false;
            }
        });
    }

    @Override
    public void onGameCrash(final File crashReportFile)
    {
        String report;

        try
        {
            report = new String(Files.readAllBytes(crashReportFile.toPath()), StandardCharsets.UTF_8);
        }
        catch (IOException e)
        {
            HydraLauncher.getLogger().catching(e);
            report = "Can't read crash report file: " + crashReportFile.getAbsolutePath() + "\n" + e.getMessage();
        }

        final String finalReport = report;

        try
        {
            SwingUtilities.invokeAndWait(new Runnable()
            {

                @Override
                public void run()
                {
                    reportText.setText(finalReport);
                    reportText.setCaretPosition(0);

                    frame.setTitle("Minecraft Crash Report (" + crashReportFile.getName() + ")");
                    frame.setVisible(true);

                    running = true;
                }
            });
        }
        catch (InterruptedException | InvocationTargetException e)
        {
            HydraLauncher.getLogger().catching(e);
        }

        while (true)
        {
            if (!isRunning())
            {
                break;
            }
        }
    }
}
