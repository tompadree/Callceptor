package callceptor.com.callceptor;

import callceptor.com.callceptor.data.models.Call
import callceptor.com.callceptor.data.repositories.calls.LocalCallsDataStore
import callceptor.com.callceptor.utils.AppConstants
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

/**
 * Created by Tomislav on 29,August,2018
 */
class CallsInteractorTest {


    @Mock
    lateinit var localCallsDataStore: LocalCallsDataStore

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }



    @Test
    fun saveCalls() {
        val saveCalls = localCallsDataStore.saveAllCalls(
                ArrayList(listOf(
                        Call("12.12.2018.", 1, "12", "+3859112345678", "https://cdn.pixabay.com/photo/2013/04/06/11/50/image-editing-101040_960_720.jpg", "User", 1535527902032),
                        Call("12.11.2018.", 4, "5", "+3859116427582", "https://cdn.pixabay.com/photo/2013/04/06/11/50/image-editing-101040_960_720.jpg", "User2", 1535527902033),
                        Call(),
                        Call("1.1.2018.", 3, "3", "+3859112343245", "https://cdn.pixabay.com/photo/2013/04/06/11/50/image-editing-101040_960_720.jpg", "User3", 1535527902034)
                ))).test()

        saveCalls.assertNoErrors()
        saveCalls.assertValue { t: LongArray ->
            t.size == 4
        }


        val getCalls = localCallsDataStore.getCalls(0, AppConstants.PAGE_ENTRIES).test()

        getCalls.assertNoErrors()
        getCalls.assertValue { messages: ArrayList<Call> -> messages.size == 4 }
        getCalls.assertValue { messages: ArrayList<Call> -> messages[0].name== "User" }
        getCalls.assertValue { messages: ArrayList<Call> -> messages[3].number== "+3859112343245" }
    }
    
}