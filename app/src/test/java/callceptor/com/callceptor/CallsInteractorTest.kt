package callceptor.com.callceptor;

import callceptor.com.callceptor.data.models.Call
import callceptor.com.callceptor.data.repositories.calls.LocalCallsDataStore
import callceptor.com.callceptor.utils.AppConstants
import io.reactivex.Single
import io.reactivex.SingleEmitter
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

/**
 * Created by Tomislav on 29,August,2018
 */
class CallsInteractorTest {

    val calls = ArrayList(listOf(
            Call("12.12.2018.", 1, "12", "+3859112345678", "https://cdn.pixabay.com/photo/2013/04/06/11/50/image-editing-101040_960_720.jpg", "User", 1535527902032, "1"),
            Call("12.11.2018.", 4, "5", "+3859116427582", "https://cdn.pixabay.com/photo/2013/04/06/11/50/image-editing-101040_960_720.jpg", "User2", 1535527902033, "4"),
            Call(),
            Call("1.1.2018.", 3, "3", "+3859112343245", "https://cdn.pixabay.com/photo/2013/04/06/11/50/image-editing-101040_960_720.jpg", "User3", 1535527902034, "3")
    ))

    @Mock
    lateinit var localCallsDataStore: LocalCallsDataStore

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }


    @Test
    fun saveGetCalls() {

        val t : LongArray = longArrayOf(0,1,2,3)

        val mockSingleSave = Single.create {e: SingleEmitter<LongArray>? -> e?.onSuccess (t)}

        `when`(localCallsDataStore.saveAllCalls(calls)).thenReturn(mockSingleSave)

        val saveCallsTestObserver = localCallsDataStore.saveAllCalls(calls).test()

        saveCallsTestObserver.assertNoErrors()
        saveCallsTestObserver.assertValue { t: LongArray ->
            t.size == 4
        }

        val mockSingleGet = Single.create {e: SingleEmitter<ArrayList<Call>>? -> e?.onSuccess (calls)}.toFlowable()

        `when`(localCallsDataStore.getCalls(0, AppConstants.PAGE_ENTRIES)).thenReturn(mockSingleGet)

        val getCallsTestObserver = localCallsDataStore.getCalls(0, AppConstants.PAGE_ENTRIES).test()

        getCallsTestObserver.assertNoErrors()
        getCallsTestObserver.assertValue { callsResponse: ArrayList<Call> -> callsResponse.size == 4 }
        getCallsTestObserver.assertValue { callsResponse: ArrayList<Call> -> callsResponse[0].name == "User" }
        getCallsTestObserver.assertValue { callsResponse: ArrayList<Call> -> callsResponse[3].number == "+3859112343245" }
    }

    @Test
    fun deleteCalls(){

        val mockSingleDelete = Single.create {e: SingleEmitter<Int>? -> e?.onSuccess (4)}

        `when`(localCallsDataStore.deleteCalls()).thenReturn(mockSingleDelete)

        val deleteCallsTestObserver = localCallsDataStore.deleteCalls().test()

        deleteCallsTestObserver.assertNoErrors()
        deleteCallsTestObserver.assertValue { t: Int ->
            t == 4
        }

    }

}