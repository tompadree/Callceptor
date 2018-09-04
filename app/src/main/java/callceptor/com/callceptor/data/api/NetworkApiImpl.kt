package callceptor.com.callceptor.data.api;

import android.drm.DrmInfoRequest.ACCOUNT_ID
import callceptor.com.callceptor.data.models.CNAMObject
import io.reactivex.Single
import javax.inject.Inject

/**
 * Created by Tomislav on 04,September,2018
 */
class NetworkApiImpl
@Inject constructor(private val callceptorApi: CallceptorApi) : NetworkApi {

    override fun getCNAM(phoneNumber: String, authToken: String): Single<CNAMObject> {
        return Single.defer<CNAMObject> { callceptorApi.getCNAM(phoneNumber, ACCOUNT_ID, authToken) }
    }
}