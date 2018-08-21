package callceptor.com.callceptor.view.activities

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.widget.Toast
import callceptor.com.callceptor.domain.listeners.MyPhoneStateListener
import callceptor.com.callceptor.R

class MainActivity : BaseActivity() {

    lateinit var myPhoneStateListener: MyPhoneStateListener
    lateinit var telephonyManager: TelephonyManager

    val PERMISSION_REQ_CODE = 1234
    //    val PERMISSIONS_STORAGE = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    val PERMISSIONS_PHONE = arrayOf(Manifest.permission.READ_PHONE_STATE, Manifest.permission.CALL_PHONE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_DENIED || checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_DENIED) {
                requestPermissions(PERMISSIONS_PHONE, PERMISSION_REQ_CODE)
            }
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            PERMISSION_REQ_CODE -> {

                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                    myPhoneStateListener = MyPhoneStateListener(this)

                    telephonyManager.listen(myPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE)

                    Toast.makeText(this, "Permission granted: " + PERMISSIONS_PHONE, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission NOT granted: " + PERMISSIONS_PHONE, Toast.LENGTH_SHORT).show();
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


