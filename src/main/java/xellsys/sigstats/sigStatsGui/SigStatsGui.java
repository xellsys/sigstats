/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xellsys.sigstats.sigStatsGui;

/**
 *
 * @author xellsys
 */


import xellsys.sigstats.sharedEntities.SigGame;
import xellsys.sigstats.sharedEntities.SigMisc;
import xellsys.sigstats.sharedEntities.SigStatistic;
import xellsys.sigstats.sigStatsBL.SigStatsBL;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class SigStatsGui extends JFrame implements Observer {

    public boolean done = true;
    private JPanel panel;
    private SigStatsBL BL;
    private SigFilterGui SFG;
    private List<SigGame> sgl = null;
    private SigStatistic ss = new SigStatistic();
    private JProgressBar pg;
    private JTextField t_id;
    private JTextField t_first;
    private JTextField t_last;
    private JTextField t_all;
    private JTextField t_val;
    private JTextField t_dz;
    private JTextField t_valSig;
    private JTextField t_valVIP;
    private JTextField t_avgLvl;
    private JTextField t_valWin;
    private JTextField t_valWinP;
    private JTextField t_sigWin;
    private JTextField t_sigWinP;
    private JTextField t_vipWin;
    private JTextField t_vipWinP;
    private JTextField t_valLose;
    private JTextField t_valLoseP;
    private JTextField t_sigLose;
    private JTextField t_sigLoseP;
    private JTextField t_vipLose;
    private JTextField t_vipLoseP;
    private JTextField t_valLeave;
    private JTextField t_valLeaveP;
    private JTextField t_sigLeave;
    private JTextField t_sigLeaveP;
    private JTextField t_vipLeave;
    private JTextField t_vipLeaveP;
    private JTextField t_valClose;
    private JTextField t_valCloseP;
    private JTextField t_sigClose;
    private JTextField t_sigCloseP;
    private JTextField t_vipClose;
    private JTextField t_vipCloseP;
    private JTextField t_mostHero;
    private JTextField t_mostHeroC;
    private JTextField t_mostMode;
    private JTextField t_mostModeC;
    private JTextField t_mostWinHero;
    private JTextField t_mostWinHeroC;
    private JTextField t_mostWinMode;
    private JTextField t_mostWinModeC;
    private JTextField t_mostLoseHero;
    private JTextField t_mostLoseHeroC;
    private JTextField t_mostLoseMode;
    private JTextField t_mostLoseModeC;
    private JButton b_update;
    private JButton b_save;
    private JButton b_loadAll;
    private JButton b_filter;
    private JButton b_copy;
    private JButton b_loadLocal;

    public SigStatsGui() throws Throwable {
        BL = new SigStatsBL();
        SFG = new SigFilterGui();

        BL.addObserver(this);

        setTitle("SigStats " + SigMisc.version);
        setSize(900, 400);
        this.setMinimumSize(this.getSize());
        this.setResizable(false);
        setLocationRelativeTo(null);

        initUI();

        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    // <editor-fold defaultstate="collapsed" desc="initUI">
    public void initUI() {

        panel = new JPanel();

        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel.setLayout(new GridLayout(12, 6, 5, 5));

        String[] labs = {
                "First Game", "Last Game", "All Games", "Valid Games", "DZ Games", "Valid SIG", "Valid VIP", "AVG level", "Valid",
                "SIG", "VIP", "Win", "Lose", "Leave", "Close", "Most Hero", "Most Mode", "Most wins with", "Most wins in", "Most loses with",
                "Most loses in"
        };

        this.addWindowFocusListener(new WindowFocusListener() {

            public void windowGainedFocus(WindowEvent e) {
                if (!SFG.isVisible()) {
                    if (SigFilter.rdy) {
                        System.out.println("GUI.gainfocus: sfg not visible && filter is rdy");
                        applyFilter();
                    }
                    System.out.println("GUI.gainfocus: sfg not visible, but filter is NOT rdy");
                    setEnabled(true);
                } else {
                    System.out.println("GUI.gainfocus: sfg is visible");
                    //setEnabled(false);
                    //toBack();
                }
            }

            public void windowLostFocus(WindowEvent e) {
            }
        });

        //pre row
        panel.add(newLabel("Enter ID"));
        t_id = newText("", "Please enter the DL account's ID.");
        t_id.setEditable(true);
        panel.add(t_id);
        //load all
        b_loadAll = new JButton("Download All");
        b_loadAll.setToolTipText("Download all of your games. I recommend saving them and afterwards apply filters.");
        b_loadAll.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    loadAll();
                } catch (Throwable ex) {
                    System.out.println("submit error: " + ex.getMessage() + "\n" + ex + "\n" + ex.getLocalizedMessage() + "\n");
                    ex.printStackTrace();
                }
            }
        });
        panel.add(b_loadAll);
        b_update = new JButton("Update Games");
        b_update.setToolTipText("Update your current statistics. Depending on the currently entered ID. Do not mix up!");
        b_update.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    updateGames();
                } catch (Throwable ex) {
                    System.out.println("submit error: " + ex.getMessage() + "\n" + ex + "\n" + ex.getLocalizedMessage() + "\n");
                    ex.printStackTrace();
                }
            }
        });
        panel.add(b_update);
        b_loadLocal = new JButton("Load Local");
        b_loadLocal.setToolTipText("Load statistics locally for currently entered ID");
        b_loadLocal.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    loadLocal();
                } catch (Throwable ex) {
                    System.out.println("submit error: " + ex.getMessage() + "\n" + ex + "\n" + ex.getLocalizedMessage() + "\n");
                    ex.printStackTrace();
                }
            }
        });
        panel.add(b_loadLocal);
        b_save = new JButton("Save");
        b_save.setToolTipText("Locally save your current(!) statistics. I recommend saving all of your games after first download.");
        b_save.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    save();
                } catch (Throwable ex) {
                    System.out.println("submit error: " + ex.getMessage() + "\n" + ex + "\n" + ex.getLocalizedMessage() + "\n");
                    ex.printStackTrace();
                }
            }
        });
        panel.add(b_save);
        b_filter = new JButton("Filter");
        b_filter.setToolTipText("Apply filters to current statistic's view. Attention: Filters work additive!");
        b_filter.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    javax.swing.SwingUtilities.invokeLater(new Runnable() {

                        public void run() {
                            SFG = new SigFilterGui();
                            SFG.setVisible(true);
                        }
                    });
                } catch (Throwable ex) {
                    System.out.println("submit error: " + ex.getMessage() + "\n" + ex + "\n" + ex.getLocalizedMessage() + "\n");
                    ex.printStackTrace();
                }
            }
        });
        panel.add(b_filter);
        //1st row
        panel.add(newLabel(labs[0]));
        t_first = newText("", "");
        panel.add(t_first);
        panel.add(newLabel(labs[1]));
        t_last = newText("", "");
        panel.add(t_last);
        //progress bar
        pg = new JProgressBar();
        panel.add(pg);
        panel.add(newLabel(""));
        panel.add(newLabel(""));
        //2
        panel.add(newLabel(labs[2]));
        t_all = newText("", "Number of all games listed");
        panel.add(t_all);
        panel.add(newLabel(labs[3]));
        t_val = newText("", "Number of only valid games (no closed)");
        panel.add(t_val);
        panel.add(newLabel(labs[4]));
        t_dz = newText("", "Number of only DZ games (valid & invalid)");
        panel.add(t_dz);
        panel.add(newLabel(""));
        //3
        panel.add(newLabel(labs[5]));
        t_valSig = newText("", "Number of valid SIG games");
        panel.add(t_valSig);
        panel.add(newLabel(labs[6]));
        t_valVIP = newText("", "Number of valid VIP games");
        panel.add(t_valVIP);
        panel.add(newLabel(labs[7]));
        t_avgLvl = newText("", "Number of average Level in all DZ games");
        panel.add(t_avgLvl);
        b_copy = new JButton("Copy Statistic");
        b_copy.setToolTipText("Copy current statistic's view into clipboard. Paste with ctrl+v");
        b_copy.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    copyToClip();
                } catch (Throwable ex) {
                    System.out.println("submit error: " + ex.getMessage() + "\n" + ex + "\n" + ex.getLocalizedMessage() + "\n");
                    ex.printStackTrace();
                }
            }
        });
        panel.add(b_copy);
        //4
        panel.add(newLabel(""));
        panel.add(newLabel(labs[8]));
        panel.add(newLabel(""));
        panel.add(newLabel(labs[9]));
        panel.add(newLabel(""));
        panel.add(newLabel(labs[10]));
        panel.add(newLabel(""));
        //5
        panel.add(newLabel(labs[11]));
        t_valWin = newText("", "Number of wins in valid games");
        panel.add(t_valWin);
        t_valWinP = newText("", "Percentage of wins in valid games");
        panel.add(t_valWinP);
        t_sigWin = newText("", "Number of wins in SIG games");
        panel.add(t_sigWin);
        t_sigWinP = newText("", "Percentage of wins in SIG games");
        panel.add(t_sigWinP);
        t_vipWin = newText("", "Number of wins in VIP games");
        panel.add(t_vipWin);
        t_vipWinP = newText("", "Percentage of wins in VIP games");
        panel.add(t_vipWinP);
        //6
        panel.add(newLabel(labs[12]));
        t_valLose = newText("", "Number of loses in valid games");
        panel.add(t_valLose);
        t_valLoseP = newText("", "Percentage of loses in valid games");
        panel.add(t_valLoseP);
        t_sigLose = newText("", "Number of loses in SIG games");
        panel.add(t_sigLose);
        t_sigLoseP = newText("", "Percentage of loses in SIG games");
        panel.add(t_sigLoseP);
        t_vipLose = newText("", "Number of loses in VIP games");
        panel.add(t_vipLose);
        t_vipLoseP = newText("", "Percentage of loses in VIP games");
        panel.add(t_vipLoseP);
        //7
        panel.add(newLabel(labs[13]));
        t_valLeave = newText("", "Number of leaves in valid games");
        panel.add(t_valLeave);
        t_valLeaveP = newText("", "Percentage of leaves in valid games");
        panel.add(t_valLeaveP);
        t_sigLeave = newText("", "Number of leaves in SIG games");
        panel.add(t_sigLeave);
        t_sigLeaveP = newText("", "Percentage of leaves in SIG games");
        panel.add(t_sigLeaveP);
        t_vipLeave = newText("", "Number of leaves in VIP games");
        panel.add(t_vipLeave);
        t_vipLeaveP = newText("", "Percentage of leaves in VIP games");
        panel.add(t_vipLeaveP);
        //8
        panel.add(newLabel(labs[14]));
        t_valClose = newText("", "Number of closes in all(!) games");
        panel.add(t_valClose);
        t_valCloseP = newText("", "Percentage of leaves in all(!) games");
        panel.add(t_valCloseP);
        t_sigClose = newText("", "Number of closes in SIG games");
        panel.add(t_sigClose);
        t_sigCloseP = newText("", "Percentage of closes in SIG games");
        panel.add(t_sigCloseP);
        t_vipClose = newText("", "Number of closes in VIP games");
        panel.add(t_vipClose);
        t_vipCloseP = newText("", "Percentage of closes in VIP games");
        panel.add(t_vipCloseP);
        //9
        panel.add(newLabel(labs[15]));
        t_mostHero = newText("", "Most played hero");
        panel.add(t_mostHero);
        t_mostHeroC = newText("", "Number of times played");
        panel.add(t_mostHeroC);
        panel.add(newLabel(""));
        panel.add(newLabel(labs[16]));
        t_mostMode = newText("", "Most played mode");
        panel.add(t_mostMode);
        t_mostModeC = newText("", "Number of times played");
        panel.add(t_mostModeC);
        //10
        panel.add(newLabel(labs[17]));
        t_mostWinHero = newText("", "Most games won with this hero");
        panel.add(t_mostWinHero);
        t_mostWinHeroC = newText("", "Number of games won with this hero");
        panel.add(t_mostWinHeroC);
        panel.add(newLabel(""));
        panel.add(newLabel(labs[18]));
        t_mostWinMode = newText("", "Most games won with this mode");
        panel.add(t_mostWinMode);
        t_mostWinModeC = newText("", "Number of games won with this mode");
        panel.add(t_mostWinModeC);
        //11
        panel.add(newLabel(labs[19]));
        t_mostLoseHero = newText("", "Most games lost with this hero");
        panel.add(t_mostLoseHero);
        t_mostLoseHeroC = newText("", "Number of games lost with this hero");
        panel.add(t_mostLoseHeroC);
        panel.add(newLabel(""));
        panel.add(newLabel(labs[20]));
        t_mostLoseMode = newText("", "Most games lost with this mode");
        panel.add(t_mostLoseMode);
        t_mostLoseModeC = newText("", "Number of games lost with this mode");
        panel.add(t_mostLoseModeC);

        add(panel);
    }// </editor-fold>

    public void showStatistic() {
        if (sgl == null || sgl.isEmpty()) {
            return;
        }
        try {
            ss.readStatistic(this.sgl);
        } catch (Throwable ex) {
            System.out.println("GUI.showStatistic.catch: " + ex);
            return;
        }
        System.out.println("GUI.showStatistic: Jetzt fuellen wir die felder");
        this.t_first.setText(SigMisc.dateFormat.format(ss.getFirstGameDate().getTime()));
        this.t_last.setText(SigMisc.dateFormat.format(ss.getLastGameDate().getTime()));
        this.t_all.setText(ss.getSumAll() + "");
        this.t_val.setText(ss.getSumValid() + "");
        this.t_dz.setText(ss.getSumDz() + "");
        this.t_valSig.setText(ss.getSumSIGValid() + "");
        this.t_valVIP.setText(ss.getSumVIPValid() + "");
        this.t_avgLvl.setText(SigMisc.round(ss.getAvgLevel()) + "");
        //Win
        this.t_valWin.setText(ss.getSumWin() + "");
        this.t_valWinP.setText(SigMisc.round(ss.getAvgWin()) + "%");
        this.t_sigWin.setText(ss.getSumSIGWin() + "");
        this.t_sigWinP.setText(SigMisc.round(ss.getAvgSIGWin()) + "%");
        this.t_vipWin.setText(ss.getSumVIPWin() + "");
        this.t_vipWinP.setText(SigMisc.round(ss.getAvgVIPWin()) + "%");
        //Lose
        this.t_valLose.setText(ss.getSumLose() + "");
        this.t_valLoseP.setText(SigMisc.round(ss.getAvgLose()) + "%");
        this.t_sigLose.setText(ss.getSumSIGLose() + "");
        this.t_sigLoseP.setText(SigMisc.round(ss.getAvgSIGLose()) + "%");
        this.t_vipLose.setText(ss.getSumVIPLose() + "");
        this.t_vipLoseP.setText(SigMisc.round(ss.getAvgVIPLose()) + "%");
        //Leave
        this.t_valLeave.setText(ss.getSumLeave() + "");
        this.t_valLeaveP.setText(SigMisc.round(ss.getAvgLeave()) + "%");
        this.t_sigLeave.setText(ss.getSumSIGLeave() + "");
        this.t_sigLeaveP.setText(SigMisc.round(ss.getAvgSIGLeave()) + "%");
        this.t_vipLeave.setText(ss.getSumVIPLeave() + "");
        this.t_vipLeaveP.setText(SigMisc.round(ss.getAvgVIPLeave()) + "%");
        //Close
        this.t_valClose.setText(ss.getSumClose() + "");
        this.t_valCloseP.setText(SigMisc.round(ss.getAvgClose()) + "%");
        this.t_sigClose.setText(ss.getSumSIGClose() + "");
        this.t_sigCloseP.setText(SigMisc.round(ss.getAvgSIGClose()) + "%");
        this.t_vipClose.setText(ss.getSumVIPClose() + "");
        this.t_vipCloseP.setText(SigMisc.round(ss.getAvgVIPClose()) + "%");
        //most
        this.t_mostHero.setText(ss.getMostHero());
        this.t_mostHeroC.setText(ss.getHeroCount().get(ss.getMostHero()) + "");
        this.t_mostMode.setText(ss.getMostMode());
        this.t_mostModeC.setText(ss.getModeCount().get(ss.getMostMode()) + "");
        this.t_mostWinHero.setText(ss.getBestHero());
        this.t_mostWinHeroC.setText(ss.getHeroWinCount().get(ss.getBestHero()) + "");
        this.t_mostWinMode.setText(ss.getBestMode());
        this.t_mostWinModeC.setText(ss.getModeWinCount().get(ss.getBestMode()) + "");
        this.t_mostLoseHero.setText(ss.getWorstHero());
        this.t_mostLoseHeroC.setText(ss.getHeroLoseCount().get(ss.getWorstHero()) + "");
        this.t_mostLoseMode.setText(ss.getWorstMode());
        this.t_mostLoseModeC.setText(ss.getModeLoseCount().get(ss.getWorstMode()) + "");

        SigFilter.guiRdy = true;
        busy(false);
    }

    private void clearStatistic() {

        this.t_first.setText("");
        this.t_last.setText("");
        this.t_all.setText("");
        this.t_val.setText("");
        this.t_dz.setText("");
        this.t_valSig.setText("");
        this.t_valVIP.setText("");
        this.t_avgLvl.setText("");
        //Win
        this.t_valWin.setText("");
        this.t_valWinP.setText("");
        this.t_sigWin.setText("");
        this.t_sigWinP.setText("");
        this.t_vipWin.setText("");
        this.t_vipWinP.setText("");
        //Lose
        this.t_valLose.setText("");
        this.t_valLoseP.setText("");
        this.t_sigLose.setText("");
        this.t_sigLoseP.setText("");
        this.t_vipLose.setText("");
        this.t_vipLoseP.setText("");
        //Leave
        this.t_valLeave.setText("");
        this.t_valLeaveP.setText("");
        this.t_sigLeave.setText("");
        this.t_sigLeaveP.setText("");
        this.t_vipLeave.setText("");
        this.t_vipLeaveP.setText("");
        //Close
        this.t_valClose.setText("");
        this.t_valCloseP.setText("");
        this.t_sigClose.setText("");
        this.t_sigCloseP.setText("");
        this.t_vipClose.setText("");
        this.t_vipCloseP.setText("");
        //most
        this.t_mostHero.setText("");
        this.t_mostHeroC.setText("");
        this.t_mostMode.setText("");
        this.t_mostModeC.setText("");
        this.t_mostWinHero.setText("");
        this.t_mostWinHeroC.setText("");
        this.t_mostWinMode.setText("");
        this.t_mostWinModeC.setText("");
        this.t_mostLoseHero.setText("");
        this.t_mostLoseHeroC.setText("");
        this.t_mostLoseMode.setText("");
        this.t_mostLoseModeC.setText("");

    }

    private void busy(boolean busy) {
        if (busy) {
            panel.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            done = false;
            t_id.setEnabled(false);
            this.b_copy.setEnabled(false);
            this.b_update.setEnabled(false);
            this.b_save.setEnabled(false);
            this.b_loadAll.setEnabled(false);
            this.b_loadLocal.setEnabled(false);
            this.b_filter.setEnabled(false);
        } else {
            panel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            done = true;
            t_id.setEnabled(true);
            this.b_copy.setEnabled(true);
            this.b_update.setEnabled(true);
            this.b_save.setEnabled(true);
            this.b_loadAll.setEnabled(true);
            this.b_loadLocal.setEnabled(true);
            this.b_filter.setEnabled(true);
        }
    }

    private void loadLocal() {
        int id = 0;
        clearStatistic();
        if (!done) {
            return;
        }
        busy(true);
        try {
            id = Integer.parseInt(t_id.getText());
        } catch (Throwable ex) {
            t_id.setText("No valid ID");
            busy(false);
            return;
        }
        if (id == 0) {
            t_id.setText("No valid ID");
            busy(false);
            return;
        }
        try {
            this.sgl = BL.loadGamesLocally(id);
            showStatistic();
        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("no local data");
            t_id.setText("No local data");
            busy(false);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            System.out.println("incompatible data");
            t_id.setText("incompatible data");
            busy(false);
        } catch (Throwable ex) {
            ex.printStackTrace();
            System.out.println("unknown error");
            t_id.setText("unknown error");
            busy(false);
        }
        busy(false);
    }

    private void loadAll() throws InterruptedException {
        int id = 0;
        clearStatistic();
        if (!done) {
            return;
        }
        busy(true);
        try {
            id = Integer.parseInt(t_id.getText());
        } catch (Throwable ex) {
            t_id.setText("No valid ID");
            busy(false);
            return;
        }
        if (id == 0) {
            t_id.setText("No valid ID");
            busy(false);
            return;
        }
        try {
            sgl = BL.loadAllGames(id);
        } catch (Throwable ex) {
            System.out.println("GUI.loadAll.catch: " + ex);
            busy(false);
        }
        //new Thread(SP).start();
    }

    private void updateGames() {
        int id = 0;
        clearStatistic();
        if (!done) {
            return;
        }
        busy(true);
        try {
            id = Integer.parseInt(t_id.getText());
        } catch (Throwable ex) {
            t_id.setText("No valid ID");
            busy(false);
            return;
        }
        if (id == 0) {
            t_id.setText("No valid ID");
            busy(false);
            return;
        }
        try {
            sgl = BL.updateList(sgl, id);
        } catch (Throwable ex) {
            System.out.println("GUI.updateGames.catch: " + ex);
            busy(false);
        }
        showStatistic();
        busy(false);
    }

    private void save() {
        int id = 0;
        if (!done) {
            return;
        }
        busy(true);
        try {
            id = Integer.parseInt(t_id.getText());
        } catch (Throwable ex) {
            busy(false);
            t_id.setText("No valid ID");
            return;
        }
        if (id == 0) {
            busy(false);
            t_id.setText("No valid ID");
            return;
        }
        System.out.println(SigMisc.dateFormat.format(sgl.get(0).getStartTime().getTime()));
        try {
            BL.saveGamesLocally(sgl, id);
        } catch (Throwable ex) {
            System.out.println("Could not save");
        }
        busy(false);
    }

    private void copyToClip() {
        if (!done) {
            return;
        }
        busy(true);
        try {
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(ss.getStatistics()), null);
        } catch (Throwable ex) {
            System.out.println("Nothing to copy");
        }
        busy(false);
    }

    @Deprecated
    private void waitAndApplyFilter() throws InterruptedException {
        while (!SigFilter.rdy && !SFG.isVisible()) {
            Thread.sleep(500);
        }
        System.out.println("GUI.waitandapplyfilter: filter gui disposed!");
        if (!SigFilter.rdy) {
            return;
        }
        if (SigFilter.onlyDZ) {
            sgl = BL.subGamesDZ(sgl);
        }
        if (SigFilter.onlyValid) {
            sgl = BL.subGamesValid(sgl);
        }
        if (SigFilter.SIGorVIP == 1) {
            sgl = BL.subGamesSIG(sgl);
        } else if (SigFilter.SIGorVIP == 2) {
            sgl = BL.subGamesVIP(sgl);
        }
        if (SigFilter.minLvl > 1) {
            sgl = BL.subGamesMinLevel(sgl, SigFilter.minLvl);
        }
        if (SigFilter.maxLvl < 25) {
            sgl = BL.subGamesMaxLevel(sgl, SigFilter.maxLvl);
        }
        if (!SigFilter.onlyHero.isEmpty()) {
            sgl = BL.subGamesHero(sgl, SigFilter.onlyHero);
        }
        if (!SigFilter.onlyMode.isEmpty()) {
            sgl = BL.subGamesMode(sgl, SigFilter.onlyMode);
        }
        if (SigFilter.num != 0) {
            sgl = new ArrayList<SigGame>(sgl.subList(0, SigFilter.num));
        }
        SigFilter.reset();
        //Thread.sleep(100);
        this.showStatistic();
    }

    private JLabel newLabel(String s) {
        JLabel lab = new JLabel(s);
        lab.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        return lab;
    }

    private JTextField newText(String s, String tt) {
        JTextField display = new JTextField(s);
        display.setEditable(false);
        display.setBackground(new java.awt.Color(255, 255, 255));
        display.setToolTipText(tt);
        return display;
    }

    public static void main(String[] args) throws Throwable {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                try {
                    SigStatsGui gui = new SigStatsGui();
                    gui.setVisible(true);
                } catch (Throwable ex) {
                    System.out.println("main.catch: " + ex);
                }
            }
        });
    }

    private void applyFilter() {
        System.out.println("GUI.applyFilter: ");
        if (!SigFilter.rdy) {
            return;
        }
        this.setEnabled(true);
        if (SigFilter.onlyDZ) {
            sgl = BL.subGamesDZ(sgl);
        }
        if (SigFilter.onlyValid) {
            sgl = BL.subGamesValid(sgl);
        }
        if (SigFilter.SIGorVIP == 1) {
            sgl = BL.subGamesSIG(sgl);
        } else if (SigFilter.SIGorVIP == 2) {
            sgl = BL.subGamesVIP(sgl);
        }
        if (SigFilter.minLvl > 1) {
            sgl = BL.subGamesMinLevel(sgl, SigFilter.minLvl);
        }
        if (SigFilter.maxLvl < 25) {
            sgl = BL.subGamesMaxLevel(sgl, SigFilter.maxLvl);
        }
        if (!SigFilter.onlyHero.isEmpty()) {
            sgl = BL.subGamesHero(sgl, SigFilter.onlyHero);
        }
        if (!SigFilter.onlyMode.isEmpty()) {
            sgl = BL.subGamesMode(sgl, SigFilter.onlyMode);
        }
        if (SigFilter.num != 0) {
            sgl = new ArrayList<SigGame>(sgl.subList(0, SigFilter.num));
        }
        SigFilter.reset();
        this.showStatistic();
    }

    private void setProgress(int val, int max) {
        pg.setMaximum(max);
        pg.setValue(val);

    }

    public void update(Observable o, Object arg) {
        try {
            this.sgl = new ArrayList<SigGame>((ArrayList<SigGame>) arg);
            System.out.println("GUI.update.try: " + sgl.get(0).getHero());
            showStatistic();
        } catch (ClassCastException ex) {
            int val = ((int[]) arg)[0];
            int max = ((int[]) arg)[1];
            if (val == -2) {
                t_id.setText("No Connection");
                busy(false);
                val = 0;
            }
            if (val == -3) {
                t_id.setText("No Games");
                busy(false);
                val = 0;
            }
            System.out.println("GUI.update.catch: " + ((int[]) arg)[0] + " " + ((int[]) arg)[1] + " ex: " + ex);
            setProgress(val, max);
        } catch (Throwable ex) {
            System.out.println("GUI.update.catch: ex: " + ex);
            busy(false);
        }
    }
}
