package callceptor.com.callceptor.data.repositories.messages

import callceptor.com.callceptor.data.models.Message
import io.reactivex.Flowable
import io.reactivex.Single

/**
 * Created by Tom on 25.8.2018..
 */
class SystemMessagesDataStore : MessagesDataStore {

    override fun saveAllMessages(Messages: ArrayList<Message>): Single<LongArray> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun saveLastMessage(message: Message): Single<Long> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun fetchAllMessagesFromSystem(): Single<Message> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getMessages(page: Int, per_page: Int): Flowable<Message> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}