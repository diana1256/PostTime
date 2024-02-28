package work.time.worktim.data.model

import com.google.gson.annotations.SerializedName


data class Auth(@SerializedName("last_name")
                val last_name: String = "",
                @SerializedName("id")
                val id: Int = 0,
                @SerializedName("position")
                val position: String = "",
                @SerializedName("department")
                val department: String = "",
                @SerializedName("remote")
                val remote: Boolean = false,
                @SerializedName("first_name")
                val first_name: String = "",
                @SerializedName("token")
                val token: String = "")

data class ResultData(@SerializedName("last_name")
                      val last_name: String = "",
                      @SerializedName("id")
                      val id: Int = 0,
                      @SerializedName("position")
                      val position: String = "",
                      @SerializedName("department")
                      val department: String = "",
                      @SerializedName("remote")
                      val remote: Boolean = false,
                      @SerializedName("first_name")
                      val first_name: String = "",
                      @SerializedName("avatar")
                      val avatar: String = "")


