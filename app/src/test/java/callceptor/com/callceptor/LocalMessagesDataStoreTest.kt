package callceptor.com.callceptor

import callceptor.com.callceptor.data.db.CallceptorDAO
import callceptor.com.callceptor.data.models.Message
import callceptor.com.callceptor.utils.AppConstants.Companion.PAGE_ENTRIES
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when` as whenever
import org.mockito.MockitoAnnotations
import org.mockito.Mockito.`when`

@Suppress("NAME_SHADOWING")
/**
 * Created by Tom on 28.8.2018..
 */
class LocalMessagesDataStoreTest {


    @Mock
    lateinit var callceptorDAO: CallceptorDAO

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }


    @Test
    fun saveGetMessages() {

        val messages = ArrayList(listOf(
                Message("12.12.2018.", "+3859112345678", "Incoming SMS message 1", "User", "1535527902032", "1"),
                Message("12.11.2018.", "+3859116427582", "Incoming SMS message 2", "User2", "1535527902033", "4"),
                Message(),
                Message("1.1.2018.", "+3859112343245", "Incoming SMS message 3", "User3", "1535527902034","3")))


        val t : LongArray = longArrayOf(0,1,2,3)

        `when`(callceptorDAO.saveIncomingMessages(messages)).thenReturn(t)

        val saveMessages = Single.fromCallable { callceptorDAO.saveIncomingMessages(messages)}.toFlowable().test()

        saveMessages.assertNoErrors()
        saveMessages.assertValue { t: LongArray ->
            t.size == 4
        }

        `when`(callceptorDAO.getMessages(0, PAGE_ENTRIES)).thenReturn(messages)

        val getMessages = Single.fromCallable { ArrayList(callceptorDAO.getMessages(0, PAGE_ENTRIES)) }.toFlowable().test()
        getMessages.assertNoErrors()
        getMessages.assertValue { calls: ArrayList<Message> ->
            calls.size == 4
        }


    }

}