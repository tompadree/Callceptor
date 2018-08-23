package callceptor.com.callceptor.data.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import callceptor.com.callceptor.data.models.Contact

/**
 * Created by Tom on 21.8.2018..
 */
@Database(entities = [Contact::class],version = 1)
abstract class CallceptorDatabase : RoomDatabase() {
    abstract fun getCallceptorDao(): CallceptorDAO
}