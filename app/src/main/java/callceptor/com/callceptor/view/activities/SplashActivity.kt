package callceptor.com.callceptor.view.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import callceptor.com.callceptor.R
import callceptor.com.callceptor.utils.AppConstants.Companion.SPLASH_DISPLAY_LENGTH

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()}, SPLASH_DISPLAY_LENGTH)

    }
}
