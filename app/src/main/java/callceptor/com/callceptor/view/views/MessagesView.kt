package callceptor.com.callceptor.view.views

import callceptor.com.callceptor.data.models.Call
import callceptor.com.callceptor.data.models.Message
import java.util.ArrayList

/**
 * Created by Tom on 22.8.2018..
 */
interface MessagesView : DefaultView {

    fun callMessagesFetched(list : ArrayList<Message>)

    fun showLoadingFooter()

    fun hideLoadingFooter()

    fun showRefreshLoading()

    fun hideRefreshLoading()

}