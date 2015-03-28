package SharedEntities;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

/**
 *
 * @author xellsys
 */
@Deprecated
public class SigGame implements Serializable{

    // <editor-fold defaultstate="collapsed" desc="Constructors">
    public SigGame() {
        init();
    }

    public SigGame(String hero) {
        this.hero = hero;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Init">

    private void init() {
        setHero(gameTypeS = mode = "-");
        setLevel(length = kills = deaths = assists = resultPoints = 0);
        setGameResult(GameResult.close);
        setGameType(GameType.sig);
        setDZ(active = false);
        startTime = endTime = new GregorianCalendar();
    }
    // </editor-fold>

    @Deprecated
    public void printGame() {
        System.out.println("Hero: " + getHero() + " LVL: " + getLevel());
        System.out.println("Type: " + getGameType().toString() + " DZ: " + isDZ());
        System.out.println("Start: " + dateFormat.format(getStartTime().getTime()));
        System.out.println("Mode: " + getMode() + " Result: " + getGameResult().toString() + " " + getResultPoints());
        System.out.println("Stats: " + getKills() + "/" + getDeaths() + "/" + getAssists());
        System.out.println(isActive() ? "Active" : "Closed");
    }

    public void saveGame(BufferedWriter out) throws IOException {
        out.append(this.toString());
        out.append("Hero: " + getHero() + " LVL: " + getLevel() + "\n");
        out.append("Type: " + getGameType().toString() + " DZ: " + isDZ() + "\n");
        out.append("Start: " + dateFormat.format(getStartTime().getTime()));
        out.append("Mode: " + getMode() + " Result: " + getGameResult().toString() + " " + getResultPoints() + "\n");
        out.append("Stats: " + getKills() + "/" + getDeaths() + "/" + getAssists() + "\n");
        out.append(isActive() ? "Active" : "Closed\n\n");
    }
    // <editor-fold defaultstate="collapsed" desc="Class variables">
    //private dota_hero hero;
    private String hero;
    private int level;
    private int length;   //in seconds
    private int kills;
    private int deaths;
    private int assists;
    private GameResult gameResult;
    private int resultPoints;
    private GameType gameType;
    private String gameTypeS;
    private boolean isDZ;
    private String mode;
    private GregorianCalendar startTime;
    private GregorianCalendar endTime;
    private boolean active;
    private DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Getter">
    public String getHero() {
        return hero;
    }

    public int getLevel() {
        return level;
    }

    public int getLength() {
        return length;
    }

    public int getKills() {
        return kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public int getAssists() {
        return assists;
    }

    public GameResult getGameResult() {
        return gameResult;
    }

    public int getResultPoints() {
        return resultPoints;
    }

    public GameType getGameType() {
        return gameType;
    }

    public String getGameTypeS() {
        return gameTypeS;
    }

    public boolean isDZ() {
        return this.isDZ;
}

    public String getMode() {
        return mode;
    }

    public GregorianCalendar getStartTime() {
        return startTime;
    }

    public GregorianCalendar getEndTime() {
        return endTime;
    }

    public boolean isActive() {
        return active;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Setter">
    public void setClosed() {
        setActive(false);
    }

    public void setHero(String inHero) {
        this.hero = inHero;
    }

    public void setLevel(int inLevel) {
        this.level = inLevel;
    }

    public void setLength(int inLength) {
        this.length = inLength;
    }

    public void setKills(int inKills) {
        this.kills = inKills;
    }

    public void setDeaths(int inDeaths) {
        this.deaths = inDeaths;
    }

    public void setAssists(int inAssists) {
        this.assists = inAssists;
    }

    public void setGameResult(GameResult inState) {
        this.gameResult = inState;
        switch (inState) {
            case win:
                resultPoints = 5;
                break;
            case lose:
                resultPoints = -3;
                break;
            case leave:
                resultPoints = -10;
                break;
            case close:
            default:
                resultPoints = 0;
                break;
        }
    }

    public void setGameType(GameType inType) {
        this.gameType = inType;
        if (inType == GameType.vip) {
            this.gameTypeS = "VIP";
        } else {
            this.gameTypeS = "SIG";
        }
    }

    public void setDZ(boolean inIsDZ) {
        this.isDZ = inIsDZ;
    }

    public void setMode(String inMode) {
        this.mode = inMode;
    }

    public void setStart(String inStart) {
        startTime.set(Integer.parseInt(inStart.substring(6, 10)),
                Integer.parseInt(inStart.substring(3, 5)) - 1,
                Integer.parseInt(inStart.substring(0, 2)),
                Integer.parseInt(inStart.substring(11, 13)),
                Integer.parseInt(inStart.substring(14, 16)));
    }

    public void setActive(boolean inActive) {
        this.active = inActive;
    }
    // </editor-fold>
}
