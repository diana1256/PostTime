package work.time.worktim.data.model

import com.google.gson.annotations.SerializedName

data class LinksItem(@SerializedName("active")
                     val active: Boolean = false,
                     @SerializedName("label")
                     val label: String = "",
                     @SerializedName("url")
                     val url: String? = null)