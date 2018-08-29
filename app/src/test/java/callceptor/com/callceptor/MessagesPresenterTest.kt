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
class MessagesPresenterTest() : MessagesPresenter, OnMessagesFetched, Parcelable {

    @Mock
    private var context: Context? = null

    @Mock
    lateinit var messagesInteractor: MessageInteractor

    @Mock
    lateinit var messagesView: MessagesView

    constructor(parcel: Parcel) : this() {

    }



    @Before
    fun setUp() {

        MockitoAnnotations.initMocks(this)
    }

    @Test
    override fun fetchMessages() {
        showLoading()
        messagesInteractor.getMessages(this)
    }

    @Test
    override fun fetchNextPage() {
        messagesInteractor.fetchNextPage()
    }

    @Test
    override fun messagesFetched(list: ArrayList<Message>) {
        messagesView.callMessagesFetched(list)
        hideLoading()
    }

    @Test
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

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MessagesPresenterTest> {
        override fun createFromParcel(parcel: Parcel): MessagesPresenterTest {
            return MessagesPresenterTest(parcel)
        }

        override fun newArray(size: Int): Array<MessagesPresenterTest?> {
            return arrayOfNulls(size)
        }
    }
}