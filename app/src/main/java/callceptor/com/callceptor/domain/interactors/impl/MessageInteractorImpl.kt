package callceptor.com.callceptor.domain.interactors.impl

import callceptor.com.callceptor.domain.interactors.MessageInteractor
import callceptor.com.callceptor.domain.listeners.OnMessagesFetched
import javax.inject.Inject
import android.widget.Toast
import android.provider.Telephony
import android.content.ContentResolver
import android.content.Context
import android.provider.CallLog
import callceptor.com.callceptor.data.models.CNAMObject
import callceptor.com.callceptor.data.models.Message
import callceptor.com.callceptor.data.repositories.cnam.RemoteCNAMDataStore
import callceptor.com.callceptor.data.repositories.messages.LocalMessagesDataStore
import callceptor.com.callceptor.data.repositories.messages.SystemMessagesDataStore
import callceptor.com.callceptor.di.module.ThreadModule
import callceptor.com.callceptor.utils.AppConstants
import callceptor.com.callceptor.utils.CheckNumberContacts
import callceptor.com.callceptor.utils.scheduler.SchedulerProvider
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
                    private val localMessagesDataStore: LocalMessagesDataStore, private val remoteCNAMDataStore: RemoteCNAMDataStore,
                    private val schedulerProvider: SchedulerProvider) : MessageInteractor {

//    @Inject
//    @field:Named(ThreadModule.SUBSCRIBE_SCHEDULER)
//    lateinit var subscribeScheduler: Scheduler
//
//    @Inject
//    @field:Named(ThreadModule.OBSERVE_SCHEDULER)
//    lateinit var observeScheduler: Scheduler

    private var currentPage: Int = 0
    private val disposables: CompositeDisposable? = null
    private lateinit var paginator: PublishProcessor<Int>
    private var loading: Boolean = false
    private lateinit var listener: OnMessagesFetched

    override fun idLastNumber(onMessagesFetched: OnMessagesFetched, lastNumber: String) {
        remoteCNAMDataStore.getCNAM(lastNumber)
                .subscribeOn(schedulerProvider.ioScheduler())
                .observeOn(schedulerProvider.uiScheduler())
                .unsubscribeOn(schedulerProvider.ioScheduler())
                .subscribe(object : SingleObserver<CNAMObject> {

                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onSuccess(t: CNAMObject) {
                        saveCNAM(t)
                    }


                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        listener.lastNumberCallIDed()
//                        listener.onFetchingError(e)
                    }
                })
    }

    fun saveCNAM(cnamObject: CNAMObject) {

        localMessagesDataStore.saveCallerID(cnamObject)
                .subscribeOn(schedulerProvider.ioScheduler())
                .observeOn(schedulerProvider.uiScheduler())
                .unsubscribeOn(schedulerProvider.ioScheduler())
                .subscribe(object : SingleObserver<Int> {

                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onSuccess(t: Int) {
                        listener.lastNumberCallIDed()
                    }


                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        listener.lastNumberCallIDed()
//                        listener.onFetchingError(e)
                    }
                })

    }

    override fun getMessages(onMessagesFetched: OnMessagesFetched) {

        listener = onMessagesFetched

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
                            .subscribeOn(schedulerProvider.ioScheduler())
                            .observeOn(schedulerProvider.uiScheduler())
                            .unsubscribeOn(schedulerProvider.ioScheduler())
                }
//                .concatMap {
//                    if (it.size <= 0)
//                        systemCallsDataStore.fetchAllCallsFromSystem()
//                                .subscribeOn(schedulerProvider.ioScheduler())
//                                .observeOn(schedulerProvider.uiScheduler())
//                                .unsubscribeOn(schedulerProvider.ioScheduler())
//                                .toFlowable()
//                    else
//                        Flowable.just(it)
//                }
                .observeOn(schedulerProvider.uiScheduler(), true)
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

                        if (currentPage == 1)
                            systemMessagesDataStore.fetchAllMessagesFromSystem()
                                    .subscribeOn(schedulerProvider.ioScheduler())
                                    .observeOn(schedulerProvider.uiScheduler())
                                    .unsubscribeOn(schedulerProvider.ioScheduler())
                                    .subscribe(object : SingleObserver<ArrayList<Message>> {

                                        override fun onSubscribe(d: Disposable) {
                                        }

                                        override fun onSuccess(t: ArrayList<Message>) {
                                            if (t.size == 0)
                                                onMessagesFetched.messagesFetched(t)
                                            else
                                                saveLocalResults(t, onMessagesFetched)
                                        }

                                        override fun onError(e: Throwable) {
                                            onMessagesFetched.onFetchingError(e)
                                        }
                                    })

                        currentPage = -1
                    }
                }, {
                    it.printStackTrace()
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

    override fun saveLocalResults(messages: ArrayList<Message>, onMessagesFetched: OnMessagesFetched) {

        localMessagesDataStore.saveAllMessages(messages)
                .subscribeOn(schedulerProvider.ioScheduler())
                .observeOn(schedulerProvider.uiScheduler())
                .unsubscribeOn(schedulerProvider.ioScheduler())
                .subscribe(object : SingleObserver<LongArray> {

                    override fun onSuccess(t: LongArray) {
                        var test: LongArray = t
                        test = t
                        getMessages(listener)
                    }

                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        listener.onFetchingError(e)
                    }
                })

    }

    override fun saveLastMessage(message: Message) {

        if (message.number == null)
            return
        localMessagesDataStore.saveLastMessage(message)
                .subscribeOn(schedulerProvider.ioScheduler())
                .observeOn(schedulerProvider.uiScheduler())
                .unsubscribeOn(schedulerProvider.ioScheduler())
                .subscribe(object : SingleObserver<Long> {

                    override fun onSuccess(t: Long) {
                        var test: Long = t
                        test = t
                    }

                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        listener.onFetchingError(e)
                    }
                })
    }
}