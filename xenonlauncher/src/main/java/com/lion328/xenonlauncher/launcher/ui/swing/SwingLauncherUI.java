package com.lion328.xenonlauncher.launcher.ui.swing;

import com.lion328.xenonlauncher.i18n.I18n;
import com.lion328.xenonlauncher.launcher.Launcher;
import com.lion328.xenonlauncher.launcher.ui.LauncherUI;
import com.lion328.xenonlauncher.launcher.ui.swing.data.Browser;
import com.lion328.xenonlauncher.launcher.ui.swing.data.Button;
import com.lion328.xenonlauncher.launcher.ui.swing.data.Component;
import com.lion328.xenonlauncher.launcher.ui.swing.data.Font;
import com.lion328.xenonlauncher.launcher.ui.swing.data.Frame;
import com.lion328.xenonlauncher.launcher.ui.swing.data.Label;
import com.lion328.xenonlauncher.launcher.ui.swing.data.Panel;
import com.lion328.xenonlauncher.launcher.ui.swing.data.PasswordField;
import com.lion328.xenonlauncher.launcher.ui.swing.data.ProgressBar;
import com.lion328.xenonlauncher.launcher.ui.swing.data.TextField;
import com.lion328.xenonlauncher.settings.LauncherConstant;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.awt.Desktop;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class SwingLauncherUI implements LauncherUI
{

    private Frame frame;
    private Map<String, java.awt.Component> components;
    private Map<URL, Image> images;
    private JFrame jFrame;
    private Launcher launcher;

    private boolean disableLogin;
    private boolean finishUpdateStatus;

    public SwingLauncherUI(Frame frame) throws IOException
    {
        this.frame = frame;

        components = new HashMap<>();
        images = new HashMap<>();

        disableLogin = false;
        finishUpdateStatus = true;

        generateJFrame();
        registerBehavior();
    }

    private void generateJFrame() throws IOException
    {
        jFrame = new JFrame();
        jFrame.setSize(frame.getWidth(), frame.getHeight());
        jFrame.setUndecorated(!frame.isDecorated());

        if (frame.getX() < 0 || frame.getY() < 0)
        {
            jFrame.setLocationRelativeTo(null);
        }
        else
        {
            jFrame.setLocation(frame.getX(), frame.getY());
        }

        components.put(jFrame.getName(), jFrame);

        if (frame.getPanel() == null)
        {
            return;
        }

        JPanel jPanel = parseJPanel(frame.getPanel());

        jFrame.setContentPane(jPanel);
    }

    private JPanel parseJPanel(Panel panel) throws IOException
    {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(null);

        Font defaultFont = frame.getPanel().getFont();

        java.awt.Component swingComponent;

        for (Component component : frame.getPanel().getChildren())
        {
            swingComponent = null;

            if (component instanceof Button)
            {
                Button button = (Button) component;

                URL imageURL = button.getImageURL();
                URL hoverURL = button.getImageHoverURL();

                if (button.getImageHoverURL() != null)
                {
                    images.put(imageURL, ImageIO.read(imageURL));
                }

                if (button.getImageHoverURL() != null)
                {
                    try
                    {
                        images.put(imageURL, ImageIO.read(imageURL));
                    }
                    catch (IOException unused)
                    {
                        hoverURL = imageURL;
                    }
                }
                else
                {
                    hoverURL = imageURL;
                }

                final Icon imageIcon = new ImageIcon(images.get(imageURL));
                final Icon hoverIcon = new ImageIcon(images.get(hoverURL));

                final JLabel buttonLabel = new JLabel();
                buttonLabel.setIcon(imageIcon);
                buttonLabel.setBounds(button.getX(), button.getY(), button.getWidth(), button.getHeight());

                buttonLabel.addMouseListener(new MouseAdapter()
                {

                    @Override
                    public void mouseEntered(MouseEvent e)
                    {
                        buttonLabel.setIcon(hoverIcon);
                    }

                    @Override
                    public void mouseExited(MouseEvent e)
                    {
                        buttonLabel.setIcon(imageIcon);
                    }
                });

                swingComponent = buttonLabel;
            }
            else if (component instanceof Label)
            {
                Label label = (Label) component;

                Font font;

                if (label.getFont() != null)
                {
                    font = label.getFont();
                }
                else
                {
                    font = defaultFont;
                }

                JLabel jLabel = new JLabel();
                jLabel.setText(label.getDefaultText());
                jLabel.setBounds(label.getX(), label.getY(), label.getWidth(), label.getHeight());
                jLabel.setFont(new java.awt.Font(font.getFamily(), java.awt.Font.PLAIN, font.getSize()));
                jLabel.setText(label.getDefaultText());

                swingComponent = jLabel;
            }
            else if (component instanceof Panel)
            {
                swingComponent = parseJPanel((Panel) component);
            }
            else if (component instanceof TextField)
            {
                TextField textField = (TextField) component;

                JTextField jTextField;

                switch (component.getType())
                {
                    case PasswordField.TYPE:
                        jTextField = new JPasswordField();
                        break;
                    default:
                        jTextField = new JTextField();
                        break;
                }

                jTextField.setBounds(textField.getX(), textField.getY(), textField.getWidth(), textField.getHeight());
                jTextField.setText(textField.getDefaultText());

                swingComponent = jTextField;
            }
            else if (component instanceof ProgressBar)
            {
                ProgressBar progressBar = (ProgressBar) component;

                JProgressBar jProgressBar = new JProgressBar();
                jProgressBar.setBounds(progressBar.getX(), progressBar.getY(), progressBar.getWidth(), progressBar.getHeight());

                swingComponent = jProgressBar;
            }
            else if (component instanceof Browser)
            {
                Browser browser = (Browser) component;

                JEditorPane jEditorPane = new JEditorPane();
                jEditorPane.setBounds(browser.getX(), browser.getY(), browser.getWidth(), browser.getHeight());
                jEditorPane.setEditorKit(JEditorPane.createEditorKitForContentType("text/html"));
                jEditorPane.setEditable(false);

                if (browser.getURL() != null)
                {
                    jEditorPane.setPage(browser.getURL());

                    jEditorPane.addHyperlinkListener(new HyperlinkListener()
                    {
                        @Override
                        public void hyperlinkUpdate(HyperlinkEvent event)
                        {
                            if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
                            {
                                if (Desktop.isDesktopSupported())
                                {
                                    try
                                    {
                                        Desktop.getDesktop().browse(event.getURL().toURI());
                                    }
                                    catch (Exception e)
                                    {
                                        LauncherConstant.LOGGER.catching(e);
                                    }
                                }
                            }
                        }
                    });
                }

                swingComponent = jEditorPane;
            }

            if (swingComponent != null)
            {
                if (!components.containsKey(component.getId()))
                {
                    components.put(component.getId(), swingComponent);
                }

                jPanel.add(swingComponent);
            }
        }

        components.put(panel.getId(), jPanel);

        return jPanel;
    }

    private void registerBehavior()
    {
        java.awt.Component tmp0 = components.get("loginButton");
        java.awt.Component tmp1 = components.get("usernameField");
        java.awt.Component tmp2 = components.get("passwordField");

        if (tmp0 != null && tmp1 != null)
        {
            if (tmp1 instanceof JTextField && tmp2 instanceof JPasswordField)
            {
                final JTextField usernameField = (JTextField) tmp1;
                final JPasswordField passwordField = (JPasswordField) tmp2;

                tmp0.addMouseListener(new MouseAdapter()
                {
                    @Override
                    public void mouseClicked(MouseEvent e)
                    {
                        if (disableLogin)
                        {
                            return;
                        }

                        usernameField.setEnabled(false);
                        passwordField.setEnabled(false);

                        new SwingWorker<Void, Void>()
                        {

                            @Override
                            protected Void doInBackground() throws Exception
                            {
                                disableLogin = launcher.loginAndLaunch(usernameField.getText(), passwordField.getPassword());
                                return null;
                            }

                            @Override
                            protected void done()
                            {
                                if (!disableLogin)
                                {
                                    // If login was not successful
                                    usernameField.setEnabled(true);
                                    passwordField.setEnabled(true);
                                }
                            }
                        }.execute();
                    }
                });
            }
        }
    }

    @Override
    public void start()
    {
        setVisible(true);
    }

    @Override
    public Launcher getLauncher()
    {
        return launcher;
    }

    @Override
    public void setLauncher(Launcher launcher)
    {
        this.launcher = launcher;
    }

    @Override
    public boolean isVisible()
    {
        return jFrame.isVisible();
    }

    @Override
    public void setVisible(boolean visible)
    {
        jFrame.setVisible(true);
    }

    @Override
    public void displayError(String message)
    {
        JOptionPane.showMessageDialog(jFrame, message, I18n.get("common.error"), JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void onPercentageChange(final File file, final int overallPercentage, final long fileSize, final long fileDownloaded)
    {
        if (!finishUpdateStatus)
        {
            return;
        }

        new SwingWorker<Void, Void>()
        {

            @Override
            protected Void doInBackground() throws Exception
            {
                finishUpdateStatus = false;
                return null;
            }

            @Override
            protected void done()
            {
                java.awt.Component component = components.get("overallProgressBar");

                if (component != null && component instanceof JProgressBar)
                {
                    ((JProgressBar) component).setValue(overallPercentage % 100);
                }

                component = components.get("statusLabel");

                if (component != null && component instanceof JLabel)
                {
                    String s = String.format(I18n.get("launcher.ui.status"), file.getName(), overallPercentage, fileDownloaded, fileSize);
                    ((JLabel) component).setText(s);
                }

                finishUpdateStatus = true;
            }
        }.execute();
    }
}
