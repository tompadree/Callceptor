package callceptor.com.callceptor.presenter.impl

import android.content.Context
import callceptor.com.callceptor.data.models.Message
import callceptor.com.callceptor.domain.interactors.MessageInteractor
import callceptor.com.callceptor.domain.listeners.OnMessagesFetched
import callceptor.com.callceptor.presenter.MessagesPresenter
import callceptor.com.callceptor.view.views.MessagesView
import java.util.ArrayList
import javax.inject.Inject

/**
 * Created by Tom on 24.8.2018..
 */
class MessagesPresenterImpl
@Inject constructor(private val context: Context, private val messagesInteractor: MessageInteractor,
                    private val messagesView: MessagesView) : MessagesPresenter, OnMessagesFetched {

    override fun fetchMessages() {
        showLoading()
        messagesInteractor.getMessages(this)
    }

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

    override fun showLoadingFooter() {
        messagesView.showLoadingFooter()
    }

    override fun hideLoadingFooter() {
        messagesView.hideLoadingFooter()}

    override fun showLoading() {
        messagesView.showLoading()
    }

    override fun hideLoading() {
        messagesView.hideLoading()
    }

    override fun resume() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun pause() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun destroy() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}