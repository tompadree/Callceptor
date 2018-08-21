package callceptor.com.callceptor.data.models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.support.annotation.NonNull
import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by Tom on 21.8.2018..
 */
@Entity(tableName = "contacts")
class ContactModule : Serializable {

    @NonNull
    @PrimaryKey
    @SerializedName("phone_number")
    var phone_number: String? = ""
}