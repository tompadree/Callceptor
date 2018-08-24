package callceptor.com.callceptor.telephony

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.telephony.TelephonyManager
import android.telecom.TelecomManager
import callceptor.com.callceptor.data.models.Call
import callceptor.com.callceptor.domain.listeners.OnCallContactsFetched
import com.android.internal.telephony.ITelephony
import java.util.ArrayList
import android.provider.ContactsContract.CommonDataKinds
import android.provider.ContactsContract.PhoneLookup
import android.net.Uri
import android.provider.Settings


/**
 * Created by Tomislav on 21,August,2018
 */
class MyPhoneStateManager : BroadcastReceiver(), OnCallContactsFetched {

    var LOG_TAG = "PHONE_TAG"

    var contactNumbers: ArrayList<String> = ArrayList()
    override fun callLogsFetched(list: ArrayList<Call>) {}
    override fun contactsFetched(list: ArrayList<String>) {
        contactNumbers = list
    }

    override fun onFetchingError(e: Throwable) {}


    override fun onReceive(context: Context?, intent: Intent?) {

        try {
//            if(intent?.action.equals("android.provider.Telephony.SMS_RECEIVED")) {
//                if (intent?.extras != null) {
//                    var test = intent.extras
//                }
//            }
            when (intent?.getStringExtra(TelephonyManager.EXTRA_STATE)) {
                TelephonyManager.EXTRA_STATE_RINGING -> {
                    Log.i(LOG_TAG, "onCallStateChanged: RINGING");
                    processNumber(context, intent?.extras?.getString(TelephonyManager.EXTRA_INCOMING_NUMBER))
                }
                TelephonyManager.EXTRA_STATE_OFFHOOK -> {
                    Log.i(LOG_TAG, "onCallStateChanged: ANSWERED");
                    if (Settings.canDrawOverlays(context))
                        context?.stopService(Intent(context, HarmfulCallAlertService::class.java))
                }
                TelephonyManager.EXTRA_STATE_IDLE -> {
                    Log.i(LOG_TAG, "onCallStateChanged: IDLE");
                    if (Settings.canDrawOverlays(context))
                        context?.stopService(Intent(context, HarmfulCallAlertService::class.java))
                }
            }

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
                val iTelephony = tm.javaClass.getDeclaredMethod("getITelephony")

                iTelephony.isAccessible = true
                val telephonyService = iTelephony.invoke(tm) as ITelephony


                if (number.equals("4259501212") || number.equals("+38516043663") || number.equals("+385989436165")) {
                    if (Settings.canDrawOverlays(context))
                        context.startService(Intent(context, HarmfulCallAlertService::class.java))
                } else if (telephonyService != null && number != null && !isFromContacts(context, number)) {
                    telephonyService.endCall()
                    Log.i(LOG_TAG, "onCallStateChanged: CALL_ENDED OTHERS")
                }

            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun isFromContacts(context: Context, number: String): Boolean {

        var res: String? = null
        try {
            val resolver = context.contentResolver
            val uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number))
            val c = resolver.query(uri, arrayOf(PhoneLookup.DISPLAY_NAME), null, null, null)

            if (c != null) {
                if (c.moveToFirst()) {
                    res = c.getString(c.getColumnIndex(CommonDataKinds.Phone.DISPLAY_NAME))
                }
                c.close()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return res != null
    }

}

