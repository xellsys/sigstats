package xellsys.sigstats.sigStatsDAL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import xellsys.sigstats.sharedEntities.GameResult;
import xellsys.sigstats.sharedEntities.GameType;
import xellsys.sigstats.sharedEntities.SigGame;
import xellsys.sigstats.sharedEntities.SigMisc;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * @author xellsys
 */
public class SigStatsWebDAL extends Observable {

    //class attributes
    private int timeout = 10000;
    //observable

    //methods
    //public
    public void setProcess(Object arg) {
        setChanged();
        notifyObservers(arg);
    }

    public boolean checkWeb() throws IOException {
        //TODO: exakten String herausfinden der nur bei Maintenance zu finden ist
        if (Jsoup.connect("http://www.dota-league.com/index.php?section=news").timeout(timeout).get().text().contains("Maintenance")) {
            return false;
        } else {
            return true;
        }
    }

    public List<SigGame> loadAllGames(int id) throws Throwable {
        List<SigGame> sgl = new ArrayList<SigGame>();
        boolean exists = true;
        int p = 0, toCount = 0, max;
        try {
            max = getMaxPages(id);
        } catch (NullPointerException ex) {
            //die person hat keine liste unten, daher nur 1 seite zu laden, manuell max auf 1 setzen
            max = 1;
            toCount = 4;
            System.out.println("DAL.loadAllGames.catch.NullPointerException: getmaxpages = " + max + " ex: " + ex);
        } catch (Throwable ex) {
            System.out.println("DAL.loadAllGames.catch.Throwable: ex: " + ex);
            int[] array = {-2, 1};
            setProcess(array);
            throw ex;
        }
        while (exists) {
            try {
                sgl.addAll(loadGamesByPages(id, ++p));
                int[] array = {p, max};
                setProcess(array);
            } catch (SocketTimeoutException ex) {
                if (++toCount > 4) {
                    int[] array = {-2, 1};
                    setProcess(array);
                    throw SigMisc.NoConnectionToDL;
                }
                --p;
                continue;
            } catch (Throwable ex) {
                if (ex.equals(SigMisc.PageNotFound)) {
                    exists = false;
                } else {
                    throw ex;
                }
            }
        }
        setProcess(sgl);
        return sgl;
    }

    public List<SigGame> loadGamesByPages(int id, int page) throws Throwable {
        //parse each game from source
        ArrayList<SigGame> sgl = new ArrayList<SigGame>(0);
        Document doc = getTable(id, page);
        Elements trs = doc.getElementsByTag("tr");
        System.out.println("DAL.loadGamesByPages: Page=" + page + " Games=" + (trs.size() - 1));
        if (trs.size() < 2) {
            throw SigMisc.PageNotFound;
        }
        //improve performance by allocating memory for all elements to come in one instruction
        sgl.ensureCapacity(sgl.size() + trs.size() - 1);
        for (int i = 1; i < trs.size(); ++i) {
            sgl.add(trToSigGame(trs.get(i)));
        }
        return sgl;
    }

    public ArrayList<SigGame> loadGames(int id, int num) throws Throwable {
        int sum = 0, page = 0, toCount = 0;
        //parse each game from source
        ArrayList<SigGame> sgl = new ArrayList<SigGame>(0);
        Document doc;
        Elements trs;
        while (sum < num) {
            try {
                doc = getTable(id, ++page);
                trs = doc.getElementsByTag("tr");
            } catch (SocketTimeoutException ex) {
                System.out.println("DAL.loadGames.catch: " + ex);
                --page;
                if (++toCount > 4) {
                    throw SigMisc.NoConnectionToDL;
                }
                continue;
            }
            System.out.println("DAL.loadGames: Page=" + page + " Games=" + (trs.size() - 1));
            if (trs.size() < 2) {
                throw SigMisc.PageNotFound;
            }
            //improve performance by allocating memory for all elements to come in one instruction
            sgl.ensureCapacity(sgl.size() + trs.size() - 1);
            for (int i = 1; i < trs.size(); ++i) {
                ++sum;
                sgl.add(trToSigGame(trs.get(i)));
            }
        }
        return sgl;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getMaxPages(int id) throws Throwable {
        Document doc = getHeroPage(id, 1);
        return doc.getElementsByClass("pages").first().getElementsByTag("a").size();
    }
    //private

    private int getMaxGames(int id) throws Throwable {
        Document doc = getOverview(id);
        return Integer.parseInt(doc.getElementsByTag("table").first().getElementsByTag("tr").get(3).getElementsByTag("td").get(1).text());
    }

    private Document getOverview(int id) throws Throwable {
        return Jsoup.connect("http://www.dota-league.com/index.php?section=profile&show=sig&id=" + id).timeout(timeout).get();
    }

    private Document getHeroPage(int id, int page) throws Throwable {
        return Jsoup.connect("http://www.dota-league.com/?section=profile&show=all_sig&id=" + id + "&page=" + page).timeout(timeout).get();
    }

    private Document getTable(int id, int page) throws Throwable {
        String temp = getHeroPage(id, page).toString();
        if (temp.contains("Page not found")) {
            throw SigMisc.PageNotFound;
        }
        int start = temp.indexOf("<table");
        if (start < 0) {
            throw SigMisc.PageNotFound;
        }
        //es ist der erste <table> tag im source
        int end = temp.indexOf("</table>", start + 1);
        if (end < 0) {
            throw SigMisc.PageNotFound;
        }
        temp = temp.substring(start, temp.indexOf(">", end) + 1);
        Document doc = Jsoup.parse(temp);
        return doc;
    }

    private SigGame trToSigGame(Element tr) {
        SigGame sg = new SigGame();
        List<Element> el = tr.getElementsByTag("td");
        //extract game type
        sg.setGameType(el.get(0).toString().contains("vip") ? GameType.vip : GameType.sig);
        //extract host type
        if (el.get(1).getElementsByTag("img").size() > 0) {
            sg.setDZ(true);
            //inside this if we only get information dz offers
            //extract hero name and level
            sg.setHero(el.get(1).getElementsByTag("img").get(1).attr("alt").toString());
            String tmp = el.get(1).getElementsByTag("img").get(1).attr("title").toString();
            int pos;
            if ((pos = tmp.indexOf(":")) > 0) {
                sg.setLevel(Integer.parseInt(tmp.substring(pos + 2, tmp.length())));
            }
        } else {
            sg.setDZ(false);
        }
        //extract game mode
        sg.setMode(el.get(2).text().toString());
        //extract time
        sg.setStart(el.get(3).text().toString());
        //extract active/closed
        if (el.get(4).text().toString().matches("close")) ;
        sg.setClosed();
        //extract result
        switch (Integer.parseInt(el.get(5).getElementsByTag("span").text().toString())) {
            case -3:
                sg.setGameResult(GameResult.lose);
                break;
            case 5:
                sg.setGameResult(GameResult.win);
                break;
            case -10:
                sg.setGameResult(GameResult.leave);
                break;
            case 0:
            default:
                sg.setGameResult(GameResult.close);
                break;
        }
        return sg;
    }
}
