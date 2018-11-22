package iwmb02.com.iwmb02.services;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionChecker {
    public static boolean connectionAvailable(Context context) {

        boolean connected = false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) { // verbunden mit dem internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                // verbunden über wifi
                connected = true;

            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                // verbunden über das mobile Datennetz
                connected = true;

            }
        } else {
            // nicht mit dem internet verbunden
            connected = false;

        }
        return connected;
    }
}
