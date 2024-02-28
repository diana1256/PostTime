package work.time.worktim.data.model

import com.google.gson.annotations.SerializedName

data class DataItem(@SerializedName("work_date")
                    val workDate: String = "",
                    @SerializedName("work_date_end")
                    val workDateEnd: String = "",
                    @SerializedName("user_time_penalty")
                    val userTimePenalty: String = "",
                    @SerializedName("created_at")
                    val createdAt: String = "",
                    @SerializedName("user_time_end")
                    val userTimeEnd: String = "",
                    @SerializedName("user_time_start")
                    val userTimeStart: String = "",
                    @SerializedName("reestr_details")
                    val reestrDetails: List<Reestretail>,
                    @SerializedName("late")
                    val late: String = "",
                    @SerializedName("updated_at")
                    val updatedAt: String = "",
                    @SerializedName("user_id")
                    val userId: Int = 0,
                    @SerializedName("id")
                    val id: Int = 0,
                    @SerializedName("work_date_start")
                    val workDateStart: String = "",
                    @SerializedName("user")
                    val user: UserItem
)