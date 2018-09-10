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
import android.os.Handler
import android.provider.Settings
import android.support.annotation.RequiresApi
import callceptor.com.callceptor.di.component.DaggerAppComponent
import callceptor.com.callceptor.di.module.AppModule
import callceptor.com.callceptor.domain.listeners.LastCallSMSCheck
import callceptor.com.callceptor.domain.listeners.SystemDataManager
import callceptor.com.callceptor.utils.AppConstants
import callceptor.com.callceptor.utils.CheckNumberContacts
import com.cinnamon.utils.storage.CinnamonPreferences
import java.util.*
import javax.inject.Inject


class HomeActivity : BaseActivity(), LastCallSMSCheck {

    lateinit var phoneStateManager: MyPhoneStateReceiver
    private var callsFragmentInstance = CallsFragment()
    private var messagesFragmentInstance = MessagesFragment()
    private var settingsFragmentInstance = SettingsFragment()
    private var activeFrag: Fragment = callsFragmentInstance

    @Inject
    lateinit var systemDataManager: SystemDataManager

    val PERMISSION_REQ_CODE = 1234
    val PERMISSIONS_PHONE_BEFORE_P = arrayOf(Manifest.permission.READ_PHONE_STATE, Manifest.permission.CALL_PHONE, Manifest.permission.READ_CALL_LOG, Manifest.permission.READ_CONTACTS, Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS)
    val PERMISSIONS_AFTER_P = arrayOf(Manifest.permission.ANSWER_PHONE_CALLS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_CALL_LOG, Manifest.permission.CALL_PHONE, Manifest.permission.READ_CONTACTS, Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS)

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

    private fun setBottomMenu() {

        homeActivityBottomNavigation.enableAnimation(false)
        homeActivityBottomNavigation.enableShiftingMode(false)
        homeActivityBottomNavigation.enableItemShiftingMode(false)
        homeActivityBottomNavigation.setTextVisibility(false)

        homeActivityBottomNavigation.setOnNavigationItemSelectedListener { item ->

            when (item.itemId) {
                R.id.action_messages -> {

                    if (!messagesFragmentInstance.isAdded)
                        addFragment(messagesFragmentInstance, FragmentTag.MessagesFragment.getTag())

                    supportFragmentManager.beginTransaction().hide(activeFrag).show(messagesFragmentInstance).commit()
                    activeFrag = messagesFragmentInstance
                    supportActionBar?.title = getString(R.string.messages_no_contacts)
                }
                R.id.action_calls -> {

                    if (!callsFragmentInstance.isAdded)
                        addFragment(callsFragmentInstance, FragmentTag.CallsFragment.getTag())

                    if (activeFrag != callsFragmentInstance)
                        supportFragmentManager.beginTransaction().show(callsFragmentInstance).hide(activeFrag).commit()
                    else {

                        supportFragmentManager.beginTransaction().hide(activeFrag).show(callsFragmentInstance).commitAllowingStateLoss()
                    }
                    activeFrag = callsFragmentInstance
                    supportActionBar?.title = getString(R.string.incoming_calls)
                }
                R.id.action_settings -> {

                    if (!settingsFragmentInstance.isAdded)
                        addFragment(settingsFragmentInstance, FragmentTag.SettingsFragment.getTag())

                    supportFragmentManager.beginTransaction().hide(activeFrag).show(settingsFragmentInstance).commit()
                    activeFrag = settingsFragmentInstance
                    supportActionBar?.title = getString(R.string.settings)

                }
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

        val intFilter = IntentFilter("android.provider.Telephony.SMS_RECEIVED")
        intFilter.priority = 100
        this.registerReceiver(phoneStateManager, intFilter)
    }

    override fun refreshCallList(lastNumber: String) {
        val blockList = (CinnamonPreferences.getInstance(this).getObject(AppConstants.BLOCK_LIST, List::class.java, ArrayList<String>())) as ArrayList<String>
        if (!CheckNumberContacts.isFromContacts(this, lastNumber) || blockList.contains(lastNumber)) {
            callsFragmentInstance.callsInteractor.saveLastCall(systemDataManager.getLastCall())
            callsFragmentInstance.localCalls = ArrayList()
            callsFragmentInstance.callsPresenter.fetchCallLogs(lastNumber)
        }
    }

    override fun refreshSMSList(lastNumber: String) {
        if (!messagesFragmentInstance.isAdded) {
            addFragment(messagesFragmentInstance, FragmentTag.MessagesFragment.getTag())
            supportFragmentManager.beginTransaction().hide(messagesFragmentInstance).commit()
        }
        val blockList = (CinnamonPreferences.getInstance(this).getObject(AppConstants.BLOCK_LIST, List::class.java, ArrayList<String>())) as ArrayList<String>

        if (messagesFragmentInstance.isAdded && (!CheckNumberContacts.isFromContacts(this, lastNumber) || blockList.contains(lastNumber))) {

            Handler().postDelayed({
                messagesFragmentInstance.messageInteractor.saveLastMessage(systemDataManager.getLastMessage())
                messagesFragmentInstance.localMessages = ArrayList()
                messagesFragmentInstance.messagesPresenter.fetchMessages(lastNumber)

            }, 500)

        }
    }

    private fun checkPermissions() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            homeActivityBottomNavigation.selectedItemId = R.id.action_calls
            registerReceiver()

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT <= Build.VERSION_CODES.O_MR1) {

            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_DENIED
                    || checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_DENIED
                    || checkSelfPermission(Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_DENIED
                    || checkSelfPermission(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_DENIED
                    || checkSelfPermission(Manifest.permission.READ_SMS) == PackageManager.PERMISSION_DENIED)
                requestPermissions(PERMISSIONS_PHONE_BEFORE_P, PERMISSION_REQ_CODE)

            else if (!Settings.canDrawOverlays(this))
                checkDrawOverlayPermission()

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

            else if (!Settings.canDrawOverlays(this))
                checkDrawOverlayPermission()

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

//        if (requestCode == 12345) {
//            if (Settings.canDrawOverlays(this)) {

                homeActivityBottomNavigation.selectedItemId = R.id.action_calls
                registerReceiver()
           // }
//        }
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

                } else {
                    Toast.makeText(this, getString(R.string.permission_not_granted), Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

}

