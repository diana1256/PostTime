package work.time.worktim.ui.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import work.time.worktim.databinding.ItemSearchBinding
import work.time.worktim.data.model.DataItem
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

class AdapterSearchEnd(private val data: List<DataItem>, private val context: Context) : RecyclerView.Adapter<AdapterSearchEnd.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemSearchBinding) : RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(dataItem: DataItem) {
            val reestrDetails = dataItem.reestrDetails

            if (!reestrDetails.isNullOrEmpty()) {

                val reset: List<String> = reestrDetails.mapNotNull { reestretail ->
                    formatTime(reestretail.workEnd)
                }

                val res: List<String> = reestrDetails.mapNotNull { reestretail ->
                    formatTime(reestretail.workStart)
                }

                val nonEmptyReset = reset.filter { it.isNotBlank() }
                val nonEmptyRes = res.filter { it.isNotBlank() }

                val resString = if (nonEmptyRes.isNotEmpty()) nonEmptyRes.joinToString(", ") else "—:—"
                val resetString = if (nonEmptyReset.isNotEmpty()) nonEmptyReset.joinToString(", ") else "—:—"

                binding.tvTvews.text = resString
                binding.tvGfvd.text = resetString


                binding.tvGgff.text = "Вы опоздали на ${formatTime(dataItem.late)} минут"
                val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale("ru", "RU"))
                try {
                    val date: Date = inputFormat.parse(dataItem.workDate) ?: throw Exception("Неверный формат даты")
                    val outputFormat = SimpleDateFormat("d MMMM", Locale("ru", "RU"))
                    val outputDateString: String = outputFormat.format(date)

                    binding.tvDnwq.text = outputDateString
                    binding.tvData.text = outputDateString
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            } else {
                binding.tvD.text = ""
                 binding.tvGgff.text = ""
                 binding.tvDnwq.text = "--:--"
                binding.tvGfvd.text = "--:--"
                binding.tvTvews.text = "--:--"
                binding.tvData.text = "--:--"
            }
        }


    private fun formatTime(timeString: String?): String {
            val inputFormat = SimpleDateFormat("HH:mm:ss", Locale("ru", "RU"))
            val outputFormat = SimpleDateFormat("HH:mm", Locale("ru", "RU"))

            return try {
                val date = inputFormat.parse(timeString ?: "")
                outputFormat.format(date)
            } catch (e: Exception) {
                timeString ?: ""
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }
}