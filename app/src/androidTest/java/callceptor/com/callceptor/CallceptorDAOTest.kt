package callceptor.com.callceptor

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import callceptor.com.callceptor.data.db.CallceptorDAO
import callceptor.com.callceptor.data.db.CallceptorDatabase
import callceptor.com.callceptor.data.models.Call
import callceptor.com.callceptor.data.models.Message
import callceptor.com.callceptor.utils.AppConstants.Companion.PAGE_ENTRIES
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by Tom on 28.8.2018..
 */
@RunWith(AndroidJUnit4::class)
class CallceptorDAOTest {

    lateinit var callceptorDAO: CallceptorDAO
    lateinit var database: CallceptorDatabase

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getTargetContext()
        database = Room.inMemoryDatabaseBuilder(context, CallceptorDatabase::class.java).build()
        callceptorDAO = database.getCallceptorDao()
    }

    @After
    fun tearDown() {
        database.close()
    }


    @Test
    fun testSaveGetCalls() {
        val calls = listOf(
                Call("12.12.2018.", 1, "12", "+3859112345678", "https://cdn.pixabay.com/photo/2013/04/06/11/50/image-editing-101040_960_720.jpg", "User", 1535527902032,"1"),
                Call("12.11.2018.", 4, "5", "+3859116427582", "https://cdn.pixabay.com/photo/2013/04/06/11/50/image-editing-101040_960_720.jpg", "User2", 1535527902033, "3"),
                Call("1.1.2018.", 3, "3", "+3859112343245", "https://cdn.pixabay.com/photo/2013/04/06/11/50/image-editing-101040_960_720.jpg", "User3", 1535527902034,"5"))


        val calls2 = listOf(
                Call("5.5.2018.", 5, "1", "+3859112345678", "https://cdn.pixabay.com/photo/2013/04/06/11/50/image-editing-101040_960_720.jpg", "User4", 1535527902035,"1"),
                Call("6.6.2018.", 1, "15", "+3859116427582", "https://cdn.pixabay.com/photo/2013/04/06/11/50/image-editing-101040_960_720.jpg", "User5", 1535527902036,"3"),
                Call("1.12.2018.", 6, "13", "+3859112343245", "https://cdn.pixabay.com/photo/2013/04/06/11/50/image-editing-101040_960_720.jpg", "User6", 1535527902037,"5"))

        val singleCall = Call("11.6.2018.", 4, "5", "+3859386427582", "https://cdn.pixabay.com/photo/2013/04/06/11/50/image-editing-101040_960_720.jpg", "Jake Wharton", 1535527902038,"6")


        callceptorDAO.saveIncomingCalls(ArrayList(calls))
        callceptorDAO.saveIncomingCall(singleCall)
        callceptorDAO.saveIncomingCalls(ArrayList(calls2))

        val getResults = callceptorDAO.getCalls(0, PAGE_ENTRIES)

        val expectedResult = listOf(
                Call("11.6.2018.", 4, "5", "+3859386427582", "https://cdn.pixabay.com/photo/2013/04/06/11/50/image-editing-101040_960_720.jpg", "Jake Wharton", 1535527902038,"6"),
                Call("1.12.2018.", 6, "13", "+3859112343245", "https://cdn.pixabay.com/photo/2013/04/06/11/50/image-editing-101040_960_720.jpg", "User6", 1535527902037,"5"),
                Call("6.6.2018.", 1, "15", "+3859116427582", "https://cdn.pixabay.com/photo/2013/04/06/11/50/image-editing-101040_960_720.jpg", "User5", 1535527902036,"3"),
                Call("5.5.2018.", 5, "1", "+3859112345678", "https://cdn.pixabay.com/photo/2013/04/06/11/50/image-editing-101040_960_720.jpg", "User4", 1535527902035,"1"),
                Call("1.1.2018.", 3, "3", "+3859112343245", "https://cdn.pixabay.com/photo/2013/04/06/11/50/image-editing-101040_960_720.jpg", "User3", 1535527902034,"5"),
                Call("12.11.2018.", 4, "5", "+3859116427582", "https://cdn.pixabay.com/photo/2013/04/06/11/50/image-editing-101040_960_720.jpg", "User2", 1535527902033,"3"),
                Call("12.12.2018.", 1, "12", "+3859112345678", "https://cdn.pixabay.com/photo/2013/04/06/11/50/image-editing-101040_960_720.jpg", "User", 1535527902032,"1"))


        Assert.assertEquals(getResults[0].timestamp, expectedResult[0].timestamp)
        Assert.assertEquals(getResults[6].timestamp, expectedResult[6].timestamp)
    }


    @Test
    fun testSaveGetMessages() {
        val messages = listOf(
                Message("12.12.2018.", "+3859112345678", "Incoming SMS message 1", "User", "1535527902032","1"),
                Message("12.11.2018.", "+3859116427582", "Incoming SMS message 2", "User2", "1535527902033","3"),
                Message("1.1.2018.", "+3859112343245", "Incoming SMS message 3", "User3", "1535527902034","5"))


        val messages2 = listOf(
                Message("5.5.2018.", "+3859112345678", "Incoming SMS message 4", "User4", "1535527902035","1"),
                Message("6.6.2018.", "+3859116427582", "Incoming SMS message 5", "User5", "1535527902036","3"),
                Message("1.12.2018.", "+3859112343245", "Incoming SMS message 6", "User6", "1535527902037","5"))

        val singleMessage = Message("11.6.2018.", "+3859386427582", "Incoming SMS message 7", "Jake Wharton", "1535527902038","6")


        callceptorDAO.saveIncomingMessages(ArrayList(messages))
        callceptorDAO.saveIncomingMessage(singleMessage)
        callceptorDAO.saveIncomingMessages(ArrayList(messages2))

        val getResults = callceptorDAO.getMessages(0, PAGE_ENTRIES)

        val expectedResult = listOf(
                Message("11.6.2018.", "+3859386427582", "Incoming SMS message 7", "Jake Wharton", "1535527902038","6"),
                Message("1.12.2018.", "+3859112343245", "Incoming SMS message 6", "User6", "1535527902037","5"),
                Message("6.6.2018.", "+3859116427582", "Incoming SMS message 5", "User5", "1535527902036","3"),
                Message("5.5.2018.", "+3859112345678", "Incoming SMS message 4", "User4", "1535527902035","1"),
                Message("1.1.2018.", "+3859112343245", "Incoming SMS message 3", "User3", "1535527902034","5"),
                Message("12.11.2018.", "+3859116427582", "Incoming SMS message 2", "User2", "1535527902033","3"),
                Message("12.12.2018.", "+3859112345678", "Incoming SMS message 1", "User", "1535527902032","1"))


        Assert.assertEquals(getResults[0].timestamp, expectedResult[0].timestamp)
        Assert.assertEquals(getResults[6].timestamp, expectedResult[6].timestamp)
    }


}