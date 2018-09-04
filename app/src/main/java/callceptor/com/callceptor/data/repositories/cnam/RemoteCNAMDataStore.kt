package callceptor.com.callceptor.data.repositories.cnam

import callceptor.com.callceptor.data.api.NetworkApi
import callceptor.com.callceptor.data.models.CNAMObject
import io.reactivex.Single
import javax.inject.Inject

/**
 * Created by Tom on 4.9.2018..
 */
class RemoteCNAMDataStore
@Inject constructor(private val networkApi: NetworkApi) : CNAMDataStore {

    override fun getCNAM(number: String): Single<CNAMObject> {
        return networkApi.getCNAM(number)
    }

}