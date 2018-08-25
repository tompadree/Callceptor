package callceptor.com.callceptor.data.repositories.calls

import callceptor.com.callceptor.data.models.Call
import callceptor.com.callceptor.domain.listeners.SystemDataManager
import io.reactivex.Flowable
import io.reactivex.Single
import javax.inject.Inject

/**
 * Created by Tom on 25.8.2018..
 */
class SystemCallsDataStore
@Inject constructor(private val systemDataManager: SystemDataManager) : CallsDataStore {

    override fun saveAllCalls(calls: ArrayList<Call>): Single<LongArray> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun saveLastCall(call: Call): Single<Long> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun fetchAllCallsFromSystem(): Single<ArrayList<Call>> {
        return Single.fromCallable { systemDataManager.getCallLogs() }
    }

    override fun getCalls(page: Int, per_page: Int): Flowable<ArrayList<Call>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}