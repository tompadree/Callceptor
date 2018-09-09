package callceptor.com.callceptor

import callceptor.com.callceptor.data.models.Message
import callceptor.com.callceptor.data.repositories.messages.LocalMessagesDataStore
import callceptor.com.callceptor.utils.AppConstants.Companion.PAGE_ENTRIES
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when` as whenever
import org.mockito.MockitoAnnotations
import io.reactivex.SingleEmitter

import org.mockito.Mockito.`when`


@Suppress("NAME_SHADOWING")
/**
 * Created by Tom on 28.8.2018..
 */
class MessagesInteractorTest {

    @Mock
    lateinit var localMessagesDataStore: LocalMessagesDataStore

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
 }

    @Test
    fun saveGetMessages() {

        val messages =
                ArrayList(listOf(
                        Message("12.12.2018.", "+3859112345678", "Incoming SMS message 1", "User", "1535527902032", "1"),
                        Message("12.11.2018.", "+3859116427582", "Incoming SMS message 2", "User2", "1535527902033", "4"),
                        Message(),
                        Message("1.1.2018.", "+3859112343245", "Incoming SMS message 3", "User3", "1535527902034", "3")
                ))
        val t: LongArray = longArrayOf(0, 1, 2, 3)


        val mockSingleSave = Single.create { e: SingleEmitter<LongArray>? -> e?.onSuccess(t) }

        `when`(localMessagesDataStore.saveAllMessages(messages)).thenReturn(mockSingleSave)

        val saveMessagesTestObserver = localMessagesDataStore.saveAllMessages(messages).test()

        saveMessagesTestObserver.assertNoErrors()
        saveMessagesTestObserver.assertValue { t: LongArray ->
            t.size == 4
        }

        val mockSingleGet = Single.create { e: SingleEmitter<ArrayList<Message>>? -> e?.onSuccess(messages) }.toFlowable()

        `when`(localMessagesDataStore.getMessages(0, PAGE_ENTRIES)).thenReturn(mockSingleGet)

        val getMessagesTestObserver = localMessagesDataStore.getMessages(0, PAGE_ENTRIES).test()

        getMessagesTestObserver.assertNoErrors()
        getMessagesTestObserver.assertValue { messagesResponse: ArrayList<Message> -> messagesResponse.size == 4 }
        getMessagesTestObserver.assertValue { messagesResponse: ArrayList<Message> -> messagesResponse[0].name == "User" }
        getMessagesTestObserver.assertValue { messagesResponse: ArrayList<Message> -> messagesResponse[3].timestamp == "1535527902034" }
    }

}