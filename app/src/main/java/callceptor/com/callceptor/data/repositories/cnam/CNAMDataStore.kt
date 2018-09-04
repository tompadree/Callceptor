package callceptor.com.callceptor.data.repositories.cnam

import callceptor.com.callceptor.data.models.CNAMObject
import callceptor.com.callceptor.data.models.Message
import io.reactivex.Single


/**
 * Created by Tomislav on 04,September,2018
 */
interface CNAMDataStore {

    fun getCNAM(number : String): Single<CNAMObject>

    fun saveCNAM(cnamObject: CNAMObject): Single<Long>
}