package callceptor.com.callceptor.telephony

import android.Manifest
import android.content.pm.PackageManager
import android.database.Cursor
import android.provider.CallLog
import android.content.Context
import android.os.Build
import android.provider.ContactsContract
import android.provider.Telephony
import android.support.v4.content.ContextCompat
import callceptor.com.callceptor.data.models.Call
import callceptor.com.callceptor.data.models.Message
import callceptor.com.callceptor.domain.listeners.SystemDataManager
import callceptor.com.callceptor.utils.AppConstants
import callceptor.com.callceptor.utils.CheckNumberContacts
import com.cinnamon.utils.storage.CinnamonPreferences
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by Tom on 25.8.2018..
 */
class SystemDataManagerImpl(private val context: Context) : SystemDataManager {

    override fun getContacts(): ArrayList<String> {

        var contactsNumberList: ArrayList<String> = ArrayList()
        val cr = context.contentResolver
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED) {
            val cur: Cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null)

            while (cur.moveToNext()) {

                val number = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                if (!contactsNumberList.contains(number))
                    contactsNumberList.add(number)
            }
            cur.close()
        }

        return contactsNumberList
    }


    override fun getLastCall(): Call {
        var call: Call? = Call()

        try {
            val cr = context.contentResolver
            val strOrder = CallLog.Calls.DATE + " DESC"
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED) {
                val cur: Cursor = cr.query(CallLog.Calls.CONTENT_URI, null, null, null, strOrder)
                cur.moveToFirst()

                if (cur.getInt(cur.getColumnIndex(CallLog.Calls.TYPE)) != 2) {
                    call?.date = SimpleDateFormat("HH:mm  dd.MM.yyyy.", Locale.ITALY).format(Timestamp(cur.getLong(cur.getColumnIndex(CallLog.Calls.DATE))))

                    call?.timestamp = cur.getLong(cur.getColumnIndex(CallLog.Calls.DATE))
                    call?.name = cur.getString(cur.getColumnIndex(CallLog.Calls.CACHED_NAME))
                    call?.number = cur.getString(cur.getColumnIndex(CallLog.Calls.NUMBER))
                    call?.type = cur.getInt(cur.getColumnIndex(CallLog.Calls.TYPE))
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                        call?.photo_uri = cur.getString(cur.getColumnIndex(CallLog.Calls.CACHED_PHOTO_URI))

                    cur.close()
                }
//                Toast.makeText(context, "type: " + call?.type, Toast.LENGTH_SHORT).show()

                return call!!
            }


        } catch (e: Exception) {
            e.printStackTrace()
        }

        return call!!
    }


    override fun getCallLogs(): ArrayList<Call> {

        var list: ArrayList<Call> = ArrayList()

        try {
            val cr = context.contentResolver
            val strOrder = CallLog.Calls.DATE + " DESC"
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED) {
                val cur: Cursor = cr.query(CallLog.Calls.CONTENT_URI, null, null, null, strOrder)

                while (cur.moveToNext()) {

                    val call = Call()

                    call.date = SimpleDateFormat("HH:mm  dd.MM.yyyy.", Locale.ITALY).format(Timestamp(cur.getLong(cur.getColumnIndex(CallLog.Calls.DATE))))

                    call.timestamp = cur.getLong(cur.getColumnIndex(CallLog.Calls.DATE))
                    call.name = cur.getString(cur.getColumnIndex(CallLog.Calls.CACHED_NAME))
                    call.number = cur.getString(cur.getColumnIndex(CallLog.Calls.NUMBER))
                    call.type = cur.getInt(cur.getColumnIndex(CallLog.Calls.TYPE))
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                        call.photo_uri = cur.getString(cur.getColumnIndex(CallLog.Calls.CACHED_PHOTO_URI))

                    val blockList = (CinnamonPreferences.getInstance(context).getObject(AppConstants.BLOCK_LIST, List::class.java, java.util.ArrayList<String>())) as java.util.ArrayList<String>
                    if (call.type != 2 && (!CheckNumberContacts.isFromContacts(context, call.number!!) || blockList.contains(call.number!!)))
                        list.add(call)
                }

                cur.close()
            }

            return list

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return list

    }

    override fun getMessages(): ArrayList<Message> {

        var list: ArrayList<Message> = ArrayList()

        try {
            val cr = context.getContentResolver()
            val cur = cr.query(Telephony.Sms.CONTENT_URI, null, null, null, null)
            if (cur != null) {

                val cal = Calendar.getInstance()
                while (cur.moveToNext()) {

                    var message = Message()

                    message.date = SimpleDateFormat("HH:mm  MM.dd.yyyy.", Locale.ITALY).format(Timestamp(cur.getLong(cur.getColumnIndexOrThrow(Telephony.Sms.DATE))))
                    message.timestamp = cur.getString(cur.getColumnIndexOrThrow(Telephony.Sms.DATE))
                    message.number = cur.getString(cur.getColumnIndexOrThrow(Telephony.Sms.ADDRESS))
                    message.body = cur.getString(cur.getColumnIndexOrThrow(Telephony.Sms.BODY))

                    val blockList = (CinnamonPreferences.getInstance(context).getObject(AppConstants.BLOCK_LIST, List::class.java, java.util.ArrayList<String>())) as java.util.ArrayList<String>
                    if (Integer.parseInt(cur.getString(cur.getColumnIndexOrThrow(Telephony.Sms.TYPE))) == Telephony.Sms.MESSAGE_TYPE_INBOX
                            && (!CheckNumberContacts.isFromContacts(context, message.number!!) || blockList.contains(message.number!!)))
                        list.add(message)
                }
            }

            cur!!.close()
            return list


        } catch (e: Exception) {
            e.printStackTrace()

        }

        return list

    }

    override fun getLastMessage(): Message {
        var message = Message()

        try {
            val cr = context.getContentResolver()
            val cur = cr.query(Telephony.Sms.CONTENT_URI, null, null, null, null)
            if (cur != null) {
                cur.moveToFirst()

                message.number = cur.getString(cur.getColumnIndexOrThrow(Telephony.Sms.ADDRESS))
                val blockList = (CinnamonPreferences.getInstance(context).getObject(AppConstants.BLOCK_LIST, List::class.java, java.util.ArrayList<String>())) as java.util.ArrayList<String>

                if (Integer.parseInt(cur.getString(cur.getColumnIndexOrThrow(Telephony.Sms.TYPE))) == Telephony.Sms.MESSAGE_TYPE_INBOX
                        && (!CheckNumberContacts.isFromContacts(context, message.number!!) || blockList.contains(message.number!!))) {

                    message.date = SimpleDateFormat("HH:mm  MM.dd.yyyy.", Locale.ITALY).format(Timestamp(cur.getLong(cur.getColumnIndexOrThrow(Telephony.Sms.DATE))))
                    message.timestamp = cur.getString(cur.getColumnIndexOrThrow(Telephony.Sms.DATE))
                    message.body = cur.getString(cur.getColumnIndexOrThrow(Telephony.Sms.BODY))

                }
            }
            cur!!.close()
            return message

        } catch (e: Exception) {
            e.printStackTrace()

        }

        return message
    }

}