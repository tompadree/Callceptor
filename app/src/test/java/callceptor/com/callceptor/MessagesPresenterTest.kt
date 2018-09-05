package callceptor.com.callceptor

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import callceptor.com.callceptor.data.models.Message
import callceptor.com.callceptor.domain.interactors.MessageInteractor
import callceptor.com.callceptor.domain.listeners.OnMessagesFetched
import callceptor.com.callceptor.presenter.MessagesPresenter
import callceptor.com.callceptor.view.views.MessagesView
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import java.util.ArrayList

/**
 * Created by Tom on 28.8.2018..
 */
@RunWith(MockitoJUnitRunner::class)
class MessagesPresenterTest() : MessagesPresenter, OnMessagesFetched {

    @Mock
    private var context: Context? = null

    @Mock
    lateinit var messagesInteractor: MessageInteractor

    @Mock
    lateinit var messagesView: MessagesView


    @Before
    fun setUp() {

        MockitoAnnotations.initMocks(this)
    }

    @Test
    override fun fetchMessages(lastNumber: String) {
        showLoading()
        messagesInteractor.getMessages(this)
    }

    override fun lastNumberCallIDed() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    @Test
    override fun fetchNextPage() {
        messagesInteractor.fetchNextPage()
    }


    override fun messagesFetched(list: ArrayList<Message>) {
        messagesView.callMessagesFetched(list)
        hideLoading()
    }


    override fun onFetchingError(e: Throwable) {
        messagesView.showError(e.localizedMessage)
        hideLoading()
    }

    @Test
    override fun showLoadingFooter() {
        messagesView.showLoadingFooter()
    }

    @Test
    override fun hideLoadingFooter() {
        messagesView.hideLoadingFooter()}

    @Test
    override fun showLoading() {
        messagesView.showLoading()
    }

    @Test
    override fun hideLoading() {
        messagesView.hideLoading()
    }

    override fun resume() {
    }

    override fun pause() {
    }

    override fun destroy() {
    }

}