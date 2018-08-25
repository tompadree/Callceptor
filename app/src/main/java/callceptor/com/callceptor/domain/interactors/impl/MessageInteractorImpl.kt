package callceptor.com.callceptor.domain.interactors.impl

import callceptor.com.callceptor.domain.interactors.MessageInteractor
import callceptor.com.callceptor.domain.listeners.OnMessagesFetched
import javax.inject.Inject
import android.widget.Toast
import android.provider.Telephony
import android.content.ContentResolver
import android.content.Context
import android.provider.CallLog
import callceptor.com.callceptor.data.models.Message
import callceptor.com.callceptor.data.repositories.messages.LocalMessagesDataStore
import callceptor.com.callceptor.data.repositories.messages.SystemMessagesDataStore
import callceptor.com.callceptor.di.module.ThreadModule
import callceptor.com.callceptor.utils.AppConstants
import callceptor.com.callceptor.utils.CheckNumberContacts
import io.reactivex.Scheduler
import io.reactivex.SingleObserver
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.processors.PublishProcessor
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Named


/**
 * Created by Tom on 24.8.2018..
 */
class MessageInteractorImpl
@Inject constructor(private val context: Context, private val systemMessagesDataStore: SystemMessagesDataStore,
                    private val localMessagesDataStore: LocalMessagesDataStore) : MessageInteractor {

    @Inject
    @field:Named(ThreadModule.SUBSCRIBE_SCHEDULER)
    lateinit var subscribeScheduler: Scheduler

    @Inject
    @field:Named(ThreadModule.OBSERVE_SCHEDULER)
    lateinit var observeScheduler: Scheduler

    private var currentPage: Int = 0
    private val disposables: CompositeDisposable? = null
    private lateinit var paginator: PublishProcessor<Int>
    private var loading: Boolean = false

    override fun getMessages(onMessagesFetched: OnMessagesFetched) {

        currentPage = 1
        paginator = PublishProcessor.create()

        val d = paginator.onBackpressureDrop()
                .filter { !loading }
                .doOnNext {
                    if (currentPage != 1)
                        onMessagesFetched.showLoadingFooter()

                    loading = true
                }
                .concatMap {
                    localMessagesDataStore.getMessages(currentPage, AppConstants.PAGE_ENTRIES)
                            .subscribeOn(subscribeScheduler)
                            .observeOn(observeScheduler)
                            .unsubscribeOn(subscribeScheduler)
                }
//                .concatMap {
//                    if (it.size <= 0)
//                        systemCallsDataStore.fetchAllCallsFromSystem()
//                                .subscribeOn(subscribeScheduler)
//                                .observeOn(observeScheduler)
//                                .unsubscribeOn(subscribeScheduler)
//                                .toFlowable()
//                    else
//                        Flowable.just(it)
//                }
                .observeOn(observeScheduler, true)
                .subscribe({
                    if (currentPage == 0) {
                        // gitResultsView.hideLoading()
                    } else {
                        onMessagesFetched.hideLoadingFooter()
                    }
                    loading = false
                    if (it.size != 0) {
                        onMessagesFetched.messagesFetched(it)
                        currentPage++

                    } else {
                        currentPage = -1

                        systemMessagesDataStore.fetchAllMessagesFromSystem()
                                .subscribeOn(subscribeScheduler)
                                .observeOn(observeScheduler)
                                .unsubscribeOn(subscribeScheduler)
                                .subscribe(object : SingleObserver<ArrayList<Message>> {

                                    override fun onSubscribe(d: Disposable) {
                                    }

                                    override fun onSuccess(t: ArrayList<Message>) {
                                        saveLocalResults(t, onMessagesFetched)
                                    }

                                    override fun onError(e: Throwable) {
                                        onMessagesFetched.onFetchingError(e)
                                    }
                                })
                    }
                }, {
                    onMessagesFetched.onFetchingError(it as Throwable)
                    loading = false
                })

        disposables?.add(d)

        fetchNextPage()
    }

    override fun destroyDisposable() {
        disposables?.clear()
    }

    override fun fetchNextPage() {
        if (currentPage >= 0)
            paginator.onNext(currentPage)
    }

    fun saveLocalResults(calls: ArrayList<Message>, listener: OnMessagesFetched) {

        localMessagesDataStore.saveAllMessages(calls)
                .subscribeOn(subscribeScheduler)
                .observeOn(observeScheduler)
                .unsubscribeOn(subscribeScheduler)
                .subscribe(object : SingleObserver<LongArray> {

                    override fun onSuccess(t: LongArray) {
                        var test: LongArray = t
                        test = t
                        getMessages(listener)
                    }

                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onError(e: Throwable) {
                        listener.onFetchingError(e)
                    }
                })

    }
}