package net.mc_warrior.launcher;

import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.NumberFormatter;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.NumberFormat;

public class LaunchSettingsUI
{

    private JFrame frame;
    private PlayerSettings playerSettings;

    public LaunchSettingsUI(PlayerSettings playerSettings)
    {
        this.playerSettings = playerSettings;
    }

    public void start()
    {
        NumberFormatter formatter = new NumberFormatter(NumberFormat.getIntegerInstance());
        formatter.setValueClass(Integer.class);
        formatter.setAllowsInvalid(false);
        formatter.setMaximum(0);

        JFormattedTextField memoryTextField = new JFormattedTextField();


        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10);

        constraints.gridx = 0;
        constraints.gridy = 0;
        panel.add(new JLabel("หน่วยความจำที่ใช้ (MiB)"), constraints);

        constraints.gridx = 1;
        //panel.add();

        JFrame frame = new JFrame();
        frame.setLocationRelativeTo(null);
        frame.setContentPane(panel);
        frame.pack();
        frame.setVisible(true);
    }

    public JFrame getJFrame()
    {
        return frame;
    }

    public PlayerSettings getPlayerSettings()
    {
        return playerSettings;
    }
}
