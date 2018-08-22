package callceptor.com.callceptor.view

import android.os.Bundle
import android.support.v4.app.Fragment
import callceptor.com.callceptor.App
import callceptor.com.callceptor.di.component.AppComponent

open class BaseFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getApplicationComponent()?.inject(this)

    }

    protected fun getApplicationComponent(): AppComponent? {
        return App.instance?.appComponent
    }

}