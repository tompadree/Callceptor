package callceptor.com.callceptor.telephony

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.database.Cursor
import android.provider.CallLog
import android.content.Context
import android.provider.ContactsContract
import android.provider.Telephony
import android.support.v4.content.ContextCompat
import callceptor.com.callceptor.data.models.Call
import callceptor.com.callceptor.data.models.Message
import callceptor.com.callceptor.domain.listeners.OnCallContactsFetched
import callceptor.com.callceptor.domain.listeners.OnMessagesFetched
import callceptor.com.callceptor.domain.listeners.SystemDataManager
import callceptor.com.callceptor.utils.CheckNumberContacts
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
            cur.moveToFirst()
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
                    val cal = Calendar.getInstance()

                    val currentDay = SimpleDateFormat("ddMMyyyy", Locale.ITALY).format(cal.time)
                    val callDay = SimpleDateFormat("ddMMyyyy", Locale.ITALY).format(Timestamp(cur.getLong(cur.getColumnIndex(CallLog.Calls.DATE))))
//                    if (currentDay != callDay)
                    call?.date = SimpleDateFormat("HH:mm  dd.MM.yyyy.", Locale.ITALY).format(Timestamp(cur.getLong(cur.getColumnIndex(CallLog.Calls.DATE))))
//                    else
//                        call?.date = SimpleDateFormat("HH:mm", Locale.ITALY).format(Timestamp(cur.getLong(cur.getColumnIndex(CallLog.Calls.DATE))))

                    call?.timestamp = cur.getLong(cur.getColumnIndex(CallLog.Calls.DATE))
                    call?.name = cur.getString(cur.getColumnIndex(CallLog.Calls.CACHED_NAME))
                    call?.number = cur.getString(cur.getColumnIndex(CallLog.Calls.NUMBER))
                    call?.type = cur.getInt(cur.getColumnIndex(CallLog.Calls.TYPE))
                    call?.photo_uri = cur.getString(cur.getColumnIndex(CallLog.Calls.CACHED_PHOTO_URI))

//                    if (call.type != 2) // 1 for Incoming(1), Outgoing(2) and Missed(3), 4 (VoiceMail), 5 (Rejected) and 6 (Refused List)
//                        call.add(call)
                    //    }

                    cur.close()
                }
                return call!!
                // }
            }


        } catch (e: Exception) {
            e.printStackTrace()
            //onCallContactsFetched.onFetchingError(e)
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
                cur.moveToFirst()

                val cal = Calendar.getInstance()
                while (cur.moveToNext()) {

                    var call = Call()
                    val currentDay = SimpleDateFormat("ddMMyyyy", Locale.ITALY).format(cal.time)
                    val callDay = SimpleDateFormat("ddMMyyyy", Locale.ITALY).format(Timestamp(cur.getLong(cur.getColumnIndex(CallLog.Calls.DATE))))
//                    if (currentDay != callDay)
                    call.date = SimpleDateFormat("HH:mm  dd.MM.yyyy.", Locale.ITALY).format(Timestamp(cur.getLong(cur.getColumnIndex(CallLog.Calls.DATE))))
//                    else
//                        call.date = SimpleDateFormat("HH:mm", Locale.ITALY).format(Timestamp(cur.getLong(cur.getColumnIndex(CallLog.Calls.DATE))))

                    call.timestamp = cur.getLong(cur.getColumnIndex(CallLog.Calls.DATE))
                    call.name = cur.getString(cur.getColumnIndex(CallLog.Calls.CACHED_NAME))
                    call.number = cur.getString(cur.getColumnIndex(CallLog.Calls.NUMBER))
                    call.type = cur.getInt(cur.getColumnIndex(CallLog.Calls.TYPE))
                    call.photo_uri = cur.getString(cur.getColumnIndex(CallLog.Calls.CACHED_PHOTO_URI))

                    if (call.type != 2) // 1 for Incoming(1), Outgoing(2) and Missed(3), 4 (VoiceMail), 5 (Rejected) and 6 (Refused List)
                        list.add(call)
                }

                cur.close()
            }

            return list

        } catch (e: Exception) {
            e.printStackTrace()
            //onCallContactsFetched.onFetchingError(e)
        }

        return list

    }

    override fun getMessages(): ArrayList<Message> {

        var list: ArrayList<Message> = ArrayList()

        try {
            val cr = context.getContentResolver()
            val cur = cr.query(Telephony.Sms.CONTENT_URI, null, null, null, null)
            if (cur != null) {
                cur.moveToFirst()

                val cal = Calendar.getInstance()
                while (cur.moveToNext()) {

                    var message = Message()

                    val currentDay = SimpleDateFormat("ddMMyyyy", Locale.ITALY).format(cal.time)
                    val callDay = SimpleDateFormat("ddMMyyyy", Locale.ITALY).format(Timestamp(cur.getLong(cur.getColumnIndex(CallLog.Calls.DATE))))
//                    if (currentDay != callDay)
                    message.date = SimpleDateFormat("HH:mm  MM.dd.yyyy.", Locale.ITALY).format(Timestamp(cur.getLong(cur.getColumnIndexOrThrow(Telephony.Sms.DATE))))
//                    else
//                        message.date = SimpleDateFormat("HH:mm", Locale.ITALY).format(Timestamp(cur.getLong(cur.getColumnIndexOrThrow(Telephony.Sms.DATE))))

                    message.timestamp = cur.getString(cur.getColumnIndexOrThrow(Telephony.Sms.DATE))
                    message.number = cur.getString(cur.getColumnIndexOrThrow(Telephony.Sms.ADDRESS))
                    message.body = cur.getString(cur.getColumnIndexOrThrow(Telephony.Sms.BODY))

                    if (Integer.parseInt(cur.getString(cur.getColumnIndexOrThrow(Telephony.Sms.TYPE))) == Telephony.Sms.MESSAGE_TYPE_INBOX
                            && !CheckNumberContacts.isFromContacts(context, message.number!!))
                        list.add(message)
                }
            }

            cur!!.close()
            return list


        } catch (e: Exception) {
            e.printStackTrace()

            // return e
            // onMessagesFetched.onFetchingError(e)
        }

        return list

    }

    override fun getLastMessage(): Message {
        var message: Message = Message()

        try {
            val cr = context.getContentResolver()
            val cur = cr.query(Telephony.Sms.CONTENT_URI, null, null, null, null)
            if (cur != null) {
                cur.moveToFirst()

                val cal = Calendar.getInstance()

                message.number = cur.getString(cur.getColumnIndexOrThrow(Telephony.Sms.ADDRESS))

                if (Integer.parseInt(cur.getString(cur.getColumnIndexOrThrow(Telephony.Sms.TYPE))) == Telephony.Sms.MESSAGE_TYPE_INBOX
                        && !CheckNumberContacts.isFromContacts(context, message.number!!)) {

                    val currentDay = SimpleDateFormat("ddMMyyyy", Locale.ITALY).format(cal.time)
                    val callDay = SimpleDateFormat("ddMMyyyy", Locale.ITALY).format(Timestamp(cur.getLong(cur.getColumnIndex(CallLog.Calls.DATE))))
//                    if (currentDay != callDay)
                    message.date = SimpleDateFormat("HH:mm  MM.dd.yyyy.", Locale.ITALY).format(Timestamp(cur.getLong(cur.getColumnIndexOrThrow(Telephony.Sms.DATE))))
//                    else
//                        message.date = SimpleDateFormat("HH:mm", Locale.ITALY).format(Timestamp(cur.getLong(cur.getColumnIndexOrThrow(Telephony.Sms.DATE))))

                    message.timestamp = cur.getString(cur.getColumnIndexOrThrow(Telephony.Sms.DATE))

                    message.body = cur.getString(cur.getColumnIndexOrThrow(Telephony.Sms.BODY))


                }
            }
            cur!!.close()
            return message

        } catch (e: Exception) {
            e.printStackTrace()

            // return e
            // onMessagesFetched.onFetchingError(e)
        }

        return message
    }

}