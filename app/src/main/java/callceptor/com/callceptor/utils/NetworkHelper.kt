package callceptor.com.callceptor.utils;

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.NetworkInfo
import android.os.Parcelable
import android.support.design.widget.Snackbar
import android.util.Log
import android.view.View
import android.widget.Toast
import callceptor.com.callceptor.R
import android.net.ConnectivityManager



/**
 * Created by Tomislav on 05,September,2018
 */
class NetworkHelper {

    companion object {
        var isInternetOn = true


        fun isConnectingToInternet(context: Context): Boolean {

            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            val activeNetwork = cm.activeNetworkInfo

            return activeNetwork != null && activeNetwork.isConnectedOrConnecting
        }

//        fun isInternetAvailable(context : Context, message: String): BroadcastReceiver {
//
////            val noIntSnaBar = Snackbar.make(mParentLayout, message, Snackbar.LENGTH_INDEFINITE)
//
//
//            return object : BroadcastReceiver() {
//                override fun onReceive(context: Context, intent: Intent) {
//
//                    val extras = intent.extras
//                    val info = extras!!.getParcelable<Parcelable>("networkInfo") as NetworkInfo
//
//                    val state = info.state
//                    Log.d("BRODCAST RECEIVER", info.toString() + " " + state.toString())
//
//                    if (state == NetworkInfo.State.CONNECTED) {
//
//                        isInternetOn = true
//                    } else {
//
//                        isInternetOn = false
//                    }
//                }
//            }
//        }

    }

}