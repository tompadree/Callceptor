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
    @Headers(CONTENT_TYPE)
    @GET("$GET_FOR_PHONE/{phoneNumber}")
    fun getCNAM(@Path("phoneNumber") phoneNumber: String, @Query("account_sid") accountId: String,
                @Query("auth_token") authToken: String, @Query("format") format: String): Single<CNAMObject>

}