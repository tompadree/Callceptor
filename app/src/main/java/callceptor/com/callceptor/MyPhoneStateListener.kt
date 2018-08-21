package callceptor.com.callceptor;

import android.content.Context
import android.telephony.*
import android.util.Log
import android.telephony.TelephonyManager
import callceptor.com.callceptor.MyPhoneStateListener.ITelephony
import java.lang.reflect.AccessibleObject.setAccessible





/**
 * Created by Tomislav on 21,August,2018
 */
class MyPhoneStateListener(private val context : Context) : PhoneStateListener() {

    var LOG_TAG = "PHONE_TAG"
    var tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
//    var telService : ITelephony = Class.forName(tm.getClass().getName())


    override fun onCallStateChanged(state: Int, incomingNumber: String?) {
        super.onCallStateChanged(state, incomingNumber)


        when (state) {
            TelephonyManager.CALL_STATE_IDLE -> Log.i(LOG_TAG, "onCallStateChanged: CALL_STATE_IDLE");
            TelephonyManager.CALL_STATE_RINGING -> {
                Log.i(LOG_TAG, "onCallStateChanged: CALL_STATE_RINGING");


                val m = tm.javaClass.getDeclaredMethod("getITelephony")
                m.isAccessible = true

                val iTelephony = m.invoke(tm)
                val endCall = iTelephony.javaClass.getDeclaredMethod("endCall");

                endCall.invoke(iTelephony)

                Log.i(LOG_TAG, "onCallStateChanged: CALL_ENDED");
            }


//                TelephonyManager.ACTION_RESPOND_VIA_MESSAGE //Log.i(LOG_TAG, "onCallStateChanged: CALL_STATE_RINGING");
            TelephonyManager.CALL_STATE_OFFHOOK -> Log.i(LOG_TAG, "onCallStateChanged: CALL_STATE_OFFHOOK");
            else -> Log.i(LOG_TAG, "UNKNOWN_STATE: " + state)
        }
    }

    override fun onCellLocationChanged(location: CellLocation?) {
        super.onCellLocationChanged(location)
    }

    override fun onSignalStrengthsChanged(signalStrength: SignalStrength?) {
        super.onSignalStrengthsChanged(signalStrength)
    }

    override fun onDataActivity(direction: Int) {
        super.onDataActivity(direction)
    }

    override fun onCellInfoChanged(cellInfo: MutableList<CellInfo>?) {
        super.onCellInfoChanged(cellInfo)
    }

    override fun onMessageWaitingIndicatorChanged(mwi: Boolean) {
        super.onMessageWaitingIndicatorChanged(mwi)
    }

    override fun onCallForwardingIndicatorChanged(cfi: Boolean) {
        super.onCallForwardingIndicatorChanged(cfi)
    }

    override fun onDataConnectionStateChanged(state: Int) {
        super.onDataConnectionStateChanged(state)
    }

    override fun onDataConnectionStateChanged(state: Int, networkType: Int) {
        super.onDataConnectionStateChanged(state, networkType)
    }

    override fun onSignalStrengthChanged(asu: Int) {
        super.onSignalStrengthChanged(asu)
    }

    override fun onServiceStateChanged(serviceState: ServiceState?) {
        super.onServiceStateChanged(serviceState)
    }

    interface ITelephony {

        fun endCall(): Boolean

        fun answerRingingCall()

        fun silenceRinger()
    }

}

