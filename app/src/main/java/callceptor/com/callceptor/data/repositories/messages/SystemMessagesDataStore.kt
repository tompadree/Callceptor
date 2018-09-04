package callceptor.com.callceptor.data.repositories.messages

import callceptor.com.callceptor.data.models.CNAMObject
import callceptor.com.callceptor.data.models.Message
import callceptor.com.callceptor.domain.listeners.SystemDataManager
import io.reactivex.Flowable
import io.reactivex.Single
import javax.inject.Inject

/**
 * Created by Tom on 25.8.2018..
 */
class SystemMessagesDataStore
@Inject constructor(private val systemDataManager: SystemDataManager) : MessagesDataStore {

    override fun saveAllMessages(Messages: ArrayList<Message>): Single<LongArray> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun saveLastMessage(message: Message): Single<Long> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun saveCallerID(cnamObject: CNAMObject): Single<Int> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun fetchAllMessagesFromSystem(): Single<ArrayList<Message>> {
        return Single.fromCallable { systemDataManager.getMessages() }
    }

    override fun getMessages(page: Int, per_page: Int): Flowable<ArrayList<Message>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}