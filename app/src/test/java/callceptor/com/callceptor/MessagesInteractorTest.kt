package callceptor.com.callceptor

import callceptor.com.callceptor.data.db.CallceptorDAO
import callceptor.com.callceptor.data.models.Call
import callceptor.com.callceptor.data.models.Message
import callceptor.com.callceptor.data.repositories.messages.LocalMessagesDataStore
import callceptor.com.callceptor.utils.AppConstants.Companion.PAGE_ENTRIES
import io.reactivex.Flowable
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when` as whenever
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import io.reactivex.SingleEmitter
import org.mockito.Mockito.`when`

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
    fun saveMessages() {
        val saveMessages = localMessagesDataStore.saveAllMessages(
                ArrayList(listOf(
                        Message("12.12.2018.", "+3859112345678", "Incoming SMS message 1", "User", "1535527902032", "1"),
                        Message("12.11.2018.", "+3859116427582", "Incoming SMS message 2", "User2", "1535527902033", "4"),
                        Message(),
                        Message("1.1.2018.", "+3859112343245", "Incoming SMS message 3", "User3", "1535527902034","3")
        ))).test()

        saveMessages.assertNoErrors()
        saveMessages.assertValue { t: LongArray ->
            t.size == 4
        }


        val getMessages = localMessagesDataStore.getMessages(0, PAGE_ENTRIES).test()

        getMessages.assertNoErrors()
        getMessages.assertValue { messages: ArrayList<Message> -> messages.size == 4 }
        getMessages.assertValue { messages: ArrayList<Message> -> messages[0].name== "User" }
        getMessages.assertValue { messages: ArrayList<Message> -> messages[3].timestamp== "1535527902034" }
    }

}