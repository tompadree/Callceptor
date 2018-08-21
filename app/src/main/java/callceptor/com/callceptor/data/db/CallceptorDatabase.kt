package callceptor.com.callceptor.data.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import callceptor.com.callceptor.data.models.ContactModule

/**
 * Created by Tom on 21.8.2018..
 */
@Database(entities = [ContactModule::class],version = 1)
abstract class CallceptorDatabase : RoomDatabase() {
    abstract fun getCallceptorDao(): CallceptorDAO
}