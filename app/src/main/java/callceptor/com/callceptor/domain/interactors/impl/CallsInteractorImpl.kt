package callceptor.com.callceptor.domain.interactors.impl

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import callceptor.com.callceptor.domain.interactors.CallsInteractor
import javax.inject.Inject
import android.provider.CallLog
import android.provider.ContactsContract
import android.support.v4.content.ContextCompat.checkSelfPermission
import android.util.Log
import callceptor.com.callceptor.data.models.Call
import callceptor.com.callceptor.domain.listeners.OnCallLogsFetched
import java.lang.reflect.Array
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Tom on 22.8.2018..
 */
class CallsInteractorImpl
@Inject constructor(private val context: Context) : CallsInteractor {


    @SuppressLint("Recycle")
    override fun getCallLogs(onCallLogsFetched: OnCallLogsFetched) {

        var list : ArrayList<Call> = ArrayList()


        val cr = context.contentResolver
        val strOrder = CallLog.Calls.DATE + " DESC"
        if (checkSelfPermission(context, Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED) {
            val cur: Cursor = cr.query(CallLog.Calls.CONTENT_URI, null, null, null,
                    strOrder)

            cur.moveToFirst()




            while(cur.moveToNext()){
             //   1 - dolazni, 2 - odlazni


                var call = Call()

                call.date = SimpleDateFormat("HH:mm dd.MM.yyyy.", Locale.ITALY).format(Timestamp(cur.getLong(cur.getColumnIndex(CallLog.Calls.DATE))))

                call.name = cur.getString(cur.getColumnIndex(CallLog.Calls.CACHED_NAME))
//                call.name = cur.getString(cur.getColumnIndex(CallLog.Calls.PHONE_ACCOUNT_COMPONENT_NAME))
                call.number = cur.getString(cur.getColumnIndex(CallLog.Calls.NUMBER))
                call.type = cur.getInt(cur.getColumnIndex(CallLog.Calls.TYPE))
                call.photo_uri = cur.getString(cur.getColumnIndex(CallLog.Calls.CACHED_PHOTO_URI))

                if(call.type != 5) // 1 for Incoming(1), Outgoing(2) and Missed(3), 4 (VoiceMail), 5 (Rejected) and 6 (Refused List)
                    list.add(call)
            }

        }

        onCallLogsFetched.callLogsFetched(list)
    }
}