package callceptor.com.callceptor.data.repositories.messages

import callceptor.com.callceptor.data.models.Message
import io.reactivex.Flowable
import io.reactivex.Single

/**
 * Created by Tom on 25.8.2018..
 */
interface MessagesDataStore {

    fun saveAllMessages(Messages: ArrayList<Message>): Single<LongArray>

    fun saveLastMessage(message: Message): Single<Long>

    fun fetchAllMessagesFromSystem() : Single<Message>

    fun getMessages(page: Int, per_page: Int): Flowable<Message>


}