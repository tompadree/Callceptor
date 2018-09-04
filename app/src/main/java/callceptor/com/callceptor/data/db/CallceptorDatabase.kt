package callceptor.com.callceptor.data.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import callceptor.com.callceptor.data.models.CNAMObject
import callceptor.com.callceptor.data.models.Call
import callceptor.com.callceptor.data.models.Contact
import callceptor.com.callceptor.data.models.Message

/**
 * Created by Tom on 21.8.2018..
 */
@Database(entities = [Call::class, Message::class, CNAMObject::class], version = 1)
abstract class CallceptorDatabase : RoomDatabase() {
    abstract fun getCallceptorDao(): CallceptorDAO
}