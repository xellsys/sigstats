package xellsys.sigstats.sharedEntities;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class SigMisc {
    public static Exception PageNotFound = new Exception("Page not found");
    public static Exception NoSig = new Exception("No SIG Games found");
    public static Exception NoConnectionToDL = new Exception("Can not connect to Dota League");
    public static DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
    public static String version = "v1.1.3";
    public static double round(double d) {
        //setting localization to enable . (dot) as separator on every OS
        Locale.setDefault(Locale.ENGLISH);
        DecimalFormat twoDForm = new DecimalFormat(".##");
        return Double.valueOf(twoDForm.format(d));
    }
}
