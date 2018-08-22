package callceptor.com.callceptor.view.views

import callceptor.com.callceptor.data.models.Call
import java.util.ArrayList

/**
 * Created by Tom on 22.8.2018..
 */
interface CallsView : DefaultView {

    fun callLogsFetched(list : ArrayList<Call>)

    fun showLoadingFooter()

    fun hideLoadingFooter()

    fun showRefreshLoading()

    fun hideRefreshLoading()

}