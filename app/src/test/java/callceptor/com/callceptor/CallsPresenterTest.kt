package callceptor.com.callceptor;

import android.content.Context
import callceptor.com.callceptor.data.models.Call
import callceptor.com.callceptor.domain.interactors.CallsInteractor
import callceptor.com.callceptor.domain.listeners.OnCallContactsFetched
import callceptor.com.callceptor.presenter.CallsPresenter
import callceptor.com.callceptor.view.views.CallsView
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import java.util.ArrayList

/**
 * Created by Tomislav on 29,August,2018
 */
@RunWith(MockitoJUnitRunner::class)
class CallsPresenterTest : CallsPresenter, OnCallContactsFetched {

    @Mock
    private val context: Context? = null

    @Mock
    lateinit var callsInteractor: CallsInteractor

    @Mock
    lateinit var callsView: CallsView

    @Before
    fun setUp() {

        MockitoAnnotations.initMocks(this)
    }

    @Test
    override fun fetchCallLogs() {
        showLoading()
        callsInteractor.getCallLogs(this)
    }

    @Test
    override fun fetchNextPage() {
        callsInteractor.fetchNextPage()
    }

    @Test
    override fun callLogsFetched(list: ArrayList<Call>) {
        callsView.callLogsFetched(list)
        hideLoading()
    }

    @Test
    override fun contactsFetched(list: ArrayList<String>) {

    }

    @Test
    override fun onFetchingError(e: Throwable) {
        callsView.showError(e.localizedMessage)
        hideLoading()
    }

    @Test
    override fun showLoadingFooter() {
        callsView.showLoadingFooter()
    }

    @Test
    override fun hideLoadingFooter() {
        callsView.hideLoadingFooter()
    }

    @Test
    override fun showLoading() {
        callsView.showLoading()
    }

    @Test
    override fun hideLoading() {
        callsView.hideLoading()
    }

    override fun resume() {
    }

    override fun pause() {
    }

    override fun destroy() {
    }
}