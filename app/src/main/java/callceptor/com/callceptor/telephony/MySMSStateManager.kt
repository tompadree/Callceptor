package callceptor.com.callceptor.telephony

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * Created by Tom on 23.8.2018..
 */
class MySMSStateManager : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        try {
            if (intent?.action.equals("android.provider.Telephony.SMS_RECEIVED")) {
                if (intent?.extras != null) {
                    var test = intent.extras
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

}