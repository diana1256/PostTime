package work.time.worktim.ui.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import work.time.worktim.data.model.DataItem
import work.time.worktim.databinding.WeekItemBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AdapterWeek(private val data: List<DataItem>, private val context: Context) : RecyclerView.Adapter<AdapterWeek.ViewHolder>() {


    private val tasklist: ArrayList<DataItem> = ArrayList(data)

    @SuppressLint("NotifyDataSetChanged")
    fun addTask(list: List<DataItem>) {
        tasklist.clear()
        tasklist.addAll(list)
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: WeekItemBinding) : RecyclerView.ViewHolder(binding.root) {
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

                val resString = nonEmptyRes.joinToString(", ")
                val resetString = nonEmptyReset.joinToString(", ")

                binding.tvTvews.text = resString
                binding.tvData.text = resetString

                val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale("ru", "RU"))

                try {
                    val date: Date = inputFormat.parse(dataItem.workDate) ?: throw Exception("Invalid date format")

                    val outputFormat = SimpleDateFormat("EEE dd MMMM",  Locale("ru", "RU"))
                    val outputDateString: String = outputFormat.format(date)

                    val correctedOutputDateString = outputDateString.replaceFirstChar { it.uppercase() }

                    binding.tvD.text = correctedOutputDateString
                } catch (e: Exception) {
                    e.printStackTrace()
                }


                binding.tvGgff.text = "Вы опоздали на ${formatTime(dataItem.late)} минут"
                } else {
                binding.tvD.text = "----.----"
                binding.tvGgff.text = ""
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
        val binding = WeekItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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