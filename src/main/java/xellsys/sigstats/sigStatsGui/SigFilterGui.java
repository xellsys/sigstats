/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xellsys.sigstats.sigStatsGui;

import xellsys.sigstats.sharedEntities.SigMisc;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author xellsys
 */
public class SigFilterGui extends JFrame {

    private JTextField t_num;
    private JTextField t_hero;
    private JTextField t_mode;
    private JCheckBox b_dz;
    private JCheckBox b_val;
    private JComboBox c_sigvip;
    private JComboBox c_minlvl;
    private JComboBox c_maxlvl;

    public SigFilterGui() {

        setTitle("SigStats " + SigMisc.version + " Filter");
        setSize(300, 300);
        setMinimumSize(this.getSize());
        setLocationRelativeTo(null);

        initUI();

        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    }

    private void initUI() {

        JPanel panel = new JPanel();

        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel.setLayout(new GridLayout(9, 2, 10, 10));


        panel.add(newLabel("Last # games:"));
        t_num = newJTextField("", "Will reduce your result to this number of your last games.");
        panel.add(t_num);

        panel.add(newLabel("Only DZ?"));
        b_dz = new JCheckBox();
        panel.add(b_dz);

        panel.add(newLabel("Only Valid?"));
        b_val = new JCheckBox();
        panel.add(b_val);

        panel.add(newLabel("SIG or VIP?"));
        c_sigvip = new JComboBox();
        c_sigvip.addItem("Both");
        c_sigvip.addItem("Only SIG");
        c_sigvip.addItem("Only VIP");
        c_sigvip.setSelectedIndex(0);
        panel.add(c_sigvip);

        panel.add(newLabel("Only Hero:"));
        t_hero = newJTextField("", "Allow only games with this hero.");
        panel.add(t_hero);

        panel.add(newLabel("Only Mode:"));
        t_mode = newJTextField("", "Allow only games with this mode.");
        panel.add(t_mode);

        panel.add(newLabel("Min Level:"));
        c_minlvl = new JComboBox();
        for (int i = 1; i < 26; ++i) {
            c_minlvl.addItem(i + "");
        }
        c_minlvl.setSelectedIndex(0);
        panel.add(c_minlvl);

        panel.add(newLabel("Max Level:"));
        c_maxlvl = new JComboBox();
        for (int i = 1; i < 26; ++i) {
            c_maxlvl.addItem(i + "");
        }
        c_maxlvl.setSelectedIndex(24);
        panel.add(c_maxlvl);

        JButton b_disc = new JButton("Discard");
        panel.add(b_disc);

        JButton b_apply = new JButton("Apply");
        panel.add(b_apply);

        b_apply.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                apply();
                setVisible(false);
            }
        });
        b_disc.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });

        add(panel);
    }

    private void apply() {
        if (!SigFilter.guiRdy)
            return;
        //SIG or VIP filter
        if (c_sigvip.getSelectedItem().equals("Both")) {
            SigFilter.SIGorVIP = 0;
        } else if (c_sigvip.getSelectedItem().equals("SIG")) {
            SigFilter.SIGorVIP = 1;
        } else if (c_sigvip.getSelectedItem().equals("VIP")) {
            SigFilter.SIGorVIP = 2;
        } else {
            //error?
        }
        //Level
        try {
            SigFilter.minLvl = Integer.parseInt((String) c_minlvl.getSelectedItem());
        } catch (NumberFormatException ignored) {
        }
        try {
            SigFilter.maxLvl = Integer.parseInt((String) c_maxlvl.getSelectedItem());
        } catch (NumberFormatException ignored) {
        }
        //Hero
        SigFilter.onlyHero = t_hero.getText();
        //Mode
        SigFilter.onlyMode = t_mode.getText();
        //Valid
        SigFilter.onlyValid = b_val.isSelected();
        //DZ
        SigFilter.onlyDZ = b_dz.isSelected();
        //Last Games
        try {
            SigFilter.num = Integer.parseInt(t_num.getText());
        } catch (NumberFormatException ignored) {
        }
        //rdy
        SigFilter.rdy = true;
    }

    private JLabel newLabel(String s) {
        return new JLabel(s);
    }

    private JButton newJButton(String s) {
        return new JButton(s);
    }

    private JTextField newJTextField(String s, String tt) {
        JTextField display = new JTextField(s);
        display.setEditable(true);
        display.setBackground(new Color(255, 255, 255));
        display.setToolTipText(tt);
        return display;
    }
}
