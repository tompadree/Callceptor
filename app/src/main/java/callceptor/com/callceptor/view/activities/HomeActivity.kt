package callceptor.com.callceptor.view.activities

import android.Manifest
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.widget.Toast
import callceptor.com.callceptor.R
import callceptor.com.callceptor.common.enums.FragmentTag
import callceptor.com.callceptor.telephony.MyPhoneStateManager
import callceptor.com.callceptor.view.BaseActivity
import callceptor.com.callceptor.view.fragments.CallsFragment
import callceptor.com.callceptor.view.fragments.MessagesFragment
import callceptor.com.callceptor.view.fragments.SettingsFragment
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : BaseActivity() {

    lateinit var phoneStateManager: MyPhoneStateManager

    val PERMISSION_REQ_CODE = 1234
    val PERMISSIONS_PHONE_BEFORE_P = arrayOf(Manifest.permission.READ_PHONE_STATE, Manifest.permission.CALL_PHONE, Manifest.permission.READ_CALL_LOG, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_CONTACTS)
    val PERMISSIONS_AFTER_P = arrayOf(Manifest.permission.ANSWER_PHONE_CALLS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_CALL_LOG, Manifest.permission.CALL_PHONE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_CONTACTS)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setBottomMenu()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT <= Build.VERSION_CODES.O_MR1) {

            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_DENIED
                    || checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_DENIED
                    || checkSelfPermission(Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_DENIED
                    || checkSelfPermission(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_DENIED) {
                requestPermissions(PERMISSIONS_PHONE_BEFORE_P, PERMISSION_REQ_CODE)

            } else {
//                homeActivityBottomNavigation.selectedItemId = R.id.action_calls
//                callsClicked()
                registerReceiver()
            }

        } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M && Build.VERSION.SDK_INT > Build.VERSION_CODES.O_MR1) {

            if (checkSelfPermission(Manifest.permission.ANSWER_PHONE_CALLS) == PackageManager.PERMISSION_DENIED
                    || checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_DENIED
                    || checkSelfPermission(Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_DENIED
                    || checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_DENIED
                    || checkSelfPermission(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_DENIED) {
                requestPermissions(PERMISSIONS_AFTER_P, PERMISSION_REQ_CODE)
            } else {
//                homeActivityBottomNavigation.selectedItemId = R.id.action_calls
//                callsClicked()
                registerReceiver()
            }
        }

    }

    private fun setBottomMenu() {

        homeActivityBottomNavigation.enableAnimation(false)
        homeActivityBottomNavigation.enableShiftingMode(false)
        homeActivityBottomNavigation.enableItemShiftingMode(false)
        homeActivityBottomNavigation.setTextVisibility(false)

        homeActivityBottomNavigation.setOnNavigationItemSelectedListener { item ->
            // lastItem = homeActivityBottomNavigation.currentItem
            when (item.itemId) {
                R.id.action_messages -> messagesClicked()
                R.id.action_calls -> callsClicked()
                R.id.action_settings -> settingsClicked()
            }
            true
        }

        homeActivityBottomNavigation.selectedItemId = R.id.action_calls
        callsClicked()

    }

    override fun onDestroy() {
        super.onDestroy()

        unregisterReceiver()
    }

    private fun unregisterReceiver() {

        this.unregisterReceiver()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        //Clear the Activity's bundle of the subsidiary fragments' bundles.
        outState.clear()
    }

    private fun messagesClicked() {
        setFragment(MessagesFragment.newInstance(), FragmentTag.MessagesFragment.getTag())
    }

    private fun callsClicked() {
        setFragment(CallsFragment.newInstance(), FragmentTag.CallsFragment.getTag())
    }

    private fun settingsClicked() {
        setFragment(SettingsFragment.newInstance(), FragmentTag.SettingsFragment.getTag())
    }

    private fun setFragment(currentFragment: Fragment, fragmentTag: String) {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.homeActivityContainer, currentFragment, fragmentTag)
                .commit()
    }

    private fun registerReceiver() {
        phoneStateManager = MyPhoneStateManager()
        this.registerReceiver(phoneStateManager, IntentFilter("android.intent.action.PHONE_STATE"))
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            PERMISSION_REQ_CODE -> {

                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    homeActivityBottomNavigation.selectedItemId = R.id.action_calls
                    callsClicked()
                    registerReceiver()

                    Toast.makeText(this, "Permission granted: " /*+ PERMISSIONS_PHONE*/, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission NOT granted: "/* + PERMISSIONS_PHONE*/, Toast.LENGTH_SHORT).show();
                }

            }
        }
    }
}