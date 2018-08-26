package callceptor.com.callceptor.domain.listeners

import android.support.annotation.Nullable
import callceptor.com.callceptor.data.models.Message
import java.util.ArrayList

/**
 * Created by Tom on 24.8.2018..
 */
interface OnMessagesFetched {

    fun messagesFetched(list : ArrayList<Message>)

    fun onFetchingError(e : Throwable)

    fun showLoadingFooter()

    fun hideLoadingFooter()

    fun showLoading()

    fun hideLoading()
}