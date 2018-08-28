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
import android.provider.ContactsContract.CommonDataKinds
import android.provider.ContactsContract.PhoneLookup
import android.net.Uri
import android.os.Handler
import android.provider.BlockedNumberContract
import android.provider.Settings
import android.provider.Telephony
import android.widget.Toast
import callceptor.com.callceptor.data.repositories.calls.LocalCallsDataStore
import callceptor.com.callceptor.domain.interactors.CallsInteractor
import callceptor.com.callceptor.domain.interactors.MessageInteractor
import callceptor.com.callceptor.domain.interactors.impl.CallsInteractorImpl
import callceptor.com.callceptor.domain.listeners.LastCallSMSCheck
import callceptor.com.callceptor.domain.listeners.SystemDataManager
import callceptor.com.callceptor.utils.AppConstants.Companion.BLOCK_LIST
import callceptor.com.callceptor.utils.CheckNumberContacts
import com.cinnamon.utils.storage.CinnamonPreferences
import java.util.*
import kotlin.concurrent.schedule
import javax.inject.Inject
import kotlin.collections.ArrayList


/**
 * Created by Tomislav on 21,August,2018
 */
class MyPhoneStateReceiver(private var lastCallSMSCheck: LastCallSMSCheck) : BroadcastReceiver(), OnCallContactsFetched {

    var LOG_TAG = "PHONE_TAG"
    var call = false
    private var blocklist: ArrayList<String>? = null

    var contactNumbers: ArrayList<String> = ArrayList()
    override fun callLogsFetched(list: ArrayList<Call>) {}
    override fun contactsFetched(list: ArrayList<String>) {
        contactNumbers = list
    }

    override fun onFetchingError(e: Throwable) {}


    override fun onReceive(context: Context?, intent: Intent?) {

//        if (blocklist == null)
        blocklist = ArrayList()
        blocklist = (CinnamonPreferences.getInstance(context).getObject(BLOCK_LIST, List::class.java, ArrayList<String>())) as ArrayList<String>


        try {
            if (intent?.action.equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {

                val smsMessages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
                for (message in smsMessages) {

                    lastCallSMSCheck.refreshSMSList()

                }
                abortBroadcast()
            }

            when (intent?.getStringExtra(TelephonyManager.EXTRA_STATE)) {
                TelephonyManager.EXTRA_STATE_RINGING -> {
                    processNumber(context, intent?.extras?.getString(TelephonyManager.EXTRA_INCOMING_NUMBER))
                    call = true
                }
                TelephonyManager.EXTRA_STATE_OFFHOOK -> {
                    if (Settings.canDrawOverlays(context))
                        context?.stopService(Intent(context, HarmfulCallAlertService::class.java))
                    call = true
                }
                TelephonyManager.EXTRA_STATE_IDLE -> {
                    /*TODO LOLLIPOP ?*/
                    if (Settings.canDrawOverlays(context))
                        context?.stopService(Intent(context, HarmfulCallAlertService::class.java))

                    if (call) {
                        call = false

                        Handler().postDelayed({ lastCallSMSCheck.refreshCallList() }, 500)
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
                if (number.equals("4259501212")) {
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


                if (number.equals("4259501212")) {
                    if (Settings.canDrawOverlays(context))
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

    override fun showLoadingFooter() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hideLoadingFooter() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showLoading() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hideLoading() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

