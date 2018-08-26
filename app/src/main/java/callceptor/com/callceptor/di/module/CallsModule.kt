package callceptor.com.callceptor.di.module

import android.telecom.Call
import callceptor.com.callceptor.data.repositories.calls.CallsDataStore
import callceptor.com.callceptor.data.repositories.calls.LocalCallsDataStore
import callceptor.com.callceptor.data.repositories.calls.SystemCallsDataStore
import callceptor.com.callceptor.di.HomeScope
import callceptor.com.callceptor.domain.interactors.CallsInteractor
import callceptor.com.callceptor.domain.interactors.impl.CallsInteractorImpl
import callceptor.com.callceptor.presenter.CallsPresenter
import callceptor.com.callceptor.presenter.impl.CallsPresenterImpl
import callceptor.com.callceptor.telephony.MyPhoneStateReceiver
import callceptor.com.callceptor.view.views.CallsView
import dagger.Module
import dagger.Provides

/**
 * Created by Tom on 22.8.2018..
 */
@Module
class CallsModule(private val callsView: CallsView) {

    @Provides
    @HomeScope
    fun providesCallsView(): CallsView = this.callsView

    @Provides
    @HomeScope
    fun providesCallsPresenter(callsPresenter: CallsPresenterImpl): CallsPresenter = callsPresenter

    @Provides
    @HomeScope
    fun providesCallsInteractor(callsInteractor: CallsInteractorImpl): CallsInteractor = callsInteractor

    @Provides
    @HomeScope
    fun providesLocalCallsDataStore(localCallsDataStore: LocalCallsDataStore): CallsDataStore = localCallsDataStore

    @Provides
    @HomeScope
    fun providesSystemCallsDataStore(systemCallsDataStore: SystemCallsDataStore): CallsDataStore = systemCallsDataStore

//    @Provides
//    @HomeScope
//    fun providesMyPhoneStateReceiver()  = MyPhoneStateReceiver()

}