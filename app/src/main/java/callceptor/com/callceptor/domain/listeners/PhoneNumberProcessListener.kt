package callceptor.com.callceptor.domain.listeners

/**
 * Created by Tom on 21.8.2018..
 */
interface PhoneNumberProcessListener {

    fun endCall(number : String)

    fun allowCallWithWarning(number : String)

    fun allowCall(number : String)
}