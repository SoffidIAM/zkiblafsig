/*
 * Created on 24-ago-2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package es.caib.zkiblaf.applet;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextPane;

/**
 * @author u07286
 *
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class PinDialog extends JDialog {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * @return Returns the ok.
     */
    public final boolean isOk() {
        return ok;
    }

    /**
     * @return Returns the password.
     */
    public final char[] getPassword() {
        return password;
    }

    char[] password = null;
    private boolean ok = false;

    private javax.swing.JPanel jContentPane = null;

    private JLabel jLabel = null;
    private JLabel jCertificateLabel = null;
    private JPasswordField jPasswordField = null;
    private JPanel jPanel = null;
    private JButton jButton = null;
    private JButton jButton1 = null;
    private JTextPane jTextPane = null;
    private JCheckBox jCheckBox = null;
    private boolean selected = false;

    /**
     * This is the default constructor
     */
    public PinDialog() {
        super();
        initialize();
    }

    public final void setCertificate(final String cert) {
        jCertificateLabel.setText("Certificat: " + cert);
    }

    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize() {
        this.setTitle("Introduïu el PIN per a signar");
        this.setModal(true);
        this.setSize(438, 263);
        this.setContentPane(getJContentPane());
        centerWindow();
    }

    /**
     * This method initializes jContentPane
     * 
     * @return javax.swing.JPanel
     */
    private javax.swing.JPanel getJContentPane() {
        if (jContentPane == null) {
            BorderLayout borderLayout12 = new BorderLayout();
            jContentPane = new javax.swing.JPanel();
            jContentPane.setLayout(borderLayout12);

            borderLayout12.setHgap(80);
            borderLayout12.setVgap(80);

            jContentPane.add(getJPanel(), java.awt.BorderLayout.CENTER);
        }
        return jContentPane;
    }

    /**
     * This method initializes jPasswordField
     * 
     * @return javax.swing.JPasswordField
     */
    private JPasswordField getJPasswordField() {
        if (jPasswordField == null) {
            jPasswordField = new JPasswordField();
            jPasswordField.setColumns(18);
            jPasswordField.setHorizontalAlignment(javax.swing.JTextField.LEFT);
            jPasswordField.setText("");
            jPasswordField.setName("jPasswordField");
            jPasswordField.setToolTipText("Contrasenya de xarxa");
            jPasswordField.setPreferredSize(new java.awt.Dimension(250, 20));
            jPasswordField.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyTyped(final java.awt.event.KeyEvent e) {
                    if (e.getKeyChar() == '\33') {
                        setVisible(false);
                    }
                }
            });
            jPasswordField
                    .addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(final java.awt.event.ActionEvent e) {
                            doOk();
                        }
                    });
        }
        return jPasswordField;
    }

    /**
     * This method initializes jPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getJPanel() {
        if (jPanel == null) {
            jCertificateLabel = new JLabel();
            jCertificateLabel.setText("");
            jCertificateLabel.setFont(new java.awt.Font("Tahoma",
                    java.awt.Font.PLAIN, 12));
            jCertificateLabel.setName("jCertificateLabel");
            jCertificateLabel
                    .setVerticalTextPosition(javax.swing.SwingConstants.TOP);

            jLabel = new JLabel();
            jLabel.setText("PIN");
            jLabel
                    .setFont(new java.awt.Font("Tahoma", java.awt.Font.PLAIN,
                            12));
            jLabel.setName("jLabel");
            jLabel.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

            jCheckBox = new JCheckBox();
            jCheckBox.setSelected(false);
            jCheckBox.setText("Recordar durant mitja hora");

            java.awt.GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
            java.awt.GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
            java.awt.GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
            java.awt.GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
            java.awt.GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
            java.awt.GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
            jPanel = new JPanel();
            jPanel.setLayout(new GridBagLayout());
            gridBagConstraints4.gridx = 1;
            gridBagConstraints4.gridy = 0;
            gridBagConstraints4.insets = new java.awt.Insets(0, 40, 0, 0);
            gridBagConstraints4.weightx = 1.0D;
            gridBagConstraints4.anchor = java.awt.GridBagConstraints.WEST;

            gridBagConstraints5.gridx = 0;
            gridBagConstraints5.gridy = 1;
            gridBagConstraints5.insets = new java.awt.Insets(0, 40, 0, 0);
            gridBagConstraints5.weightx = 1.0D;
            gridBagConstraints5.anchor = java.awt.GridBagConstraints.WEST;

            gridBagConstraints6.gridx = 1;
            gridBagConstraints6.gridy = 1;
            gridBagConstraints6.weightx = 1.0D;
            gridBagConstraints6.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints6.ipadx = 0;
            gridBagConstraints6.ipady = 0;
            gridBagConstraints6.anchor = java.awt.GridBagConstraints.CENTER;
            gridBagConstraints6.weighty = 1.0D;
            gridBagConstraints6.gridheight = 1;
            gridBagConstraints6.insets = new java.awt.Insets(0, 0, 0, 40);

            gridBagConstraints7.gridx = 1;
            gridBagConstraints7.gridy = 1;
            gridBagConstraints7.weightx = 1.0D;
            gridBagConstraints7.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints7.ipadx = 0;
            gridBagConstraints7.ipady = 0;
            gridBagConstraints7.anchor = java.awt.GridBagConstraints.SOUTH;
            gridBagConstraints7.weighty = 1.0D;
            gridBagConstraints7.gridheight = 1;
            gridBagConstraints7.insets = new java.awt.Insets(0, 0, 0, 0);

            gridBagConstraints10.gridx = 1;
            gridBagConstraints10.gridy = 2;
            gridBagConstraints10.anchor = java.awt.GridBagConstraints.EAST;
            gridBagConstraints10.ipadx = 0;
            gridBagConstraints10.insets = new java.awt.Insets(20, 20, 40, 40);

            gridBagConstraints11.gridx = 1;
            gridBagConstraints11.gridy = 2;
            gridBagConstraints11.anchor = java.awt.GridBagConstraints.WEST;
            gridBagConstraints11.insets = new java.awt.Insets(20, 0, 40, 0);

            jPanel.setBackground(java.awt.SystemColor.control);
            jPanel.add(jCertificateLabel, gridBagConstraints4);
            jPanel.add(jLabel, gridBagConstraints5);
            jPanel.add(getJPasswordField(), gridBagConstraints6);
            jPanel.add(jCheckBox, gridBagConstraints7);
            jPanel.add(getJButton1(), gridBagConstraints10);
            jPanel.add(getJButton(), gridBagConstraints11);
            jPanel.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyTyped(final java.awt.event.KeyEvent e) {
                    if (e.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
                        doOk();
                    }
                }

            });
        }
        return jPanel;
    }

    /**
     * This method initializes jButton
     * 
     * @return javax.swing.JButton
     */
    private JButton getJButton() {
        if (jButton == null) {
            jButton = new JButton();
            jButton.setText("Cancel·lar");
            jButton.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(final java.awt.event.ActionEvent e) {
                    ok = false;
                    PinDialog.this.setVisible(false);
                }
            });
        }
        return jButton;
    }

    /**
     * This method initializes jButton1
     * 
     * @return javax.swing.JButton
     */
    private JButton getJButton1() {
        if (jButton1 == null) {
            jButton1 = new JButton();
            jButton1.setText("Acceptar");
            jButton1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
            jButton1.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(final java.awt.event.ActionEvent e) {
                    doOk();
                }
            });
        }
        return jButton1;
    }

    private void doOk() {
        password = jPasswordField.getPassword();
        ok = true;
        selected = jCheckBox.isSelected();
        setVisible(false);
    }

    private void centerWindow() {
        Rectangle screen = new Rectangle(Toolkit.getDefaultToolkit()
                .getScreenSize());
        Point center = new Point((int) screen.getCenterX(), (int) screen
                .getCenterY());
        Point newLocation = new Point(center.x - this.getWidth() / 2, center.y
                - this.getHeight() / 2);
        if (screen.contains(newLocation.x, newLocation.y, this.getWidth(), this
                .getHeight())) {
            this.setLocation(newLocation);
        }
    } // centerWindow()

    public static char[] getPIN() {
        PinDialog dialog = new PinDialog();
        dialog.setVisible(true);
        return dialog.getPassword();
    }

    public final boolean isSelected() {
        return selected;
    }
}
