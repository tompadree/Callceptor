package callceptor.com.callceptor;

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import callceptor.com.callceptor.data.db.CallceptorDAO
import callceptor.com.callceptor.data.db.CallceptorDatabase
import callceptor.com.callceptor.data.models.Call
import callceptor.com.callceptor.data.repositories.calls.LocalCallsDataStore
import callceptor.com.callceptor.utils.AppConstants
import io.reactivex.Single
import io.reactivex.SingleEmitter
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

/**
 * Created by Tomislav on 29,August,2018
 */
class CallsInteractorTest {


    lateinit var localCallsDataStore: LocalCallsDataStore


    @Mock
    lateinit var database: CallceptorDatabase

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

//        callceptorDAO = database.getCallceptorDao()
        localCallsDataStore = LocalCallsDataStore(database)
    }


    @Test
    fun saveCalls() {
        val calls = ArrayList(listOf(
                Call("12.12.2018.", 1, "12", "+3859112345678", "https://cdn.pixabay.com/photo/2013/04/06/11/50/image-editing-101040_960_720.jpg", "User", 1535527902032, "1"),
                Call("12.11.2018.", 4, "5", "+3859116427582", "https://cdn.pixabay.com/photo/2013/04/06/11/50/image-editing-101040_960_720.jpg", "User2", 1535527902033, "4"),
                Call(),
                Call("1.1.2018.", 3, "3", "+3859112343245", "https://cdn.pixabay.com/photo/2013/04/06/11/50/image-editing-101040_960_720.jpg", "User3", 1535527902034, "3")
        ))
        var t = LongArray(4)
//        t = (1,2,3,4)
        t[0] = 1
        t[1] = 2
        t[2] = 2
        t[3] = 3
        val test : LongArray = t
        val mockSingle = //calls
                Single.fromCallable { e: SingleEmitter<LongArray>? ->
                    e?.onSuccess(test) }


//            callceptorDatabase.getCallceptorDao().saveIncomingCalls(calls) }

        Mockito.`when`(localCallsDataStore.saveAllCalls(calls
                )).thenReturn(mockSingle) //.test()

        val resultSingle = localCallsDataStore.saveAllCalls(calls)

        val testObserver = resultSingle.test()
        https://github.com/kozmi55/Kotlin-Android-Examples

        testObserver.assertNoErrors()
        testObserver.assertValue { t: LongArray ->
            t.size == 4
        }


        val getCalls = localCallsDataStore.getCalls(0, AppConstants.PAGE_ENTRIES).test()

        getCalls.assertNoErrors()
        getCalls.assertValue { messages: ArrayList<Call> -> messages.size == 4 }
        getCalls.assertValue { messages: ArrayList<Call> -> messages[0].name == "User" }
        getCalls.assertValue { messages: ArrayList<Call> -> messages[3].number == "+3859112343245" }
    }

}