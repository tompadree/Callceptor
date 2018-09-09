package callceptor.com.callceptor.utils;

import android.content.Context
import android.net.ConnectivityManager

/**
 * Created by Tomislav on 05,September,2018
 */
class NetworkHelper {

    companion object {

        fun isConnectingToInternet(context: Context): Boolean {

            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            val activeNetwork = cm.activeNetworkInfo

            return activeNetwork != null && activeNetwork.isConnectedOrConnecting
        }

    }

}