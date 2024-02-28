package work.time.worktim.data.model

import com.google.gson.annotations.SerializedName

data class Coordin(@SerializedName("latitude")
                   val latitude: String = "",
                   @SerializedName("longitude")
                   val longitude: String = "")