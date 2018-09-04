package callceptor.com.callceptor.data.models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverter
import android.arch.persistence.room.TypeConverters
import android.os.Parcel
import android.os.Parcelable
import android.support.annotation.NonNull
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import java.io.Serializable

/**
 * Created by Tom on 24.8.2018..
 */
@Entity(tableName = "messages")
class Message : Parcelable, Serializable {

    var date: String? = null
    var number : String? =null
    var body : String? = null
    var name : String? = null
    @PrimaryKey
    @NonNull
    var timestamp : String? = null

    @TypeConverters(CNAMObjectConverter::class)
    var localCNAMObject : CNAMObject? = CNAMObject()

    class CNAMObjectConverter {

        @TypeConverter
        fun stringToCNAMObject(value: String): CNAMObject {

            val listType = object : TypeToken<ArrayList<String>>() {}.type
            return Gson().fromJson(value, listType)
        }

        @TypeConverter
        fun fromCNAMObjectToString(list: CNAMObject): String = Gson().toJson(list)

    }

    constructor()

    constructor(date: String?, number: String?, body: String?, name: String?, timestamp: String?, localCNAMObject: CNAMObject?) {
        this.date = date
        this.number = number
        this.body = body
        this.name = name
        this.timestamp = timestamp
        this.localCNAMObject = localCNAMObject
    }


    constructor(parcel: Parcel) : this() {
        date = parcel.readString()
        number = parcel.readString()
        body = parcel.readString()
        name = parcel.readString()
        timestamp = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(date)
        parcel.writeString(number)
        parcel.writeString(body)
        parcel.writeString(name)
        parcel.writeString(timestamp)
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