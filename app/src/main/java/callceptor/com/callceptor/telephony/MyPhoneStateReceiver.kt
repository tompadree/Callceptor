package callceptor.com.callceptor.telephony

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.telephony.TelephonyManager
import android.telecom.TelecomManager
import com.android.internal.telephony.ITelephony
import android.os.Handler
import android.provider.Settings
import android.provider.Telephony
import android.widget.Toast
import callceptor.com.callceptor.R
import callceptor.com.callceptor.domain.listeners.LastCallSMSCheck
import callceptor.com.callceptor.utils.AppConstants.Companion.BLOCK_LIST
import callceptor.com.callceptor.utils.CheckNumberContacts
import callceptor.com.callceptor.utils.NetworkHelper
import com.cinnamon.utils.storage.CinnamonPreferences
import kotlin.collections.ArrayList


/**
 * Created by Tomislav on 21,August,2018
 */
class MyPhoneStateReceiver(private var lastCallSMSCheck: LastCallSMSCheck) : BroadcastReceiver() {

    var call = false
    private var lastNumber: String? = ""
    private var blocklist: ArrayList<String>? = null

    override fun onReceive(context: Context?, intent: Intent?) {

        blocklist = ArrayList()
        blocklist = (CinnamonPreferences.getInstance(context).getObject(BLOCK_LIST, List::class.java, ArrayList<String>())) as ArrayList<String>


        try {
            if (intent?.action.equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {

                val smsMessages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
                for (message in smsMessages) {

                    if (!NetworkHelper.isConnectingToInternet(context!!))
                        Toast.makeText(context, R.string.no_internet, Toast.LENGTH_LONG).show()

                    lastCallSMSCheck.refreshSMSList(message.originatingAddress!!)

                }
                abortBroadcast()
            }

            when (intent?.getStringExtra(TelephonyManager.EXTRA_STATE)) {
                TelephonyManager.EXTRA_STATE_RINGING -> {
                    lastNumber = intent?.extras?.getString(TelephonyManager.EXTRA_INCOMING_NUMBER)
                    processNumber(context, lastNumber)
                    call = true
                }
                TelephonyManager.EXTRA_STATE_OFFHOOK -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (Settings.canDrawOverlays(context))
                            context?.stopService(Intent(context, HarmfulCallAlertService::class.java))
                    } else
                        context?.stopService(Intent(context, HarmfulCallAlertService::class.java))
                    call = true
                }
                TelephonyManager.EXTRA_STATE_IDLE -> {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (Settings.canDrawOverlays(context))
                            context?.stopService(Intent(context, HarmfulCallAlertService::class.java))
                    } else
                        context?.stopService(Intent(context, HarmfulCallAlertService::class.java))

                    if (call) {
                        call = false
                        Handler().postDelayed({
                            if (!NetworkHelper.isConnectingToInternet(context!!))
                                Toast.makeText(context, R.string.no_internet, Toast.LENGTH_LONG).show()
                            lastCallSMSCheck.refreshCallList(lastNumber!!)
                            lastNumber = ""
                        }, 1000)

                    }
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
                if (number.equals("4259501212") || number.equals("+14259501212") || number.equals("0014259501212")) {
                    if (Settings.canDrawOverlays(context))
                        context.startService(Intent(context, HarmfulCallAlertService::class.java))

                } else if (telecomManager != null
                        && number != null
                        && (!CheckNumberContacts.isFromContacts(context, number) || blocklist!!.contains(number))) {
                    telecomManager.endCall()
                }

            } else {

                val tm = context?.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                val iTelephony = tm.javaClass.getDeclaredMethod("getITelephony")

                iTelephony.isAccessible = true
                val telephonyService = iTelephony.invoke(tm) as ITelephony


                if (number.equals("4259501212") || number.equals("+14259501212") || number.equals("0014259501212")) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (Settings.canDrawOverlays(context))
                            context.startService(Intent(context, HarmfulCallAlertService::class.java))
                    } else
                        context.startService(Intent(context, HarmfulCallAlertService::class.java))
                } else if (telephonyService != null
                        && number != null
                        && (!CheckNumberContacts.isFromContacts(context, number) || blocklist!!.contains(number))) {
                    telephonyService.endCall()
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}

