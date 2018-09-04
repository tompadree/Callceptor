package callceptor.com.callceptor.data.repositories.calls

import callceptor.com.callceptor.data.models.CNAMObject
import callceptor.com.callceptor.data.models.Call
import io.reactivex.Flowable
import io.reactivex.Single

/**
 * Created by Tom on 25.8.2018..
 */
interface CallsDataStore {

    fun saveAllCalls(calls: ArrayList<Call>): Single<LongArray>

    fun saveLastCall(call: Call): Single<Long>

    fun fetchAllCallsFromSystem() : Single<ArrayList<Call>>

    fun getCalls(page: Int, per_page: Int): Flowable<ArrayList<Call>>

    fun saveCallerID(cnamObject: CNAMObject): Single<Int>

}