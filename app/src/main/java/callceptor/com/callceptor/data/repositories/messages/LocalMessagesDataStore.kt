package callceptor.com.callceptor.data.repositories.messages

import callceptor.com.callceptor.data.db.CallceptorDatabase
import callceptor.com.callceptor.data.models.Message
import io.reactivex.Flowable
import io.reactivex.Single
import javax.inject.Inject

/**
 * Created by Tom on 25.8.2018..
 */
class LocalMessagesDataStore
@Inject constructor(callceptorDatabase: CallceptorDatabase) : MessagesDataStore {

    private val callceptorDAO = callceptorDatabase.getCallceptorDao()

    override fun saveAllMessages(messages: ArrayList<Message>): Single<LongArray> {
        return Single.fromCallable { callceptorDAO.saveIncomingMessages(messages)}
    }

    override fun saveLastMessage(message: Message): Single<Long> {
        return Single.fromCallable { callceptorDAO.saveIncomingMessage(message)}
    }

    override fun fetchAllMessagesFromSystem(): Single<ArrayList<Message>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getMessages(page: Int, per_page: Int): Flowable<ArrayList<Message>> {
        return Single.fromCallable { ArrayList(callceptorDAO.getMessages(((page - 1) * per_page), per_page)) }.toFlowable()
    }
}