package callceptor.com.callceptor.di.module

import callceptor.com.callceptor.data.repositories.messages.LocalMessagesDataStore
import callceptor.com.callceptor.data.repositories.messages.MessagesDataStore
import callceptor.com.callceptor.data.repositories.messages.SystemMessagesDataStore
import callceptor.com.callceptor.di.HomeScope
import callceptor.com.callceptor.domain.interactors.MessageInteractor
import callceptor.com.callceptor.domain.interactors.impl.MessageInteractorImpl
import callceptor.com.callceptor.presenter.MessagesPresenter
import callceptor.com.callceptor.presenter.impl.MessagesPresenterImpl
import callceptor.com.callceptor.view.views.MessagesView
import dagger.Module
import dagger.Provides

/**
 * Created by Tom on 24.8.2018..
 */
@Module
class MessageModule(private val messagesView: MessagesView) {

    @HomeScope
    @Provides
    fun providesMessageView() : MessagesView = this.messagesView

    @HomeScope
    @Provides
    fun providesMessagesPresenter(presenter : MessagesPresenterImpl) : MessagesPresenter = presenter

    @HomeScope
    @Provides
    fun providesMessageInteractor(interactor : MessageInteractorImpl) : MessageInteractor = interactor

    @Provides
    @HomeScope
    fun providesLocalMessagesDataStore(localMessagesDataStore: LocalMessagesDataStore): MessagesDataStore = localMessagesDataStore

    @Provides
    @HomeScope
    fun providesSystemMessagesDataStore(systemMessagesDataStore: SystemMessagesDataStore): MessagesDataStore = systemMessagesDataStore

}