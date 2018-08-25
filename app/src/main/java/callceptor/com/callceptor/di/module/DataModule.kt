package callceptor.com.callceptor.di.module

import android.arch.persistence.room.Room
import android.content.Context
import callceptor.com.callceptor.data.db.CallceptorDatabase
import callceptor.com.callceptor.domain.listeners.SystemDataManager
import callceptor.com.callceptor.telephony.SystemDataManagerImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by Tom on 21.8.2018..
 */
@Module
@Singleton
class DataModule {

    @Singleton
    @Provides
    fun provideRoomDatabase(context: Context): CallceptorDatabase {
        return Room.databaseBuilder(
                context,
                CallceptorDatabase::class.java,
                "callceptor_db").build()
    }



}