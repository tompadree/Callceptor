package callceptor.com.callceptor.data.models

import android.arch.persistence.room.Entity
import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

/**
 * Created by Tom on 24.8.2018..
 */
@Entity(tableName = "messages")
class Message() : Parcelable, Serializable {

    var date: String? = null
    var type : Int? = null
    var number : String? =null
    var body : String? = null
    var name : String? = null

    constructor(parcel: Parcel) : this() {
        date = parcel.readString()
        type = parcel.readValue(Int::class.java.classLoader) as? Int
        number = parcel.readString()
        body = parcel.readString()
        name = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(date)
        parcel.writeValue(type)
        parcel.writeString(number)
        parcel.writeString(body)
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Message> {
        override fun createFromParcel(parcel: Parcel): Message {
            return Message(parcel)
        }

        override fun newArray(size: Int): Array<Message?> {
            return arrayOfNulls(size)
        }
    }

}