package xellsys.sigstats.sharedEntities;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SigStatistic {

    private Calendar firstGameDate;
    private Calendar lastGameDate;
    //All avg
    private double avgLevel;
    private double avgWin;
    private double avgLose;
    private double avgLeave;
    private double avgClose;
    //VIP avg
    private double avgVIP;
    private double avgVIPWin;
    private double avgVIPLose;
    private double avgVIPLeave;
    private double avgVIPClose;
    //SIG avg
    private double avgSIG;
    private double avgSIGWin;
    private double avgSIGLose;
    private double avgSIGLeave;
    private double avgSIGClose;
    //All Sum
    private int sumWin;
    private int sumLose;
    private int sumLeave;
    private int sumClose;
    private int sumAll;
    private int sumValid;
    private int sumDz;
    //VIP Sum
    private int sumVIP;
    private int sumVIPWin;
    private int sumVIPLose;
    private int sumVIPLeave;
    private int sumVIPClose;
    private int sumVIPValid;
    //SIG Sum
    private int sumSIG;
    private int sumSIGWin;
    private int sumSIGLose;
    private int sumSIGLeave;
    private int sumSIGClose;
    private int sumSIGValid;
    //Other
    private String mostHero;
    private String mostMode;
    private String bestHero;
    private String bestMode;
    private String worstHero;
    private String worstMode;
    private Map<String, Integer> heroCount;
    private Map<String, Integer> modeCount;
    private Map<String, Integer> heroWinCount;
    private Map<String, Integer> modeWinCount;
    private Map<String, Integer> heroLoseCount;
    private Map<String, Integer> modeLoseCount;
    private int[] pot;
    private ArrayList<SigGame> sigGameList;

    private void init() {
        heroCount = new HashMap<String, Integer>();
        modeCount = new HashMap<String, Integer>();
        heroLoseCount = new HashMap<String, Integer>();
        heroWinCount = new HashMap<String, Integer>();
        modeLoseCount = new HashMap<String, Integer>();
        modeWinCount = new HashMap<String, Integer>();
        sumWin = sumLose = sumLeave = sumClose = sumAll = sumValid = sumDz = 0;
        sumVIP = sumVIPWin = sumVIPLose = sumVIPLeave = sumVIPClose = sumSIG = sumSIGWin = sumSIGLose = sumSIGLeave = sumSIGClose = 0;
        avgWin = avgLose = avgLeave = avgClose = avgSIG = avgVIP = avgLevel = 0;
        avgSIGWin = avgSIGLose = avgSIGLeave = avgSIGClose = sumSIGValid = 0;
        avgVIPWin = avgVIPLose = avgVIPLeave = avgVIPClose = sumVIPValid = 0;
        mostHero = mostMode = "";
    }

    @Deprecated
    public void printStatistic() {
        System.out.println("First Game: " + SigMisc.dateFormat.format(getFirstGameDate().getTime()) + "\nLast Game: " + SigMisc.dateFormat.format(getLastGameDate().getTime()));
        System.out.println("All Games: " + getSumAll() + "\nValid Games (no 0): " + getSumValid());
        System.out.println("DZ games: " + getSumDz() + "/" + getSumAll());
        System.out.println("Valid SIG: " + getSumSIGValid() + "/" + getSumValid() + " \nValid VIP: " + getSumVIPValid() + "/" + getSumValid());

        System.out.println("All Valid:");
        System.out.println("\tWin:   " + getSumWin() + " " + SigMisc.round(getAvgWin()) + "%");
        System.out.println("\tLose:  " + getSumLose() + " " + SigMisc.round(getAvgLose()) + "%");
        System.out.println("\tLeave: " + getSumLeave() + " " + SigMisc.round(getAvgLeave()) + "%");
        System.out.println("\tClose: " + getSumClose() + " " + SigMisc.round(getAvgClose()) + "%");

        System.out.println("All SIG: ");
        System.out.println("\tWin:   " + getSumSIGWin() + " " + SigMisc.round(getAvgSIGWin()) + "%");
        System.out.println("\tLose:  " + getSumSIGLose() + " " + SigMisc.round(getAvgSIGLose()) + "%");
        System.out.println("\tLeave: " + getSumSIGLeave() + " " + SigMisc.round(getAvgSIGLeave()) + "%");
        System.out.println("\tClose: " + getSumSIGClose() + " " + SigMisc.round(getAvgSIGClose()) + "%");

        System.out.println("All VIP: ");
        System.out.println("\tWin  : " + getSumVIPWin() + " " + SigMisc.round(getAvgVIPWin()) + "%");
        System.out.println("\tLose : " + getSumVIPLose() + " " + SigMisc.round(getAvgVIPLose()) + "%");
        System.out.println("\tLeave: " + getSumVIPLeave() + " " + SigMisc.round(getAvgVIPLeave()) + "%");
        System.out.println("\tClose: " + getSumVIPClose() + " " + SigMisc.round(getAvgVIPClose()) + "%");

        System.out.println("Average Level: " + getAvgLevel() + " ");
        System.out.println("Most played hero: " + getMostHero());
        System.out.println("Most played mode: " + getMostMode());
    }

    public String getStatistics() {
        StringBuilder s = new StringBuilder().append("First Game: ").append(SigMisc.dateFormat.format(getFirstGameDate().getTime())).append("\nLast Game: ").append(SigMisc.dateFormat.format(getLastGameDate().getTime())).append("\nAll Games: ").append(getSumAll()).append("\nValid Games (no 0): ").append(getSumValid()).append("\n").append("DZ games: ").append(getSumDz()).append("/").append(getSumAll()).append("\n").append("Valid SIG: ").append(getSumSIGValid()).append("/").append(getSumValid()).append(" \nValid VIP: ").append(getSumVIPValid()).append("/").append(getSumValid()).append("\n").append("All Valid:\n").append("\tWin:   ").append(getSumWin()).append(" ").append(SigMisc.round(getAvgWin())).append("%\n").append("\tLose:  ").append(getSumLose()).append(" ").append(SigMisc.round(getAvgLose())).append("%\n").append("\tLeave: ").append(getSumLeave()).append(" ").append(SigMisc.round(getAvgLeave())).append("%\n").append("\tClose: ").append(getSumClose()).append(" ").append(SigMisc.round(getAvgClose())).append("%\n").append("All SIG: \n").append("\tWin:   ").append(getSumSIGWin()).append(" ").append(SigMisc.round(getAvgSIGWin())).append("%\n").append("\tLose:  ").append(getSumSIGLose()).append(" ").append(SigMisc.round(getAvgSIGLose())).append("%\n").append("\tLeave: ").append(getSumSIGLeave()).append(" ").append(SigMisc.round(getAvgSIGLeave())).append("%\n").append("\tClose: ").append(getSumSIGClose()).append(" ").append(SigMisc.round(getAvgSIGClose())).append("%\n").append("All VIP: \n").append("\tWin  : ").append(getSumVIPWin()).append(" ").append(SigMisc.round(getAvgVIPWin())).append("%\n").append("\tLose : ").append(getSumVIPLose()).append(" ").append(SigMisc.round(getAvgVIPLose())).append("%\n").append("\tLeave: ").append(getSumVIPLeave()).append(" ").append(SigMisc.round(getAvgVIPLeave())).append("%\n").append("\tClose: ").append(getSumVIPClose()).append(" ").append(SigMisc.round(getAvgVIPClose())).append("%\n").append("Average Level: ").append(getAvgLevel()).append("\n").append("Most played hero: ").append(getMostHero()).append(" with ").append(getHeroCount().get(getMostHero())).append(" times\n").append("Most wins: ").append(getBestHero()).append(" with ").append(getHeroWinCount().get(getBestHero())).append(" wins\n").append("Most loses: ").append(getWorstHero()).append(" with ").append(getHeroLoseCount().get(getWorstHero())).append(" loses\n").append("Most played mode: ").append(getMostMode()).append(" with ").append(getModeCount().get(getMostMode())).append(" times\n").append("Most wins in: ").append(getBestMode()).append(" with ").append(getModeWinCount().get(getBestMode())).append(" wins\n").append("Most loses in: ").append(getWorstMode()).append(" with ").append(getModeLoseCount().get(getWorstMode())).append(" loses\n");

        return s.toString();
    }

    @Deprecated
    public void printPointsOverTime() {
        int sum = 0;
        for (Iterator<SigGame> i = getSigGameList().iterator(); i.hasNext();) {
            SigGame sg = i.next();
            if (sg.getResultPoints() == 0) {
                continue;
            }
            sum += i.next().getResultPoints();
            for (int j = 0; j < sum / 100; ++j) {
                System.out.print('#');
            }
            System.out.println();
        }
    }

    public void readStatistic(List<SigGame> sgl) {
        SigGame sg;
        this.init();
        this.firstGameDate = this.lastGameDate = sgl.get(0).getStartTime();
        for (Iterator<SigGame> i = sgl.iterator(); i.hasNext();) {
            sg = i.next();
            //set dates
            this.firstGameDate = sg.getStartTime().getTimeInMillis()
                    < this.getFirstGameDate().getTimeInMillis() ? sg.getStartTime() : this.getFirstGameDate();
            this.lastGameDate = sg.getStartTime().getTimeInMillis()
                    > this.getLastGameDate().getTimeInMillis() ? sg.getStartTime() : this.getLastGameDate();
            //count win/lose/leave/close
            switch (sg.getGameResult()) {
                case win:
                    ++sumWin;
                    //count wins/loses with current hero
                    if (sg.isDZ()) {
                        if (this.getHeroWinCount().containsKey(sg.getHero())) {
                            Integer c = this.getHeroWinCount().get(sg.getHero());
                            this.getHeroWinCount().put(sg.getHero(), ++c);
                        } else {
                            this.getHeroWinCount().put(sg.getHero(), 1);
                        }
                    }
                    if (this.getModeWinCount().containsKey(sg.getMode())) {
                        Integer c = this.getModeWinCount().get(sg.getMode());
                        this.getModeWinCount().put(sg.getMode(), ++c);
                    } else {
                        this.getModeWinCount().put(sg.getMode(), 1);
                    }
                    break;
                case lose:
                    ++sumLose;
                    //count wins/loses with current mode
                    if (sg.isDZ()) {
                        if (this.getHeroLoseCount().containsKey(sg.getHero())) {
                            Integer c = this.getHeroLoseCount().get(sg.getHero());
                            this.getHeroLoseCount().put(sg.getHero(), ++c);
                        } else {
                            this.getHeroLoseCount().put(sg.getHero(), 1);
                        }
                    }
                    if (this.getModeLoseCount().containsKey(sg.getMode())) {
                        Integer c = this.getModeLoseCount().get(sg.getMode());
                        this.getModeLoseCount().put(sg.getMode(), ++c);
                    } else {
                        this.getModeLoseCount().put(sg.getMode(), 1);
                    }
                    break;
                case leave:
                    ++sumLeave;
                    break;
                case close:
                default:
                    ++sumClose;
                    break;
            }
            //count VIP
            if (sg.getGameType() == GameType.vip) {
                ++this.sumVIP;
                switch (sg.getGameResult()) {
                    case win:
                        ++sumVIPWin;
                        ++sumVIPValid;
                        break;
                    case lose:
                        ++sumVIPLose;
                        ++sumVIPValid;
                        break;
                    case leave:
                        ++sumVIPLeave;
                        ++sumVIPValid;
                        break;
                    case close:
                    default:
                        ++sumVIPClose;
                        break;
                }
            }
            //count SIG
            if (sg.getGameType() == GameType.sig) {
                ++this.sumSIG;
                switch (sg.getGameResult()) {
                    case win:
                        ++sumSIGWin;
                        ++sumSIGValid;
                        break;
                    case lose:
                        ++sumSIGLose;
                        ++sumSIGValid;
                        break;
                    case leave:
                        ++sumSIGLeave;
                        ++sumSIGValid;
                        break;
                    case close:
                    default:
                        ++sumSIGClose;
                        break;
                }
            }
            //mode count
            String tmp = sg.getMode();
            if (this.getModeCount().containsKey(tmp)) {
                Integer c = this.getModeCount().get(tmp);
                this.getModeCount().put(tmp, ++c);
            } else {
                this.getModeCount().put(tmp, 1);
            }
            //if dz is available count this too
            if (sg.isDZ()) {
                //increase dz count
                ++this.sumDz;
                //set hero count
                String tmp1 = sg.getHero();
                if (this.getHeroCount().containsKey(tmp1)) {
                    Integer c = this.getHeroCount().get(tmp1);
                    this.getHeroCount().put(tmp1, ++c);
                } else {
                    this.getHeroCount().put(tmp1, 1);
                }
                //average level
                this.avgLevel += sg.getLevel();
            }
        }
        this.setMost(sgl);
        //All Sums
        this.sumAll = sgl.size();
        this.sumValid = this.getSumAll() - this.getSumClose();
        //All Averages
        if (getSumDz() > 0) {
            this.avgLevel /= (double) this.getSumDz();
        }
        if (getSumValid() > 0) {
            this.avgWin = getSumWin() / (double) this.getSumValid() * 100;
            this.avgLose = getSumLose() / (double) this.getSumValid() * 100;
            this.avgLeave = getSumLeave() / (double) this.getSumValid() * 100;
        }
        if (getSumAll() > 0) {
            this.avgClose = getSumClose() / (double) this.getSumAll() * 100;
        }
        //SIG Averages
        if (getSumAll() > 0) {
            this.avgSIG = getSumSIG() / (double) this.getSumAll() * 100;
        }
        if (getSumSIGValid() > 0) {
            this.avgSIGWin = getSumSIGWin() / (double) getSumSIGValid() * 100;
            this.avgSIGLose = getSumSIGLose() / (double) getSumSIGValid() * 100;
            this.avgSIGLeave = getSumSIGLeave() / (double) getSumSIGValid() * 100;
        }
        if (getSumSIG() > 0) {
            this.avgSIGClose = getSumSIGClose() / (double) getSumSIG() * 100;
        }
        //VIP Averages
        if (getSumAll() > 0) {
            this.avgVIP = getSumVIP() / (double) this.getSumAll() * 100;
        }
        if (getSumVIPValid() > 0) {
            this.avgVIPWin = getSumVIPWin() / (double) getSumVIPValid() * 100;
            this.avgVIPLose = getSumVIPLose() / (double) getSumVIPValid() * 100;
            this.avgVIPLeave = getSumVIPLeave() / (double) getSumVIPValid() * 100;
        }
        if (getSumVIP() > 0) {
            this.avgVIPClose = getSumVIPClose() / (double) getSumVIP() * 100;
        }
        this.sigGameList = new ArrayList(sgl);
    }

    public static int countResult(List<SigGame> sgl, GameResult res) {
        int count = 0;
        for (Iterator i = sgl.iterator(); i.hasNext();) {
            if (((SigGame) i.next()).getGameResult() == res) {
                ++count;
            }
        }
        return count;
    }

    public static int countHero(List<SigGame> sgl, String hero) {
        int count = 0;
        for (Iterator i = sgl.iterator(); i.hasNext();) {
            if (((SigGame) i.next()).getHero().equals(hero)) {
                ++count;
            }
        }
        return count;
    }

    public static double avgLevel(List<SigGame> sgl) {
        double avg = 0;
        for (Iterator i = sgl.iterator(); i.hasNext();) {
            avg += ((SigGame) i.next()).getLevel();
        }
        avg /= sgl.size();
        return avg;
    }

    private void setMost(List<SigGame> sgl) {
        String tmp;
        //most hero
        try {
            this.mostHero = getHeroCount().keySet().iterator().next();
            for (Iterator<String> i = getHeroCount().keySet().iterator(); i.hasNext();) {
                tmp = i.next();
                if (getHeroCount().get(tmp) > getHeroCount().get(this.getMostHero())) {
                    this.mostHero = new String(tmp);
                }
            }
        } catch (Throwable ex) {
            this.mostHero = "-";
        }

        //most mode
        try {
            this.mostMode = getModeCount().keySet().iterator().next();
            for (Iterator<String> i = getModeCount().keySet().iterator(); i.hasNext();) {
                tmp = i.next();
                if (getModeCount().get(tmp) > getModeCount().get(this.getMostMode())) {
                    this.mostMode = new String(tmp);
                }
            }
        } catch (Throwable ex) {
            this.mostMode = "-";
        }
        //most successful hero
        try {
            this.bestHero = getHeroWinCount().keySet().iterator().next();
            for (Iterator<String> i = getHeroWinCount().keySet().iterator(); i.hasNext();) {
                tmp = i.next();
                if (getHeroWinCount().get(tmp) > getHeroWinCount().get(this.getBestHero())) {
                    this.bestHero = new String(tmp);
                }
            }
        } catch (Throwable ex) {
            this.bestHero = "-";
        }
        //most sucessful mode
        try {
            this.bestMode = getModeWinCount().keySet().iterator().next();
            for (Iterator<String> i = getModeWinCount().keySet().iterator(); i.hasNext();) {
                tmp = i.next();
                if (getModeWinCount().get(tmp) > getModeWinCount().get(this.getBestMode())) {
                    this.bestMode = new String(tmp);
                }
            }
        } catch (Throwable ex) {
            this.bestMode = "-";
        }
        //least successful hero
        try {
            this.worstHero = getHeroLoseCount().keySet().iterator().next();
            for (Iterator<String> i = getHeroLoseCount().keySet().iterator(); i.hasNext();) {
                tmp = i.next();
                if (getHeroLoseCount().get(tmp) > getHeroLoseCount().get(this.getWorstHero())) {
                    this.worstHero = new String(tmp);
                }
            }
        } catch (Throwable ex) {
            this.worstHero = "-";
        }
        //least successful mode
        try {
            this.worstMode = getModeLoseCount().keySet().iterator().next();
            for (Iterator<String> i = getModeLoseCount().keySet().iterator(); i.hasNext();) {
                tmp = i.next();
                if (getModeLoseCount().get(tmp) > getModeLoseCount().get(this.getWorstMode())) {
                    this.worstMode = new String(tmp);
                }
            }
        } catch (Throwable ex) {
            this.worstMode = "-";
        }
    }
    // <editor-fold defaultstate="collapsed" desc="Getters">

    public Calendar getFirstGameDate() {
        return firstGameDate;
    }

    public Calendar getLastGameDate() {
        return lastGameDate;
    }

    public double getAvgLevel() {
        return avgLevel;
    }

    public double getAvgWin() {
        return avgWin;
    }

    public double getAvgLose() {
        return avgLose;
    }

    public double getAvgLeave() {
        return avgLeave;
    }

    public double getAvgClose() {
        return avgClose;
    }

    public double getAvgVIP() {
        return avgVIP;
    }

    public double getAvgVIPWin() {
        return avgVIPWin;
    }

    public double getAvgVIPLose() {
        return avgVIPLose;
    }

    public double getAvgVIPLeave() {
        return avgVIPLeave;
    }

    public double getAvgVIPClose() {
        return avgVIPClose;
    }

    public double getAvgSIG() {
        return avgSIG;
    }

    public double getAvgSIGWin() {
        return avgSIGWin;
    }

    public double getAvgSIGLose() {
        return avgSIGLose;
    }

    public double getAvgSIGLeave() {
        return avgSIGLeave;
    }

    public double getAvgSIGClose() {
        return avgSIGClose;
    }

    public int getSumWin() {
        return sumWin;
    }

    public int getSumLose() {
        return sumLose;
    }

    public int getSumLeave() {
        return sumLeave;
    }

    public int getSumClose() {
        return sumClose;
    }

    public int getSumAll() {
        return sumAll;
    }

    public int getSumValid() {
        return sumValid;
    }

    public int getSumDz() {
        return sumDz;
    }

    public int getSumVIP() {
        return sumVIP;
    }

    public int getSumVIPWin() {
        return sumVIPWin;
    }

    public int getSumVIPLose() {
        return sumVIPLose;
    }

    public int getSumVIPLeave() {
        return sumVIPLeave;
    }

    public int getSumVIPClose() {
        return sumVIPClose;
    }

    public int getSumVIPValid() {
        return sumVIPValid;
    }

    public int getSumSIG() {
        return sumSIG;
    }

    public int getSumSIGWin() {
        return sumSIGWin;
    }

    public int getSumSIGLose() {
        return sumSIGLose;
    }

    public int getSumSIGLeave() {
        return sumSIGLeave;
    }

    public int getSumSIGClose() {
        return sumSIGClose;
    }

    public int getSumSIGValid() {
        return sumSIGValid;
    }

    public String getMostHero() {
        return mostHero;
    }

    public String getMostMode() {
        return mostMode;
    }

    public String getBestHero() {
        return bestHero;
    }

    public String getBestMode() {
        return bestMode;
    }

    public String getWorstHero() {
        return worstHero;
    }

    public String getWorstMode() {
        return worstMode;
    }

    public Map<String, Integer> getHeroCount() {
        return heroCount;
    }

    public Map<String, Integer> getModeCount() {
        return modeCount;
    }

    public Map<String, Integer> getHeroWinCount() {
        return heroWinCount;
    }

    public Map<String, Integer> getModeWinCount() {
        return modeWinCount;
    }

    public Map<String, Integer> getHeroLoseCount() {
        return heroLoseCount;
    }

    public Map<String, Integer> getModeLoseCount() {
        return modeLoseCount;
    }

    public int[] getPot() {
        return pot;
    }

    public ArrayList<SigGame> getSigGameList() {
        return sigGameList;
    }// </editor-fold>
}
