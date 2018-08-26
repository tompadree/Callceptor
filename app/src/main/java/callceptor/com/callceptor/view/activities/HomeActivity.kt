package callceptor.com.callceptor.view.activities

import android.Manifest
import android.content.Context
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
import android.os.Handler
import android.provider.Settings
import android.support.annotation.RequiresApi
import callceptor.com.callceptor.data.models.Call
import callceptor.com.callceptor.data.models.Message
import callceptor.com.callceptor.di.component.DaggerAppComponent
import callceptor.com.callceptor.di.module.AppModule
import callceptor.com.callceptor.di.module.ThreadModule
import callceptor.com.callceptor.domain.listeners.LastCallSMSCheck
import callceptor.com.callceptor.domain.listeners.SystemDataManager
import io.reactivex.Scheduler
import java.util.*
import javax.inject.Inject
import javax.inject.Named
import kotlin.concurrent.schedule


class HomeActivity
//    @Inject constructor(private var context: Context)
    : BaseActivity(), LastCallSMSCheck {

    lateinit var phoneStateManager: MyPhoneStateReceiver
    private var callsFragmentInstance = CallsFragment()
    private var messagesFragmentInstance = MessagesFragment()
    private var settingsFragmentInstance = SettingsFragment()
    private var activeFrag: Fragment = callsFragmentInstance

    @Inject
    lateinit var systemDataManager: SystemDataManager

    val PERMISSION_REQ_CODE = 1234
    val PERMISSIONS_PHONE_BEFORE_P = arrayOf(Manifest.permission.READ_PHONE_STATE, Manifest.permission.CALL_PHONE, Manifest.permission.READ_CALL_LOG, Manifest.permission.READ_CONTACTS, Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS) //, Manifest.permission.SEND_SMS, Manifest.permission.READ_SMS, Manifest.permission.SYSTEM_ALERT_WINDOW)
    val PERMISSIONS_AFTER_P = arrayOf(Manifest.permission.ANSWER_PHONE_CALLS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_CALL_LOG, Manifest.permission.CALL_PHONE, Manifest.permission.READ_CONTACTS, Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS) // Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.RECEIVE_SMS, Manifest.permission.SEND_SMS,

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        DaggerAppComponent.builder()
                .appModule(AppModule(application))
                .build().inject(this)

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

//        if (!callsFragmentInstance.isAdded)
//            addFragment(callsFragmentInstance, FragmentTag.CallsFragment.getTag())
//        if (!messagesFragmentInstance.isAdded)
//            addFragment(messagesFragmentInstance, FragmentTag.MessagesFragment.getTag())
//        if (!settingsFragmentInstance.isAdded)
//            addFragment(settingsFragmentInstance, FragmentTag.SettingsFragment.getTag())
//        supportFragmentManager.beginTransaction().hide(settingsFragmentInstance).commit()


        homeActivityBottomNavigation.setOnNavigationItemSelectedListener { item ->
            // lastItem = homeActivityBottomNavigation.currentItem
            when (item.itemId) {
                R.id.action_messages -> {

                    if (!messagesFragmentInstance.isAdded)
                        addFragment(messagesFragmentInstance, FragmentTag.MessagesFragment.getTag())

                    supportFragmentManager.beginTransaction().hide(activeFrag).show(messagesFragmentInstance).commit()
                    activeFrag = messagesFragmentInstance
                    supportActionBar?.title = getString(R.string.messages_no_contacts)
                }// messagesClicked()
                R.id.action_calls -> {

                    if (!callsFragmentInstance.isAdded)
                        addFragment(callsFragmentInstance, FragmentTag.CallsFragment.getTag())

                    if (activeFrag != callsFragmentInstance)
                        supportFragmentManager.beginTransaction().show(callsFragmentInstance).hide(activeFrag).commit()
                    else {
//                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M)
//                            supportFragmentManager.beginTransaction().hide(activeFrag).show(callsFragmentInstance).commit()
//                        else
                        supportFragmentManager.beginTransaction().hide(activeFrag).show(callsFragmentInstance).commitAllowingStateLoss()
                    }
                    activeFrag = callsFragmentInstance
                    supportActionBar?.title = getString(R.string.incoming_calls)

                }//callsClicked()
                R.id.action_settings -> {

                    if (!settingsFragmentInstance.isAdded)
                        addFragment(settingsFragmentInstance, FragmentTag.SettingsFragment.getTag())

                    supportFragmentManager.beginTransaction().hide(activeFrag).show(settingsFragmentInstance).commit()
                    activeFrag = settingsFragmentInstance
                    supportActionBar?.title = getString(R.string.settings)


                } //settingsClicked()
            }
            true
        }

        checkPermissions()

    }

    private fun addFragment(currentFragment: Fragment, fragmentTag: String) {

        supportFragmentManager
                .beginTransaction()
                .add(R.id.homeActivityContainer, currentFragment, fragmentTag)
                .commitAllowingStateLoss()

    }

    private fun registerReceiver() {

        phoneStateManager = MyPhoneStateReceiver(this)
        this.registerReceiver(phoneStateManager, IntentFilter("android.intent.action.PHONE_STATE"))

//        val mySMSStateManager = MySMSStateManager()
        val intFilter = IntentFilter("android.provider.Telephony.SMS_RECEIVED")
        intFilter.priority = 100
        this.registerReceiver(phoneStateManager, intFilter)
    }

    override fun refreshCallList() {
        callsFragmentInstance.callsInteractor.saveLastCall(systemDataManager.getLastCall())
        callsFragmentInstance.localCalls = ArrayList()
        callsFragmentInstance.callsPresenter.fetchCallLogs()
    }

    override fun refreshSMSList() {
        if (!messagesFragmentInstance.isAdded) {
            addFragment(messagesFragmentInstance, FragmentTag.MessagesFragment.getTag())
            supportFragmentManager.beginTransaction().hide(messagesFragmentInstance).commit()
        }

        Handler().postDelayed({

        if (messagesFragmentInstance.isAdded) {
            messagesFragmentInstance.messageInteractor.saveLastMessage(systemDataManager.getLastMessage())
            messagesFragmentInstance.localMessages = ArrayList()
            messagesFragmentInstance.messagesPresenter.fetchMessages()
        }

        }, 500)

    }

    private fun checkPermissions() {

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
//            if ((checkSelfPermission(Manifest.permission.SYSTEM_ALERT_WINDOW) == PackageManager.PERMISSION_DENIED)
//                    || (!Settings.canDrawOverlays(this)))
//                checkDrawOverlayPermission()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT <= Build.VERSION_CODES.O_MR1) {

            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_DENIED
                    || checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_DENIED
                    || checkSelfPermission(Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_DENIED
                    || checkSelfPermission(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_DENIED
                    || checkSelfPermission(Manifest.permission.READ_SMS) == PackageManager.PERMISSION_DENIED)
                requestPermissions(PERMISSIONS_PHONE_BEFORE_P, PERMISSION_REQ_CODE)
//            else if ((checkSelfPermission(Manifest.permission.SYSTEM_ALERT_WINDOW) == PackageManager.PERMISSION_DENIED)
            else if (!Settings.canDrawOverlays(this))
                checkDrawOverlayPermission()
            // addFragment(callsFragmentInstance, FragmentTag.CallsFragment.getTag())
            else {
                homeActivityBottomNavigation.selectedItemId = R.id.action_calls
                registerReceiver()
            }

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT > Build.VERSION_CODES.O_MR1) {

            if (checkSelfPermission(Manifest.permission.ANSWER_PHONE_CALLS) == PackageManager.PERMISSION_DENIED
                    || checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_DENIED
                    || checkSelfPermission(Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_DENIED
                    || checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_DENIED
                    || checkSelfPermission(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_DENIED
                    || checkSelfPermission(Manifest.permission.READ_SMS) == PackageManager.PERMISSION_DENIED
                    || checkSelfPermission(Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_DENIED)
                requestPermissions(PERMISSIONS_AFTER_P, PERMISSION_REQ_CODE)
//            else if ((checkSelfPermission(Manifest.permission.SYSTEM_ALERT_WINDOW) == PackageManager.PERMISSION_DENIED)
            else if (!Settings.canDrawOverlays(this))
                checkDrawOverlayPermission()
            // addFragment(callsFragmentInstance, FragmentTag.CallsFragment.getTag())
            else {
                homeActivityBottomNavigation.selectedItemId = R.id.action_calls
                registerReceiver()

            }

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

                homeActivityBottomNavigation.selectedItemId = R.id.action_calls
                registerReceiver()
            }
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            PERMISSION_REQ_CODE -> {

                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                        if ((checkSelfPermission(Manifest.permission.SYSTEM_ALERT_WINDOW) == PackageManager.PERMISSION_DENIED)
                                || (!Settings.canDrawOverlays(this)))
                            checkDrawOverlayPermission()

//                    Toast.makeText(this, "Permission granted" /*+ PERMISSIONS_PHONE*/, Toast.LENGTH_SHORT).show()

                } else {
                    Toast.makeText(this, "Permission NOT granted"/* + PERMISSIONS_PHONE*/, Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

}

