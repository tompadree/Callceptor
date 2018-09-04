package callceptor.com.callceptor.data.api

import callceptor.com.callceptor.data.api.APIConstants.Companion.CONTENT_TYPE
import callceptor.com.callceptor.data.api.APIConstants.Companion.GET_FOR_PHONE
import callceptor.com.callceptor.data.models.CNAMObject
import io.reactivex.Single
import retrofit2.http.*


/**
 * Created by Tomislav on 04,September,2018
 */
interface CallceptorApi {
    //    https://api.opencnam.com/v3/phone/+385989436165?account_sid=SID&auth_token=TOKEN
    @Headers(CONTENT_TYPE)
    @GET("$GET_FOR_PHONE/{phoneNumber}")
//@GET("https://api.opencnam.com/v3/phone/{phoneNumber}")
    fun getCNAM(@Path("phoneNumber") phoneNumber: String, @Query("account_sid") accountId: String,
                @Query("auth_token") authToken: String, @Query("format") format: String): Single<CNAMObject>

}