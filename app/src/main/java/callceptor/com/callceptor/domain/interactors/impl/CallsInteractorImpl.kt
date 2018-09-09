package callceptor.com.callceptor.domain.interactors.impl

import android.content.Context
import callceptor.com.callceptor.data.models.CNAMObject
import callceptor.com.callceptor.domain.interactors.CallsInteractor
import javax.inject.Inject
import callceptor.com.callceptor.data.models.Call
import callceptor.com.callceptor.data.repositories.calls.LocalCallsDataStore
import callceptor.com.callceptor.data.repositories.calls.SystemCallsDataStore
import callceptor.com.callceptor.data.repositories.cnam.RemoteCNAMDataStore
import callceptor.com.callceptor.di.module.ThreadModule
import callceptor.com.callceptor.domain.listeners.OnCallContactsFetched
import callceptor.com.callceptor.utils.AppConstants.Companion.PAGE_ENTRIES
import callceptor.com.callceptor.utils.scheduler.SchedulerProvider
import io.reactivex.Flowable
import io.reactivex.Scheduler
import io.reactivex.SingleObserver
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.processors.PublishProcessor
import javax.inject.Named
import kotlin.collections.ArrayList

/**
 * Created by Tom on 22.8.2018..
 */
class CallsInteractorImpl
@Inject constructor(private val context: Context, private val localCallsDataStore: LocalCallsDataStore,
                    private val systemCallsDataStore: SystemCallsDataStore, private val remoteCNAMDataStore: RemoteCNAMDataStore,
                    private val schedulerProvider: SchedulerProvider) : CallsInteractor {

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
    private lateinit var listener: OnCallContactsFetched

    override fun getContacts(onCallContactsFetched: OnCallContactsFetched) {

    }

    override fun idLastNumber(onCallContactsFetched: OnCallContactsFetched, lastNumber: String) {
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

        localCallsDataStore.saveCallerID(cnamObject)
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

    override fun getCallLogs(onCallContactsFetched: OnCallContactsFetched) {

        this.listener = onCallContactsFetched

        currentPage = 1
        paginator = PublishProcessor.create()

        val d = paginator.onBackpressureDrop()
                .filter { !loading }
                .doOnNext {
                    if (currentPage != 1)
                        onCallContactsFetched.showLoadingFooter()

                    loading = true
                }
                .concatMap {
                    localCallsDataStore.getCalls(currentPage, PAGE_ENTRIES)
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
                        onCallContactsFetched.hideLoadingFooter()
                    }
                    loading = false
                    if (it.size != 0) {
                        onCallContactsFetched.callLogsFetched(it)
                        currentPage++

                    } else {

                        if (currentPage == 1)
                            systemCallsDataStore.fetchAllCallsFromSystem()
                                    .subscribeOn(schedulerProvider.ioScheduler())
                                    .observeOn(schedulerProvider.uiScheduler())
                                    .unsubscribeOn(schedulerProvider.ioScheduler())
                                    .subscribe(object : SingleObserver<ArrayList<Call>> {

                                        override fun onSubscribe(d: Disposable) {
                                        }

                                        override fun onSuccess(t: ArrayList<Call>) {
                                            if (t.size == 0) {
                                                onCallContactsFetched.callLogsFetched(t)
                                            } else
                                                saveLocalResults(t, onCallContactsFetched)
                                        }

                                        override fun onError(e: Throwable) {
                                            onCallContactsFetched.onFetchingError(e)
                                        }
                                    })

                        currentPage = -1
                    }
                }, {
                    it.printStackTrace()
                    onCallContactsFetched.onFetchingError(it as Throwable)
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

    fun saveLocalResults(calls: ArrayList<Call>, listener: OnCallContactsFetched) {

        localCallsDataStore.saveAllCalls(calls)
                .subscribeOn(schedulerProvider.ioScheduler())
                .observeOn(schedulerProvider.uiScheduler())
                .unsubscribeOn(schedulerProvider.ioScheduler())
                .subscribe(object : SingleObserver<LongArray> {

                    override fun onSuccess(t: LongArray) {
                        var test: LongArray = t
                        test = t
                        getCallLogs(listener)
                    }

                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        listener.onFetchingError(e)
                    }
                })

    }

    override fun saveLastCall(call: Call) {

        if (call.number == null)
            return

        localCallsDataStore.saveLastCall(call)
                .subscribeOn(schedulerProvider.ioScheduler())
                .observeOn(schedulerProvider.uiScheduler())
                .unsubscribeOn(schedulerProvider.ioScheduler())
                .subscribe(object : SingleObserver<Long> {

                    override fun onSuccess(t: Long) {
                        var test: Long = t
                        test = t
                        listener.hideLoading()
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