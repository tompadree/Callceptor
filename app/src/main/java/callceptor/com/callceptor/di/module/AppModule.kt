package callceptor.com.callceptor.di.module

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by Tom on 21.8.2018..
 */
@Module
class AppModule(private val mApplication: Application) {

    @Provides
    @Singleton
    fun provideApplication(): Context {
        return this.mApplication
    }
}