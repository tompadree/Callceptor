package callceptor.com.callceptor;

import callceptor.com.callceptor.data.db.CallceptorDAO
import callceptor.com.callceptor.data.models.Call
import callceptor.com.callceptor.utils.AppConstants.Companion.PAGE_ENTRIES
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@Suppress("NAME_SHADOWING")
/**
 * Created by Tomislav on 29,August,2018
 */
class LocalCallsDataStoreTest() {

    @Mock
    lateinit var callceptorDAO: CallceptorDAO

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun saveGetCalls() {

        val calls = ArrayList(listOf(
                Call("12.12.2018.", 1, "12", "+3859112345678", "https://cdn.pixabay.com/photo/2013/04/06/11/50/image-editing-101040_960_720.jpg", "User", 1535527902032, "1"),
                Call("12.11.2018.", 4, "5", "+3859116427582", "https://cdn.pixabay.com/photo/2013/04/06/11/50/image-editing-101040_960_720.jpg", "User2", 1535527902033,"4"),
                Call(),
                Call("1.1.2018.", 3, "3", "+3859112343245", "https://cdn.pixabay.com/photo/2013/04/06/11/50/image-editing-101040_960_720.jpg", "User3", 1535527902034,"3")))

        val t : LongArray = longArrayOf(0,1,2,3)


        `when`(callceptorDAO.saveIncomingCalls(calls)).thenReturn(t)

        val saveCalls =   Single.fromCallable { callceptorDAO.saveIncomingCalls(calls) }.test()

        saveCalls.assertNoErrors()
        saveCalls.assertValue { t: LongArray ->
            t.size == 4
        }

        `when`(callceptorDAO.getCalls(0, PAGE_ENTRIES)).thenReturn(calls)

        val getCalls = Single.fromCallable { ArrayList(callceptorDAO.getCalls(0, PAGE_ENTRIES)) }.toFlowable().test()
        getCalls.assertNoErrors()
        getCalls.assertValue { calls: ArrayList<Call> ->
            calls.size == 4
        }

    }
}