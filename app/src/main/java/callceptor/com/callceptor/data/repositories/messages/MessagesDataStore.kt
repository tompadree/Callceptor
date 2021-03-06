package callceptor.com.callceptor.data.repositories.messages

import callceptor.com.callceptor.data.models.CNAMObject
import callceptor.com.callceptor.data.models.Message
import io.reactivex.Flowable
import io.reactivex.Single

/**
 * Created by Tom on 25.8.2018..
 */
interface MessagesDataStore {

    fun saveAllMessages(messages: ArrayList<Message>): Single<LongArray>

    fun saveLastMessage(message: Message): Single<Long>

    fun fetchAllMessagesFromSystem() : Single<ArrayList<Message>>

    fun getMessages(page: Int, per_page: Int): Flowable<ArrayList<Message>>

    fun saveCallerID(cnamObject: CNAMObject): Single<Int>

    fun deleteMessages() : Single<Int>

}