package callceptor.com.callceptor.domain.listeners

import callceptor.com.callceptor.data.models.Call
import callceptor.com.callceptor.data.models.Message
import io.reactivex.Single

/**
 * Created by Tom on 25.8.2018..
 */
interface SystemDataManager {

    fun getContacts() : ArrayList<String>

    fun getCallLogs() : ArrayList<Call>

    fun getLastCall() : Call

    fun getMessages() : ArrayList<Message>

    fun getLastMessage() : Message
}