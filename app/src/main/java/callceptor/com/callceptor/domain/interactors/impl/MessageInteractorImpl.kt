package callceptor.com.callceptor.domain.interactors.impl

import callceptor.com.callceptor.domain.interactors.MessageInteractor
import callceptor.com.callceptor.domain.listeners.OnMessagesFetched
import javax.inject.Inject
import android.widget.Toast
import android.provider.Telephony
import android.content.ContentResolver
import android.content.Context
import android.provider.CallLog
import callceptor.com.callceptor.data.models.Message
import callceptor.com.callceptor.utils.CheckNumberContacts
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by Tom on 24.8.2018..
 */
class MessageInteractorImpl
@Inject constructor(private val context: Context) : MessageInteractor {

    override fun getMessages(onMessagesFetched: OnMessagesFetched) {

        var list: ArrayList<Message> = ArrayList()

        try {
            val cr = context.getContentResolver()
            val cur = cr.query(Telephony.Sms.CONTENT_URI, null, null, null, null)
            if (cur != null) {
                cur.moveToFirst()
                cur.moveToFirst()
                val cal = Calendar.getInstance()
                while (cur.moveToNext()) {

                    var message = Message()

                    val currentDay = SimpleDateFormat("ddMMyyyy", Locale.ITALY).format(cal.time)
                    val callDay = SimpleDateFormat("ddMMyyyy", Locale.ITALY).format(Timestamp(cur.getLong(cur.getColumnIndex(CallLog.Calls.DATE))))
                    if (currentDay != callDay)
                        message.date = SimpleDateFormat("HH:mm  MM.dd.yyyy.", Locale.ITALY).format(Timestamp(cur.getLong(cur.getColumnIndexOrThrow(Telephony.Sms.DATE))))
                    else
                        message.date = SimpleDateFormat("HH:mm", Locale.ITALY).format(Timestamp(cur.getLong(cur.getColumnIndexOrThrow(Telephony.Sms.DATE))))


//                    message.date = cur.getString(cur.getColumnIndexOrThrow(Telephony.Sms.DATE))
                    message.number = cur.getString(cur.getColumnIndexOrThrow(Telephony.Sms.ADDRESS))
                    message.body = cur.getString(cur.getColumnIndexOrThrow(Telephony.Sms.BODY))

                    if (Integer.parseInt(cur.getString(cur.getColumnIndexOrThrow(Telephony.Sms.TYPE))) == Telephony.Sms.MESSAGE_TYPE_INBOX
                            && !CheckNumberContacts.isFromContacts(context, message.number!!))
                        list.add(message)
                }
            }
            onMessagesFetched.messagesFetched(list)
            cur!!.close()

        } catch (e : Exception){
            e.printStackTrace()
            onMessagesFetched.onFetchingError(e)
        }

    }
}