package callceptor.com.callceptor.data.db

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import callceptor.com.callceptor.data.models.Call
import callceptor.com.callceptor.data.models.Message

/**
 * Created by Tom on 21.8.2018..
 */
@Dao
interface CallceptorDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveIncomingCalls(calls: ArrayList<Call>): LongArray

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveIncomingCall(call: Call): Long

    @Query("SELECT * FROM calls LIMIT :page, :per_page ORDER BY timestamp") //" ORDER BY repoName ASC
    fun getCalls(page: Int, per_page: Int): ArrayList<Call>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveIncomingMessages(messages: ArrayList<Message>): LongArray

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveIncomingMessage(message: Message): Long

    @Query("SELECT * FROM messages LIMIT :page, :per_page ORDER BY timestamp ASC") // ORDER BY userName ASC LIMIT (:page * :per_page) , :per_page")
    fun getMessages(page: Int, per_page: Int): ArrayList<Message>


}