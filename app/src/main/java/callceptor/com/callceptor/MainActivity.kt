package callceptor.com.callceptor

import android.Manifest
import android.content.Context
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager

class MainActivity : AppCompatActivity() {

    lateinit var myPhoneStateListener : MyPhoneStateListener
    lateinit var telephonyManager : TelephonyManager

    val permissionRequestCode = 1234
    //    val PERMISSIONS_STORAGE = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    val PERMISSIONS_PHONE = arrayOf(Manifest.permission.CALL_PHONE, Manifest.permission.READ_PHONE_STATE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(PERMISSIONS_PHONE, permissionRequestCode)
        }
        telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        myPhoneStateListener = MyPhoneStateListener(this)

        telephonyManager.listen(myPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE)

    }

//    https://stackoverflow.com/questions/24580508/how-to-import-com-android-internal-telephony-itelephony-to-the-android-applicati

//    https://stackoverflow.com/questions/44650941/how-to-terminate-an-incoming-call-within-itelephony

//    https://stackoverflow.com/questions/19343028/intercepting-call-in-android?lq=1

//    https://www.truiton.com/2014/08/android-phonestatelistener-example/
//
//    https://stackoverflow.com/questions/13395633/add-phonestatelistener
//
//    https://stackoverflow.com/questions/1083527/how-to-block-calls-in-android

}
