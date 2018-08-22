package callceptor.com.callceptor.view.activities

import android.Manifest
import android.content.Context
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.widget.Toast
import callceptor.com.callceptor.R
import callceptor.com.callceptor.telephony.MyPhoneStateManager

class HomeActivity : BaseActivity() {

    // lateinit var myPhoneStateListener: MyPhoneStateListener
    lateinit var telephonyManager: TelephonyManager
    lateinit var phoneStateManager: MyPhoneStateManager

    val PERMISSION_REQ_CODE = 1234
    val PERMISSIONS_PHONE_BEFORE_P = arrayOf(Manifest.permission.READ_PHONE_STATE, Manifest.permission.CALL_PHONE)
    val PERMISSIONS_AFTER_P = arrayOf(Manifest.permission.ANSWER_PHONE_CALLS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_CALL_LOG, Manifest.permission.CALL_PHONE)

//

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT <= Build.VERSION_CODES.O_MR1) {

            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_DENIED
                    || checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_DENIED) {
                requestPermissions(PERMISSIONS_PHONE_BEFORE_P, PERMISSION_REQ_CODE)

            }
            else
                registerReceiver()

        } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O_MR1) {

            if (checkSelfPermission(Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_DENIED
                    || checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_DENIED) {
                requestPermissions(PERMISSIONS_AFTER_P, PERMISSION_REQ_CODE)
            }
            else registerReceiver()
        }

    }

    override fun onDestroy() {
        super.onDestroy()

        unregisterReceiver()
    }

    fun unregisterReceiver(){

        this.unregisterReceiver()
    }

    fun registerReceiver(){
        phoneStateManager = MyPhoneStateManager()
        this.registerReceiver(phoneStateManager, IntentFilter("android.intent.action.PHONE_STATE"))
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            PERMISSION_REQ_CODE -> {

                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    registerReceiver()

//                    telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
//                    myPhoneStateListener = MyPhoneStateListener(this)
//
//                    telephonyManager.listen(myPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE)
//
//                    phoneStateManager.

                    Toast.makeText(this, "Permission granted: " /*+ PERMISSIONS_PHONE*/, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission NOT granted: "/* + PERMISSIONS_PHONE*/, Toast.LENGTH_SHORT).show();
                }


            }
        }
    }
}

//    http://www.nikola-breznjak.com/blog/android/make-native-android-app-can-block-phone-calls/

//    https://stackoverflow.com/questions/24580508/how-to-import-com-android-internal-telephony-itelephony-to-the-android-applicati

//    https://stackoverflow.com/questions/44650941/how-to-terminate-an-incoming-call-within-itelephony

//    https://stackoverflow.com/questions/19343028/intercepting-call-in-android?lq=1

//    https://www.truiton.com/2014/08/android-phonestatelistener-example/
//
//    https://stackoverflow.com/questions/13395633/add-phonestatelistener
//
//    https://stackoverflow.com/questions/1083527/how-to-block-calls-in-android


