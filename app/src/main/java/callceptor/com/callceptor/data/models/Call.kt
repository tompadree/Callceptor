package callceptor.com.callceptor.data.models

import android.arch.persistence.room.Entity
import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

/**
 * Created by Tom on 22.8.2018..
 */
@Entity(tableName = "calls")
class Call() : Parcelable, Serializable {

    var date: String? = null
    var type : Int? = null
    var duration : String? = null
    var number : String? =null
    var photo_uri : String? = null
    var name : String? = null

    constructor(parcel: Parcel) : this() {
        date = parcel.readString()
        type = parcel.readValue(Int::class.java.classLoader) as? Int
        duration = parcel.readString()
        number = parcel.readString()
        photo_uri = parcel.readString()
        name = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(date)
        parcel.writeValue(type)
        parcel.writeString(duration)
        parcel.writeString(number)
        parcel.writeString(photo_uri)
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Call> {
        override fun createFromParcel(parcel: Parcel): Call {
            return Call(parcel)
        }

        override fun newArray(size: Int): Array<Call?> {
            return arrayOfNulls(size)
        }
    }

}
//
//transcription,
//photo_id,
//subscription_component_name,
//type,
//geocoded_location,
//presentation,
//duration,
//subscription_id,
//is_read,
//number,
//features,
//voicemail_uri,
//normalized_number,
//logtype,
//via_number,
//matched_number,
//last_modified,
//vvm_id,
//new,
//numberlabel,
//lookup_uri,
//photo_uri,
//data_usage,
//phone_account_address,
//formatted_number,
//add_for_all_users,
//numbertype,
//countryiso,
//name,
//post_dial_digits,
//transcription_state,
//_id,