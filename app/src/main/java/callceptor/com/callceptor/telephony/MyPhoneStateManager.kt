package callceptor.com.callceptor.telephony;

import android.content.Context
import android.telephony.*
import android.util.Log
import android.telephony.TelephonyManager


/**
 * Created by Tomislav on 21,August,2018
 */
class MyPhoneStateManager(private val context : Context) : PhoneStateListener() {

    var LOG_TAG = "PHONE_TAG"
    var tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
//    var telService : ITelephony = Class.forName(tm.getClass().getName())


    override fun onCallStateChanged(state: Int, incomingNumber: String?) {
        super.onCallStateChanged(state, incomingNumber)


        when (state) {
            TelephonyManager.CALL_STATE_IDLE -> Log.i(LOG_TAG, "onCallStateChanged: CALL_STATE_IDLE");
            TelephonyManager.CALL_STATE_RINGING -> {
                
                
                processNumber()
                
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

    private fun processNumber(number : String) {

        
        
    }
    

    
}

