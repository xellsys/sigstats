package xellsys.sigstats.sigStatsBL;

import xellsys.sigstats.sharedEntities.GameResult;
import xellsys.sigstats.sharedEntities.GameType;
import xellsys.sigstats.sharedEntities.SigGame;
import xellsys.sigstats.sharedEntities.SigMisc;
import xellsys.sigstats.sigStatsDAL.SigStatsWebDAL;

import java.io.*;
import java.util.*;

/**
 * @author xellsys
 */
public class SigStatsBL extends Observable implements Observer, Runnable {

    //attributes
    //private
    private SigStatsWebDAL DAL;
    private int id_t;
    //constructor

    //Observable
    public void setProcess(Object arg) {
        setChanged();
        notifyObservers(arg);
    }

    //Observer
    public void update(Observable o, Object arg) {
        try {
            List<SigGame> sgl = (ArrayList<SigGame>) arg;
            setProcess(arg);
            System.out.println("BL.update.try: " + ((ArrayList<SigGame>) arg).get(0).getHero());
        } catch (IndexOutOfBoundsException ex) {
            System.out.println("BL.update.catch.IndexOutOfBoundsException: ex: " + ex);
            int[] array = {-3, 1};
            setProcess(array);
        } catch (Throwable ex) {
            System.out.println("BL.update.catch.Throwable: ex: " + ex);
            setProcess(arg);
        }
    }

    public SigStatsBL() throws Throwable {
        DAL = new SigStatsWebDAL();

        //Observer
        DAL.addObserver(this);

//        try {
//            DAL.checkWeb();
//        } catch (Throwable ex) {
//            throw SigMisc.NoConnectionToDL;
//        }
    }
    //methods
    //public

    public List<SigGame> loadAllGames(int id) throws Throwable {
        id_t = id;
        new Thread(this).start();
        return null;
        /*temporary excluded
        List<SigGame> sgl = null;
        try {
        sgl = DAL.loadAllGames(id);
        } catch (Throwable ex) {
        System.out.println(ex);
        }
        return sgl;
         */
    }

    public List<SigGame> loadMinGames(int id, int num) throws Throwable {
        return DAL.loadGames(id, num);
    }

    public List<SigGame> loadValidGames(int id, int num) throws Throwable {
        int p = 0;
        List<SigGame> sgl = new ArrayList<SigGame>(50);
        while (true) {
            try {
                sgl.addAll(DAL.loadGamesByPages(id, ++p));
            } catch (Throwable ex) {
                if (ex.equals(SigMisc.PageNotFound)) {
                    return sgl;
                } else {
                    throw ex;
                }
            }
            //find enough valid games
            sgl = this.subGamesValid(sgl);
            if (sgl.size() == num) {
                return sgl;
            } else if (sgl.size() > num) {
                sgl = new ArrayList<SigGame>(sgl.subList(0, num));
                return sgl;
            }
        }
    }

    public List<SigGame> loadExactGames(int id, int num) throws Throwable {
        List<SigGame> sgl = DAL.loadGames(id, num);
        sgl = new ArrayList<SigGame>(sgl.subList(0, num));
        return sgl;
    }

    public List<SigGame> loadGamesSince(int id, Calendar firstDate) throws Throwable {
        boolean finish = false;
        int firstGame = 0, p = 0;
        List<SigGame> sgl = new ArrayList<SigGame>(50);
        while (!finish) {
            try {
                sgl.addAll(DAL.loadGamesByPages(id, ++p));
            } catch (Throwable ex) {
                if (ex.equals(SigMisc.PageNotFound)) {
                    finish = true;
                    firstGame = sgl.size() - 1;
                } else {
                    throw ex;
                }
            }
            //find first date
            for (SigGame sg : sgl) {
                if (sg.getStartTime().getTime().before(firstDate.getTime())) {
                    finish = true;
                    firstGame = sgl.indexOf(sg);
                    break;
                }
            }
        }
        return new ArrayList<SigGame>(sgl.subList(0, firstGame));
    }

    @Deprecated
    public List<SigGame> loadGames(int id, int num) {
        return new ArrayList<SigGame>();
    }

    @Deprecated
    public SigGame loadGame(int id, String time) {
        return new SigGame();
    }

    @Deprecated
    public SigGame loadGame(int userid, int gameid) {
        return new SigGame();
    }
    //public
    //returns a sublist containing all games between two given parameters or at a specific date, month, year

    @Deprecated
    // <editor-fold defaultstate="collapsed" desc="Subgames filter">
    public List<SigGame> subGames(List<SigGame> sgl, int firstId, int lastId) {
        return sgl;
    }

    public List<SigGame> subGamesBetweenDates(List<SigGame> sgl, Calendar firstDate, Calendar lastDate) {
        ArrayList<SigGame> subsgl = new ArrayList<SigGame>();
        SigGame sg;
        for (SigGame aSgl : sgl) {
            sg = aSgl;
            if (sg.getStartTime().getTimeInMillis() > firstDate.getTimeInMillis()
                    && sg.getStartTime().getTimeInMillis() < lastDate.getTimeInMillis()) {
                subsgl.add(sg);
            }
        }
        return sgl;
    }

    public List<SigGame> subGamesDate(List<SigGame> sgl, Calendar day) {
        ArrayList<SigGame> subsgl = new ArrayList<SigGame>();
        SigGame sg;
        for (SigGame aSgl : sgl) {
            sg = aSgl;
            if (sg.getStartTime().get(Calendar.DAY_OF_YEAR) == day.get(Calendar.DAY_OF_YEAR)
                    && sg.getStartTime().get(Calendar.YEAR) == day.get(Calendar.YEAR)) {
                subsgl.add(sg);
            }
        }
        return subsgl;
    }

    public List<SigGame> subGamesMonth(List<SigGame> sgl, int month, int year) {
        ArrayList<SigGame> subsgl = new ArrayList<SigGame>();
        SigGame sg;
        for (SigGame aSgl : sgl) {
            sg = aSgl;
            if (sg.getStartTime().get(Calendar.YEAR) == year
                    && sg.getStartTime().get(Calendar.MONTH) == month) {
                subsgl.add(sg);
            }
        }
        return subsgl;
    }

    public List<SigGame> subGamesYear(List<SigGame> sgl, int year) {
        ArrayList<SigGame> subsgl = new ArrayList<SigGame>();
        SigGame sg;
        for (SigGame aSgl : sgl) {
            sg = aSgl;
            if (sg.getStartTime().get(Calendar.YEAR) == year) {
                subsgl.add(sg);

            }
        }
        return subsgl;
    }

    public List<SigGame> subGamesDZ(List<SigGame> sgl) {
        ArrayList<SigGame> subsgl = new ArrayList<SigGame>();
        SigGame sg;
        for (SigGame aSgl : sgl) {
            sg = aSgl;
            if (sg.isDZ()) {
                subsgl.add(sg);

            }
        }
        return subsgl;
    }

    public List<SigGame> subGamesBnet(List<SigGame> sgl) {
        ArrayList<SigGame> subsgl = new ArrayList<SigGame>();
        SigGame sg;
        for (SigGame aSgl : sgl) {
            sg = aSgl;
            if (!sg.isDZ()) {
                subsgl.add(sg);
            }
        }
        return subsgl;
    }

    public List<SigGame> subGamesHero(List<SigGame> sgl, String hero) {
        ArrayList<SigGame> subsgl = new ArrayList<SigGame>();
        SigGame sg;
        for (SigGame aSgl : sgl) {
            sg = aSgl;
            if (sg.getHero().equals(hero)) {
                subsgl.add(sg);
            }
        }
        return subsgl;
    }

    public List<SigGame> subGamesMode(List<SigGame> sgl, String mode) {
        ArrayList<SigGame> subsgl = new ArrayList<SigGame>();
        SigGame sg;
        for (SigGame aSgl : sgl) {
            sg = aSgl;
            if (sg.getMode().equals(mode)) {
                subsgl.add(sg);
            }
        }
        return subsgl;
    }

    public List<SigGame> subGamesMinLevel(List<SigGame> sgl, int level) {
        ArrayList<SigGame> subsgl = new ArrayList<SigGame>();
        SigGame sg;
        for (SigGame aSgl : sgl) {
            sg = aSgl;
            if (sg.getLevel() >= level) {
                subsgl.add(sg);
            }
        }
        return subsgl;
    }

    public List<SigGame> subGamesMaxLevel(List<SigGame> sgl, int level) {
        ArrayList<SigGame> subsgl = new ArrayList<SigGame>();
        SigGame sg;
        for (SigGame aSgl : sgl) {
            sg = aSgl;
            if (sg.getLevel() <= level) {
                subsgl.add(sg);
            }
        }
        return subsgl;
    }

    public List<SigGame> subGamesVIP(List<SigGame> sgl) {
        ArrayList<SigGame> subsgl = new ArrayList<SigGame>();
        SigGame sg;
        for (SigGame aSgl : sgl) {
            sg = aSgl;
            if (sg.getGameType().equals(GameType.vip)) {
                subsgl.add(sg);
            }
        }
        return subsgl;
    }

    public List<SigGame> subGamesValid(List<SigGame> sgl) {
        ArrayList<SigGame> subsgl = new ArrayList<SigGame>();
        SigGame sg;
        for (SigGame aSgl : sgl) {
            sg = aSgl;
            if (!sg.getGameResult().equals(GameResult.close)) {
                subsgl.add(sg);

            }
        }
        return subsgl;
    }

    public List<SigGame> subGamesSIG(List<SigGame> sgl) {
        ArrayList<SigGame> subsgl = new ArrayList<SigGame>();
        SigGame sg;
        for (SigGame aSgl : sgl) {
            sg = aSgl;
            if (sg.getGameType().equals(GameType.sig)) {
                subsgl.add(sg);

            }
        }
        return subsgl;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Save Load Update">

    public void saveGamesLocally(List<SigGame> sgl, int userid) throws Throwable {
        Serializable sl = (Serializable) sgl;
        FileOutputStream fileOut = new FileOutputStream("sig." + userid + ".ser");
        ObjectOutputStream output = new ObjectOutputStream(fileOut);
        output.writeObject(sl);
    }

    public List<SigGame> loadGamesLocally(int userid) throws Throwable {
        FileInputStream fis = new FileInputStream("sig." + userid + ".ser");
        ObjectInputStream in = new ObjectInputStream(fis);
        List<SigGame> ret = null;
        try {
            ret = (List<SigGame>) in.readObject();
            ret.get(0).getAssists();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (ClassCastException ex) {
            ret = new ArrayList<SigGame>();
            fis.close();
            fis = new FileInputStream("sig." + userid + ".ser");
            ObjectInputStream inn = new ObjectInputStream(fis);
            List<SharedEntities.SigGame> objj = (List<SharedEntities.SigGame>) inn.readObject();
            for (SharedEntities.SigGame i : objj) {
                ret.add(mapSigGame(i));
            }
        }
        return ret;
    }

    private SigGame mapSigGame(SharedEntities.SigGame input) {
        SigGame out = new SigGame(input.getHero());
        out.setAssists(input.getAssists());
        out.setActive(input.isActive());
        out.setDeaths(input.getDeaths());
        out.setDZ(input.isDZ());
        out.setGameResult(input.getGameResult().toString());
        out.setGameType(input.getGameType().toString());
        out.setHero(input.getHero());
        out.setKills(input.getKills());
        out.setLength(input.getLength());
        out.setLevel(input.getLevel());
        out.setMode(input.getMode());
        out.setStart(input.getStartTime());
        return out;
    }

    public List<SigGame> updateList(List<SigGame> sgl, int userid) throws Throwable {
        List<SigGame> tmp;
        tmp = this.loadGamesSince(userid, sgl.get(0).getStartTime());
        if (tmp.size() > 0 && !(tmp.get(0).getStartTime().getTimeInMillis() == sgl.get(0).getStartTime().getTimeInMillis())) {
            if (tmp.size() > 1)
                tmp.remove(tmp.size() - 1);
            tmp.addAll(sgl);
            return tmp;
        } else {
            return sgl;
        }
    }// </editor-fold>

    @Deprecated
    public SigGame getGameById(List<SigGame> sgl, int gameid) {
        return sgl.get(gameid);
    }

    public SigGame getGameByIndex(List<SigGame> sgl, int index) {
        return sgl.get(index);
    }

    public SigGame getGameByDate(List<SigGame> sgl, Calendar date) {
        SigGame sg;
        for (SigGame aSgl : sgl) {
            sg = aSgl;
            if (sg.getStartTime() == date) {
                return sg;
            }
        }
        return null;
    }

    public void run() {
        List<SigGame> sgl = null;
        try {
            sgl = DAL.loadAllGames(id_t);
        } catch (Throwable ex) {
            System.out.println("BL.run.catch: " + ex);
        }
    }
}
