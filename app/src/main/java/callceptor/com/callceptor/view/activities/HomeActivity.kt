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
import callceptor.com.callceptor.telephony.MyPhoneStateReceiver
import callceptor.com.callceptor.view.BaseActivity
import callceptor.com.callceptor.view.fragments.CallsFragment
import callceptor.com.callceptor.view.fragments.MessagesFragment
import callceptor.com.callceptor.view.fragments.SettingsFragment
import kotlinx.android.synthetic.main.activity_home.*
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.support.annotation.RequiresApi


class HomeActivity : BaseActivity() {

    lateinit var phoneStateManager: MyPhoneStateReceiver
    private var callsFragmentInstance = CallsFragment()
    private var messagesFragmentInstance = MessagesFragment()
    private var settingsFragmentInstance = SettingsFragment()
    private var activeFrag: Fragment = callsFragmentInstance

    val PERMISSION_REQ_CODE = 1234
    val PERMISSIONS_PHONE_BEFORE_P = arrayOf(Manifest.permission.READ_PHONE_STATE, Manifest.permission.CALL_PHONE, Manifest.permission.READ_CALL_LOG, Manifest.permission.READ_CONTACTS, Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS) //, Manifest.permission.SEND_SMS, Manifest.permission.READ_SMS, Manifest.permission.SYSTEM_ALERT_WINDOW)
    val PERMISSIONS_AFTER_P = arrayOf(Manifest.permission.ANSWER_PHONE_CALLS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_CALL_LOG, Manifest.permission.CALL_PHONE, Manifest.permission.READ_CONTACTS, Manifest.permission.SYSTEM_ALERT_WINDOW, Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS) // Manifest.permission.RECEIVE_SMS, Manifest.permission.SEND_SMS,

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setBottomMenu()

    }

    override fun onResume() {
        super.onResume()


    }

    override fun onDestroy() {
        super.onDestroy()

        unregisterReceiver()
    }

    private fun unregisterReceiver() {
        this.unregisterReceiver()
        this.unregisterReceiver()
    }

//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//        //Clear the Activity's bundle of the subsidiary fragments' bundles.
//        outState.clear()
//    }

    private fun setBottomMenu() {

        homeActivityBottomNavigation.enableAnimation(false)
        homeActivityBottomNavigation.enableShiftingMode(false)
        homeActivityBottomNavigation.enableItemShiftingMode(false)
        homeActivityBottomNavigation.setTextVisibility(false)

//        addFragment(messagesFragmentInstance, FragmentTag.MessagesFragment.getTag())
//        supportFragmentManager.beginTransaction().hide(messagesFragmentInstance).commit()

        addFragment(callsFragmentInstance, FragmentTag.CallsFragment.getTag())
        homeActivityBottomNavigation.selectedItemId = R.id.action_calls

//        addFragment(settingsFragmentInstance, FragmentTag.SettingsFragment.getTag())
//        supportFragmentManager.beginTransaction().hide(settingsFragmentInstance).commit()


        homeActivityBottomNavigation.setOnNavigationItemSelectedListener { item ->
            // lastItem = homeActivityBottomNavigation.currentItem
            when (item.itemId) {
                R.id.action_messages -> {

                    if (!messagesFragmentInstance.isAdded)
                        addFragment(messagesFragmentInstance, FragmentTag.MessagesFragment.getTag())

                    supportFragmentManager.beginTransaction().hide(activeFrag).show(messagesFragmentInstance).commit()
                    activeFrag = messagesFragmentInstance

                }// messagesClicked()
                R.id.action_calls -> {

                    if (activeFrag != callsFragmentInstance)
                        supportFragmentManager.beginTransaction().show(callsFragmentInstance).hide(activeFrag).commit()
                    else {
//                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M)
//                            supportFragmentManager.beginTransaction().hide(activeFrag).show(callsFragmentInstance).commit()
//                        else
                            supportFragmentManager.beginTransaction().hide(activeFrag).show(callsFragmentInstance).commitAllowingStateLoss()
                    }
                    activeFrag = callsFragmentInstance

                }//callsClicked()
                R.id.action_settings -> {

                    if (!settingsFragmentInstance.isAdded)
                        addFragment(settingsFragmentInstance, FragmentTag.SettingsFragment.getTag())

                    supportFragmentManager.beginTransaction().hide(activeFrag).show(settingsFragmentInstance).commit()
                    activeFrag = settingsFragmentInstance

                } //settingsClicked()
            }
            true
        }

        checkPermissions()

    }

//    private fun messagesClicked() {
//        setFragment(messagesFragmentInstance, FragmentTag.MessagesFragment.getTag())
//    }
//
//    private fun callsClicked() {
//        setFragment(callsFragmentInstance, FragmentTag.CallsFragment.getTag())
//    }
//
//    private fun settingsClicked() {
//        setFragment(settingsFragmentInstance, FragmentTag.SettingsFragment.getTag())
//    }
//
//
//    private fun setFragment(currentFragment: Fragment, fragmentTag: String) {
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
//            supportFragmentManager
//                    .beginTransaction()
//                    .replace(R.id.homeActivityContainer, currentFragment, fragmentTag)
//                    .commitAllowingStateLoss()
//        else
//            supportFragmentManager
//                    .beginTransaction()
//                    .replace(R.id.homeActivityContainer, currentFragment, fragmentTag)
//                    .commit()
//
//    }

    private fun addFragment(currentFragment: Fragment, fragmentTag: String) {

        supportFragmentManager
                .beginTransaction()
                .add(R.id.homeActivityContainer, currentFragment, fragmentTag)
                .commit()

    }

    private fun registerReceiver() {

        phoneStateManager = MyPhoneStateReceiver()
        this.registerReceiver(phoneStateManager, IntentFilter("android.intent.action.PHONE_STATE"))

//        val mySMSStateManager = MySMSStateManager()
        val intFilter = IntentFilter("android.provider.Telephony.SMS_RECEIVED")
        intFilter.priority = 100
        this.registerReceiver(phoneStateManager, intFilter)
    }

    private fun checkPermissions() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            if ((checkSelfPermission(Manifest.permission.SYSTEM_ALERT_WINDOW) == PackageManager.PERMISSION_DENIED)
                    || (!Settings.canDrawOverlays(this)))
                checkDrawOverlayPermission()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT <= Build.VERSION_CODES.O_MR1) {

            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_DENIED
                    || checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_DENIED
                    || checkSelfPermission(Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_DENIED
                    || checkSelfPermission(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_DENIED
                    || checkSelfPermission(Manifest.permission.READ_SMS) == PackageManager.PERMISSION_DENIED)
                requestPermissions(PERMISSIONS_PHONE_BEFORE_P, PERMISSION_REQ_CODE)
            else
                registerReceiver()

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT > Build.VERSION_CODES.O_MR1) {

            if (checkSelfPermission(Manifest.permission.ANSWER_PHONE_CALLS) == PackageManager.PERMISSION_DENIED
                    || checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_DENIED
                    || checkSelfPermission(Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_DENIED
                    || checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_DENIED
                    || checkSelfPermission(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_DENIED
                    || checkSelfPermission(Manifest.permission.READ_SMS) == PackageManager.PERMISSION_DENIED
                    || checkSelfPermission(Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_DENIED)
                requestPermissions(PERMISSIONS_AFTER_P, PERMISSION_REQ_CODE)
            else
                registerReceiver()

        }

    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun checkDrawOverlayPermission() {
        if (!Settings.canDrawOverlays(this)) {
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:$packageName"))
            startActivityForResult(intent, 12345)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 12345) {
            if (Settings.canDrawOverlays(this)) {
//                startService(Intent(this, HarmfulCallAlertService::class.java))
            }
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            PERMISSION_REQ_CODE -> {

                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    homeActivityBottomNavigation.selectedItemId = R.id.action_calls
                    registerReceiver()

                    Toast.makeText(this, "Permission granted" /*+ PERMISSIONS_PHONE*/, Toast.LENGTH_SHORT).show()

                } else {
                    Toast.makeText(this, "Permission NOT granted"/* + PERMISSIONS_PHONE*/, Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

}

