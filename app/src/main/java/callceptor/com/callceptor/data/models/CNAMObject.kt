package callceptor.com.callceptor.data.models;

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.support.annotation.NonNull
import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by Tomislav on 04,September,2018
 */
@Entity(tableName = "cnam")
class CNAMObject : Serializable {

    @PrimaryKey
    @NonNull
    @SerializedName("number")
    var number: String? = null

    @SerializedName("name")
    var name: String? = null

    @SerializedName("uri")
    var uri: String? = null

    @SerializedName("price")
    var price: Float? = null

    constructor()
}
