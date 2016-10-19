package com.lion328.hydra;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.text.NumberFormatter;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

public class LaunchSettingsUI
{

    private JDialog dialog;
    private JFrame mainFrame;
    private PlayerSettings playerSettings;

    public LaunchSettingsUI(PlayerSettings playerSettings, JFrame mainFrame)
    {
        this.playerSettings = playerSettings;
        this.mainFrame = mainFrame;

        init();
    }

    private void init()
    {
        NumberFormatter formatter = new NumberFormatter(NumberFormat.getIntegerInstance());
        formatter.setValueClass(Integer.class);
        formatter.setAllowsInvalid(false);
        formatter.setMinimum(512);

        SpinnerNumberModel spinnerNumberModel = new SpinnerNumberModel();
        spinnerNumberModel.setMinimum(512);
        spinnerNumberModel.setStepSize(1);
        spinnerNumberModel.setValue(playerSettings.getMaximumMemory());

        final JSpinner spinner = new JSpinner(spinnerNumberModel);

        JButton okButton = new JButton(Language.get("accept"));
        okButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                playerSettings.setMaximumMemory((int) spinner.getValue());
                dialog.setVisible(false);
            }
        });

        JButton cancelButton = new JButton(Language.get("cancel"));
        cancelButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                dialog.setVisible(false);
            }
        });

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 5, 5, 5);

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.fill = GridBagConstraints.BOTH;
        panel.add(new JLabel(Language.get("memoryAllocAmountMiB")), constraints);

        constraints.gridx = 1;
        constraints.gridwidth = 2;
        panel.add(spinner, constraints);

        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        panel.add(okButton, constraints);

        constraints.gridx = 2;
        panel.add(cancelButton, constraints);

        dialog = new JDialog(mainFrame, Language.get("settingsTitle"), true);
        dialog.setLocationRelativeTo(null);
        dialog.setContentPane(panel);
        dialog.getRootPane().setDefaultButton(okButton);
        dialog.pack();
    }

    public void start()
    {
        dialog.setVisible(true);
    }

    public JDialog getDialog()
    {
        return dialog;
    }

    public PlayerSettings getPlayerSettings()
    {
        return playerSettings;
    }
}
