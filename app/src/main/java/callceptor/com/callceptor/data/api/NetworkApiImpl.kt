package callceptor.com.callceptor.data.api;

import callceptor.com.callceptor.BuildConfig.ACCOUNT_SID
import callceptor.com.callceptor.BuildConfig.AUTH_TOKEN
import callceptor.com.callceptor.data.api.APIConstants.Companion.RESPONSE_FORMAT
import callceptor.com.callceptor.data.models.CNAMObject
import io.reactivex.Single
import javax.inject.Inject

/**
 * Created by Tomislav on 04,September,2018
 */
class NetworkApiImpl
@Inject constructor(private val callceptorApi: CallceptorApi) : NetworkApi {

    override fun getCNAM(phoneNumber: String): Single<CNAMObject> {
        return Single.defer<CNAMObject> { callceptorApi.getCNAM(phoneNumber, ACCOUNT_SID, AUTH_TOKEN, RESPONSE_FORMAT) }
    }
}