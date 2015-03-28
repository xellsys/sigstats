package xellsys.sigstats;


import xellsys.sigstats.sharedEntities.SigGame;
import xellsys.sigstats.sharedEntities.SigMisc;
import xellsys.sigstats.sharedEntities.SigStatistic;
import xellsys.sigstats.sigStatsBL.SigStatsBL;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * @author xellsys
 */
public class Sigstats {

    public static void main(String[] args) throws Throwable {
        SigStatsBL BL = new SigStatsBL();
        List<SigGame> sgl = new ArrayList<SigGame>(0);
        Calendar firstDate = new GregorianCalendar();
        firstDate.set(2010, Calendar.JULY, 21);
        Calendar myDate = new GregorianCalendar();
        myDate.set(2011, Calendar.JANUARY, 1);
        switch (8) {
            case 0: {

            }
            case 1: {
                sgl = BL.loadGamesLocally(61343);
                try {
                    sgl = BL.updateList(sgl, 61343);
                    BL.saveGamesLocally(sgl, 61343);
                } catch (Throwable ex) {
                    System.out.println("Exception gefangen: " + ex.toString());
                    return;
                }
                sgl = BL.subGamesVIP(sgl);
                break;
            }
            case 2: {
                //sgl = BL.loadMinGames(61343, 77); //Meepwn=61343
                sgl = BL.loadGamesLocally(61343);
                BL.saveGamesLocally((List<SigGame>) sgl, 61343);
                //sgl = BL.subGamesVIP(sgl);
                break;
            }
            case 3: {
                sgl = BL.loadExactGames(61343, 233);
                break;
            }
            case 4: {
                sgl = BL.loadGamesSince(61343, firstDate);
                sgl = BL.subGamesMonth(sgl, Calendar.DECEMBER, 2010);
                break;
            }
            case 5: {
                sgl = BL.loadGamesSince(61343, firstDate);
                sgl = BL.subGamesMonth(sgl, Calendar.NOVEMBER, 2010);
                sgl = BL.subGamesMode(sgl, "-rd");
                break;
            }
            case 6: {
                sgl = BL.loadGamesSince(61343, firstDate);
                //BL.saveGamesLocally(sgl, 61343);
                sgl = BL.subGamesHero(sgl, "Shadow Fiend");
                sgl = BL.subGamesMinLevel(sgl, 20);
                break;
            }
            case 7: {
                sgl = BL.loadAllGames(159211);
                BL.saveGamesLocally(sgl, 159211);
                break;
            }
            case 8: {
                sgl = BL.loadAllGames(159211);
                //sgl = BL.loadGamesLocally(159211);
                //sgl = BL.updateList(sgl, 159211);
                BL.saveGamesLocally(sgl, 159211);
                break;
            }
            case 9: {
                sgl = BL.loadGamesLocally(159211);
                for (int i = 0; i < 20; ++i)
                    sgl.remove(0);
                BL.saveGamesLocally(sgl, 159211);
                sgl = null;
                sgl = BL.loadGamesLocally(159211);
                sgl = BL.updateList(sgl, 159211);
                break;
            }
            case 10: {
                sgl = BL.loadValidGames(159211, 51);
                //sgl = BL.subGamesValid(sgl);
                break;
            }
            case 11: {
                sgl = BL.loadGamesLocally(159211);
                sgl = BL.updateList(sgl, 159211);
                BL.saveGamesLocally(sgl, 159211);
                break;
            }
            case 12: {
                sgl = BL.loadGamesLocally(159211);
                break;
            }
            default: {
                break;
            }
        }
        if (sgl != null && sgl.size() > 0) {
            for (SigGame aSgl : sgl) {
                //sgl.get(i).saveGame(out);
                //sgl.get(i).printGame();
                System.out.println(SigMisc.dateFormat.format(aSgl.getStartTime().getTime()));
            }
            SigStatistic ss = new SigStatistic();
            ss.readStatistic(sgl);
            //ss.printPointsOverTime();
            System.out.println(ss.getStatistics());
            System.out.println("size:" + sgl.size());
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(ss.getStatistics()), null);
        } else {
            System.out.println("size=0");
        }
        //out.close();
    }
}
