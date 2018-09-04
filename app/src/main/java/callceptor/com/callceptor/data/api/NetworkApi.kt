package callceptor.com.callceptor.data.api

import callceptor.com.callceptor.data.models.CNAMObject
import io.reactivex.Single


/**
 * Created by Tomislav on 04,September,2018
 */
interface NetworkApi {

    fun getCNAM( phoneNumber: String): Single<CNAMObject>

}