package callceptor.com.callceptor.presenter.impl

import android.content.Context
import callceptor.com.callceptor.data.models.Call
import callceptor.com.callceptor.domain.interactors.CallsInteractor
import callceptor.com.callceptor.domain.listeners.OnCallContactsFetched
import callceptor.com.callceptor.presenter.CallsPresenter
import callceptor.com.callceptor.view.views.CallsView
import java.util.ArrayList
import javax.inject.Inject

/**
 * Created by Tom on 22.8.2018..
 */
class CallsPresenterImpl
@Inject constructor(private val context: Context, private val callsInteractor: CallsInteractor,
                    private val callsView: CallsView) : CallsPresenter, OnCallContactsFetched {

    override fun fetchCallLogs() {
        callsView.showLoading()
        callsInteractor.getCallLogs(this)
    }

    override fun callLogsFetched(list : ArrayList<Call>) {
        callsView.callLogsFetched(list)
    }

    override fun contactsFetched(list: ArrayList<String>) {

    }

    override fun onFetchingError(e: Throwable) {
        callsView.showError(e.localizedMessage)
        callsView.hideLoading()
    }

    override fun resume() {
    }

    override fun pause() {
    }

    override fun destroy() {
    }
}