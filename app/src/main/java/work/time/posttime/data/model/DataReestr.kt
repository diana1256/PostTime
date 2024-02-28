package work.time.worktim.data.model

import com.google.gson.annotations.SerializedName

data class DataReestr(@SerializedName("per_page")
                      val perPage: Int = 0,
                      @SerializedName("data")
                      val data: List<DataItem>?,
                      @SerializedName("last_page")
                      val lastPage: Int = 0,
                      @SerializedName("next_page_url")
                      val nextPageUrl: String = "",
                      @SerializedName("prev_page_url")
                      val prevPageUrl: String? = null,
                      @SerializedName("first_page_url")
                      val firstPageUrl: String = "",
                      @SerializedName("path")
                      val path: String = "",
                      @SerializedName("total")
                      val total: Int = 0,
                      @SerializedName("last_page_url")
                      val lastPageUrl: String = "",
                      @SerializedName("from")
                      val from: Int = 0,
                      @SerializedName("links")
                      val links: List<Links>?,
                      @SerializedName("to")
                      val to: Int = 0,
                      @SerializedName("current_page")
                      val currentPage: Int = 0)


data class UserItem(@SerializedName("birthday")
                    val birthday: String = "",
                    @SerializedName("fired")
                    val fired: String? = null,
                    @SerializedName("gender")
                    val gender: Boolean = false,
                    @SerializedName("affiliate_id")
                    val affiliateId: Int = 0,
                    @SerializedName("department_id")
                    val departmentId: Int = 0,
                    @SerializedName("work_time_end")
                    val workTimeEnd: String = "",
                    @SerializedName("last_name")
                    val lastName: String = "",
                    @SerializedName("active")
                    val active: Boolean = false,
                    @SerializedName("remote")
                    val remote : Boolean = false,
                    @SerializedName("created_at")
                    val createdAt: String = "",
                    @SerializedName("email_verified_at")
                    val emailVerifiedAt: String? = null,
                    @SerializedName("work_time_start")
                    val workTimeStart: String = "",
                    @SerializedName("middle_name")
                    val middleName: String = "",
                    @SerializedName("penalty_time")
                    val penaltyTime: String = "",
                    @SerializedName("updated_at")
                    val updatedAt: String = "",
                    @SerializedName("phone")
                    val phone: String = "",
                    @SerializedName("role_id")
                    val roleId: Int = 0,
                    @SerializedName("hired")
                    val hired: String = "",
                    @SerializedName("id")
                    val id: Int = 0,
                    @SerializedName("author_id")
                    val authorId: Int = 0,
                    @SerializedName("first_name")
                    val firstName: String = "",
                    @SerializedName("email")
                    val email: String = "",
                    @SerializedName("status")
                    val status: Boolean = false,
                    @SerializedName("position_id")
                    val positionId: Int = 0)

data class Dataem(@SerializedName("work_date")
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
                    val reestrDetails: List<ReestrDetailsItem>?,
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

data class Links(@SerializedName("active")
                     val active: Boolean = false,
                     @SerializedName("label")
                     val label: String = "",
                     @SerializedName("url")
                     val url: String? = null)


data class Reestretail(@SerializedName("reestr_id")
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

