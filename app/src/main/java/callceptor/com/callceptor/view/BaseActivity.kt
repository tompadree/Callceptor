package callceptor.com.callceptor.view

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import callceptor.com.callceptor.App
import callceptor.com.callceptor.R
import callceptor.com.callceptor.di.component.AppComponent

open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
        this.getApplicationComponent()?.inject(this)
    }

    protected fun getApplicationComponent(): AppComponent? {
        return (application as App).appComponent
    }
}
