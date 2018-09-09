package callceptor.com.callceptor.domain.listeners

import callceptor.com.callceptor.data.models.Message
import java.util.ArrayList

/**
 * Created by Tom on 24.8.2018..
 */
interface OnMessagesFetched {

    fun messagesFetched(list : ArrayList<Message>)

    fun onFetchingError(e : Throwable)

    fun lastNumberCallIDed()

    fun showLoadingFooter()

    fun hideLoadingFooter()

    fun showLoading()

    fun hideLoading()
}