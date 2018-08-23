package callceptor.com.callceptor.data.models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.support.annotation.NonNull
import com.google.gson.annotations.SerializedName

/**
 * Created by Tom on 23.8.2018..
 */
@Entity(tableName = "contacts")
class Contact {

    @PrimaryKey
    @NonNull
    @SerializedName("phoneNumber")
    var phoneNumber: String? = null

}