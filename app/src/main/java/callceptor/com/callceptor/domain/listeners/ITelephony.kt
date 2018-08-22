package com.android.internal.telephony


/**
 * Created by Tomislav on 22,August,2018
 */
interface ITelephony {

    fun endCall(): Boolean

    fun answerRingingCall()

    fun silenceRinger()
}