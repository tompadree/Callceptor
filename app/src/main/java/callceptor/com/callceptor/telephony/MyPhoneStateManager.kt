package callceptor.com.callceptor.telephony;

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.telephony.*
import android.util.Log
import android.telephony.TelephonyManager
import android.widget.Toast
import java.lang.reflect.AccessibleObject.setAccessible
import android.support.v4.app.NotificationCompat.getExtras
import android.telecom.TelecomManager
import com.android.internal.telephony.ITelephony


/**
 * Created by Tomislav on 21,August,2018
 */
class MyPhoneStateManager : BroadcastReceiver() {

    var LOG_TAG = "PHONE_TAG"
//    var tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
//    var telService : ITelephony = Class.forName(tm.getClass().getName())


//    override fun onCallStateChanged(state: Int, incomingNumber: String?) {
//        super.onCallStateChanged(state, incomingNumber)
//
//
//        when (state) {
//            TelephonyManager.CALL_STATE_IDLE -> Log.i(LOG_TAG, "onCallStateChanged: CALL_STATE_IDLE");
//            TelephonyManager.CALL_STATE_RINGING -> {
//
//
//                processNumber()
//
//                Log.i(LOG_TAG, "onCallStateChanged: CALL_STATE_RINGING");
//
//
//                val m = tm.javaClass.getDeclaredMethod("getITelephony")
//                m.isAccessible = true
//
//                val iTelephony = m.invoke(tm)
//                val endCall = iTelephony.javaClass.getDeclaredMethod("endCall");
//
//                endCall.invoke(iTelephony)
//
//                Log.i(LOG_TAG, "onCallStateChanged: CALL_ENDED");
//            }
//
//
////                TelephonyManager.ACTION_RESPOND_VIA_MESSAGE //Log.i(LOG_TAG, "onCallStateChanged: CALL_STATE_RINGING");
//            TelephonyManager.CALL_STATE_OFFHOOK -> Log.i(LOG_TAG, "onCallStateChanged: CALL_STATE_OFFHOOK");
//            else -> Log.i(LOG_TAG, "UNKNOWN_STATE: " + state)
//        }
//    }


    override fun onReceive(context: Context?, intent: Intent?) {

        try {
//            val state = intent?.getStringExtra(TelephonyManager.EXTRA_STATE)
//            val number = intent?.extras?.getString(TelephonyManager.EXTRA_INCOMING_NUMBER)

            when (intent?.getStringExtra(TelephonyManager.EXTRA_STATE)) {
                TelephonyManager.EXTRA_STATE_RINGING -> { Log.i(LOG_TAG, "onCallStateChanged: RINGING"); processNumber(context, intent?.extras?.getString(TelephonyManager.EXTRA_INCOMING_NUMBER)) }
                TelephonyManager.EXTRA_STATE_OFFHOOK -> Log.i(LOG_TAG, "onCallStateChanged: ANSWERED")
                TelephonyManager.EXTRA_STATE_IDLE -> Log.i(LOG_TAG, "onCallStateChanged: IDLE")
            }
//
//            if (state.equals(TelephonyManager.EXTRA_STATE_RINGING, ignoreCase = true)) {
//
//                Log.i(LOG_TAG, "onCallStateChanged: CALL_STATE_RINGING");
//
//            }
//            if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK, ignoreCase = true)) {
//                Log.i(LOG_TAG, "onCallStateChanged: ANSWERED")
//            }
//            if (state.equals(TelephonyManager.EXTRA_STATE_IDLE, ignoreCase = true)) {
//                Log.i(LOG_TAG, "onCallStateChanged: IDLE")
//            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun processNumber(context: Context?, number: String?) {

        try {

            if (Build.VERSION.SDK_INT >= 28) {
                val telecomManager = context?.getSystemService(Context.TELECOM_SERVICE) as TelecomManager
                if (telecomManager != null && number != null) {
                    telecomManager.endCall()
                    Log.i(LOG_TAG, "onCallStateChanged: CALL_ENDED PIE")
                }

            } else {

                val tm = context?.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                val m = tm.javaClass.getDeclaredMethod("getITelephony")

                m.isAccessible = true
                val telephonyService = m.invoke(tm) as ITelephony

                if (telephonyService != null && number != null) {
                    telephonyService.endCall()
                    Log.i(LOG_TAG, "onCallStateChanged: CALL_ENDED OTHERS");
//                            Toast.makeText(context, "Ending the call from: " + number!!, Toast.LENGTH_SHORT).show()
                }

            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun isFromContacts(){}


}

