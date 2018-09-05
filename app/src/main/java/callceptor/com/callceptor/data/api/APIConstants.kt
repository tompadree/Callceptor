package callceptor.com.callceptor.data.api


/**
 * Created by Tomislav on 04,September,2018
 */
interface APIConstants {

    companion object {
//         https://api.opencnam.com/v3/phone/+385989436165?account_sid=SID&auth_token=TOKEN

        const val BASE_URL = "https://api.opencnam.com/"

        const val GET_FOR_PHONE = "/v3/phone"

        const val CONTENT_TYPE = "Content-Type: application/json"
        //    String ACCEPT_TYPE = "Accept: application/vnd.oauth.v1+json";
        val ACCEPT_TYPE_V2 = "Accept: application/json, */*"


        const val RESPONSE_FORMAT = "json"

        val httpMethod_GET = "GET"
        val httpMethod_POST = "POST"
        val httpMethod_DELETE = "DELETE"

        val PAGE_ENTRIES = 50
    }
}