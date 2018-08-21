package callceptor.com.callceptor

import android.app.Application
import callceptor.com.callceptor.di.component.AppComponent
import callceptor.com.callceptor.di.module.AppModule

/**
 * Created by Tom on 19.8.2018..
 */
class App : Application() {

    var appComponent : AppComponent? = null

    override fun onCreate() {
        super.onCreate()

        instance = this

//        this.appComponent = DaggerAppComponent.builder()
//                .appModule(AppModule(this))
//                .build()


    }

    companion object {
        var instance: App? = null
    }
}