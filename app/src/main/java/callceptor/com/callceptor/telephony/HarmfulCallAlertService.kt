package callceptor.com.callceptor.telephony

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.view.WindowManager
import android.widget.ImageView
import android.graphics.PixelFormat
import callceptor.com.callceptor.R
import android.R.attr.y
import android.R.attr.x
import android.view.Gravity
import android.R.attr.gravity




/**
 * Created by Tom on 23.8.2018..
 */
class HarmfulCallAlertService : Service() {

    lateinit var windowManager: WindowManager
    lateinit var chatHead : ImageView

    override fun onBind(intent: Intent?): IBinder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate() {
        super.onCreate()

        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager

        chatHead = ImageView(this)
        chatHead.setImageResource(R.mipmap.ic_contact_placeholder)

        val params = WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT)

        params.gravity = Gravity.TOP or Gravity.LEFT
        params.x = 0
        params.y = 100

        windowManager.addView(chatHead, params)

    }

    override fun onDestroy() {
        super.onDestroy()
        if (chatHead != null) windowManager.removeView(chatHead)
    }
}