package xellsys.sigstats.sigStatsGui;

import java.util.Calendar;

public class SigFilter {

    public static boolean guiRdy = false;
    public static boolean rdy = false;
    public static int num = 0;
    public static boolean onlyValid = false;
    public static boolean onlyDZ = false;
    public static int SIGorVIP = 0;
    public static Calendar first = null;
    public static Calendar last = null;
    public static Calendar specDate = null;
    public static int specYear = 0;
    public static int specMonth = 0;
    public static String onlyHero = null;
    public static String onlyMode = null;
    public static int minLvl = 0;
    public static int maxLvl = 25;

    public static void reset() {
        rdy = false;
        num = 0;
        onlyValid = false;
        onlyDZ = false;
        SIGorVIP = 0;
        first = null;
        last = null;
        specDate = null;
        specYear = 0;
        specMonth = 0;
        onlyHero = null;
        onlyMode = null;
        minLvl = 0;
        maxLvl = 25;
    }
}
