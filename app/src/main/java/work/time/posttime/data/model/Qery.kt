package work.time.worktim.data.model

import com.google.gson.annotations.SerializedName
import work.time.worktim.data.model.DataItem
import work.time.worktim.data.model.LinksItem

data class Qery(@SerializedName("per_page")
                val perPage: Int = 0,
                @SerializedName("data")
                val data: List<DataItem>?,
                @SerializedName("last_page")
                val lastPage: Int = 0,
                @SerializedName("next_page_url")
                val nextPageUrl: String? = null,
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
                val links: List<LinksItem>?,
                @SerializedName("to")
                val to: Int = 0,
                @SerializedName("current_page")
                val currentPage: Int = 0)