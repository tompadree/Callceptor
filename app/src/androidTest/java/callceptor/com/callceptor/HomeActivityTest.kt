package callceptor.com.callceptor

import android.content.Intent
import android.support.test.InstrumentationRegistry
import android.support.test.rule.ActivityTestRule

import callceptor.com.callceptor.di.test.TestModule
import callceptor.com.callceptor.view.activities.HomeActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by Tom on 29.8.2018..
 */
class HomeActivityTest {

    @Rule
    @JvmField
    var activityRule = ActivityTestRule(HomeActivity::class.java, true, false)



    @Before
    fun setUp() {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val app = instrumentation.targetContext.applicationContext as App
        https@ //android.jlelse.eu/complete-example-of-testing-mvp-architecture-with-kotlin-and-rxjava-part-3-df4cf3838581

        val testComponent = DaggerT.builder()
                .testModule(TestModule())
                .build()
        app.appComponent = testComponent

       // testComponent.inject(this)

        activityRule.launchActivity(Intent())
    }

    @Test
    fun testRecyclerViewShowingCorrectItems() {
        // TODO
    }
}