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
@Inject constructor(private val context: Context, private val messagesInteractor: MessageInteractor, private val messagesView: MessagesView)
    : MessagesPresenter, OnMessagesFetched {

    override fun fetchMessages() {
        messagesInteractor.getMessages(this)
    }

    override fun fetchNextPage() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun messagesFetched(list: ArrayList<Message>) {
        messagesView.callMessagesFetched(list)
    }

    override fun onFetchingError(e: Throwable) {
        messagesView.showError(e.localizedMessage)
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