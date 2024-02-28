package work.time.worktim.data.model

import com.google.gson.annotations.SerializedName

data class ReestrDetailsItem(@SerializedName("reestr_id")
                             val reestrId: Int = 0,
                             @SerializedName("work_start")
                             val workStart: String = "",
                             @SerializedName("updated_at")
                             val updatedAt: String = "",
                             @SerializedName("work_end")
                             val workEnd: String = "",
                             @SerializedName("created_at")
                             val createdAt: String = "",
                             @SerializedName("id")
                             val id: Int = 0)