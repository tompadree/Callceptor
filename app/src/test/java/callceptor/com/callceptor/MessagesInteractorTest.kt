package callceptor.com.callceptor

import android.content.Context
import callceptor.com.callceptor.data.db.CallceptorDAO
import callceptor.com.callceptor.data.models.Call
import callceptor.com.callceptor.data.models.Message
import callceptor.com.callceptor.data.repositories.cnam.RemoteCNAMDataStore
import callceptor.com.callceptor.data.repositories.messages.LocalMessagesDataStore
import callceptor.com.callceptor.data.repositories.messages.SystemMessagesDataStore
import callceptor.com.callceptor.di.component.DaggerAppComponent
import callceptor.com.callceptor.di.module.AppModule
import callceptor.com.callceptor.di.module.ThreadModule
import callceptor.com.callceptor.domain.interactors.impl.MessageInteractorImpl
import callceptor.com.callceptor.domain.listeners.OnMessagesFetched
import callceptor.com.callceptor.utils.AppConstants
import callceptor.com.callceptor.utils.AppConstants.Companion.PAGE_ENTRIES
import io.reactivex.Flowable
import io.reactivex.Scheduler
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when` as whenever
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import io.reactivex.SingleEmitter
import io.reactivex.schedulers.TestScheduler
import org.mockito.InjectMocks
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by Tom on 28.8.2018..
 */
class MessagesInteractorTest {

    @Mock
    lateinit var localMessagesDataStore: LocalMessagesDataStore

    @Mock
    lateinit var systemMessagesDataStore: SystemMessagesDataStore

    @Mock
    lateinit var remoteCNAMDataStore: RemoteCNAMDataStore

    @Mock
    lateinit var listener: OnMessagesFetched

    @Mock
    lateinit var context: Context

    lateinit var messageInteractorImpl: MessageInteractorImpl

    @InjectMocks
    @field:Named(ThreadModule.SUBSCRIBE_SCHEDULER)
    lateinit var subscribeScheduler: Scheduler

    @InjectMocks
    @field:Named(ThreadModule.OBSERVE_SCHEDULER)
    lateinit var observeScheduler: Scheduler

    lateinit var testScheduler: TestScheduler

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

//        DaggerAppComponent.builder()
//                .appModule(AppModule(App))
//                .build().inject()

        testScheduler = TestScheduler()

        messageInteractorImpl = MessageInteractorImpl(context, systemMessagesDataStore, localMessagesDataStore, remoteCNAMDataStore)
    }

    @Test
    fun testGetUsers_errorCase_showError() {
        // Given
        val error = "Test error"
        val mockSingleError = Single.create { e: SingleEmitter<ArrayList<Message>>? -> e?.onError(Exception(error)) }.toFlowable()
        // When
//        `when`(mockGetUsers.execute(Mockito.anyInt(), Mockito.anyBoolean())).thenReturn(single)
        `when` (localMessagesDataStore.getMessages(0, PAGE_ENTRIES)).thenReturn(mockSingleError)


//        messageInteractorImpl.attachView(mockView)
        messageInteractorImpl.getMessages(listener)

        testScheduler.triggerActions()

        // Then
        verify(listener).hideLoading()
        verify(listener).onFetchingError(Exception(error))
    }



//
//    @Test
//    fun saveGetMessages() {
//
//        val messages =
//                ArrayList(listOf(
//                        Message("12.12.2018.", "+3859112345678", "Incoming SMS message 1", "User", "1535527902032", "1"),
//                        Message("12.11.2018.", "+3859116427582", "Incoming SMS message 2", "User2", "1535527902033", "4"),
//                        Message(),
//                        Message("1.1.2018.", "+3859112343245", "Incoming SMS message 3", "User3", "1535527902034", "3")
//                ))
//        val t: LongArray = longArrayOf(0, 1, 2, 3)
//
//
//
//        val mockSingleSave = Single.create { e: SingleEmitter<LongArray>? -> e?.onSuccess(t) }
//
//        `when`(localMessagesDataStore.saveAllMessages(messages)).thenReturn(mockSingleSave)
//
//        val saveMessagesTestObserver = localMessagesDataStore.saveAllMessages(messages).test()
//
//        saveMessagesTestObserver.assertNoErrors()
//        saveMessagesTestObserver.assertValue { t: LongArray ->
//            t.size == 4
//        }
//
//        val mockSingleGet = Single.create { e: SingleEmitter<ArrayList<Message>>? -> e?.onSuccess(messages) }.toFlowable()
//
//        `when`(localMessagesDataStore.getMessages(0, PAGE_ENTRIES)).thenReturn(mockSingleGet)
//
//        val getMessagesTestObserver = localMessagesDataStore.getMessages(0, PAGE_ENTRIES).test()
//
//        getMessagesTestObserver.assertNoErrors()
//        getMessagesTestObserver.assertValue { messages: ArrayList<Message> -> messages.size == 4 }
//        getMessagesTestObserver.assertValue { messages: ArrayList<Message> -> messages[0].name == "User" }
//        getMessagesTestObserver.assertValue { messages: ArrayList<Message> -> messages[3].timestamp == "1535527902034" }
//    }

}