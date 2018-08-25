package callceptor.com.callceptor.data.repositories.calls

import callceptor.com.callceptor.data.db.CallceptorDAO
import callceptor.com.callceptor.data.db.CallceptorDatabase
import callceptor.com.callceptor.data.models.Call
import io.reactivex.Flowable
import io.reactivex.Single
import javax.inject.Inject

/**
 * Created by Tom on 25.8.2018..
 */
class LocalCallsDataStore
@Inject constructor(private val callceptorDatabase: CallceptorDatabase) : CallsDataStore {

    private val dao: CallceptorDAO = callceptorDatabase.getCallceptorDao()

    override fun saveAllCalls(calls: ArrayList<Call>): Single<LongArray> {
        return Single.fromCallable { dao.saveIncomingCalls(calls) }
    }

    override fun saveLastCall(call: Call): Single<Long> {
        return Single.fromCallable { dao.saveIncomingCall(call) }
    }

    override fun fetchAllCallsFromSystem(): Single<ArrayList<Call>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCalls(page: Int, per_page: Int): Flowable<ArrayList<Call>> {
        return Single.fromCallable { ArrayList(dao.getCalls(((page - 1) * per_page), per_page)) }.toFlowable()
    }
}