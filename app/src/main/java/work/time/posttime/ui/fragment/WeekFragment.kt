package work.time.worktim.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import work.time.worktim.R
import work.time.worktim.data.RemoteRepository
import work.time.worktim.data.Resource
import work.time.worktim.data.model.DataItem
import work.time.worktim.data.model.Qery
import work.time.worktim.data.model.Reestretail
import work.time.worktim.databinding.FragmentWeekBinding
import work.time.worktim.ui.util.Pref
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Date

class WeekFragment : Fragment() {

    private lateinit var binding: FragmentWeekBinding
    private val repository = RemoteRepository()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWeekBinding.inflate(inflater, container, false)
        binding.ivBack.setOnClickListener {
            findNavController().navigate(R.id.statisticFragment)
        }
        week()
        return binding.root
    }


    private fun week() {
        repository.login(Pref(requireContext()).isLogin()).observe(viewLifecycleOwner) { date ->
            when (date) {
                is Resource.Success -> {
                    binding.pdData.isVisible = true
                    val token = "Bearer ${date.data.token}"
                    val currentDate = Date()
                    // Получаем календарь и устанавливаем его на начало текущей недели (понедельник)
                    val calendar = Calendar.getInstance()
                    calendar.time = currentDate
                    calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
                    // Форматируем даты в строковом формате "yyyy-MM-dd"
                    val dateStart = SimpleDateFormat("yyyy-MM-dd").format(calendar.time)
                    calendar.add(
                        Calendar.DAY_OF_WEEK, 6) // Перемещаемся к концу недели (воскресенье)
                    val dateEnd = SimpleDateFormat("yyyy-MM-dd").format(calendar.time)
                    repository.getRees(token, 7, dateStart, dateEnd)
                        .observe(viewLifecycleOwner) { ser ->
                            when (ser) {
                                is Resource.Success -> {
                                    binding.pdData.isVisible = false
                                    val dataReestrs: List<Qery>? = ser.data
                                    val dataItems: List<DataItem>? =
                                        dataReestrs?.flatMap { dat -> dat.data ?: emptyList() }
                                    val reset: List<Reestretail>? =
                                        dataItems?.flatMap { qwe -> qwe.reestrDetails }

                                    val adapterSearchEnd = dataItems?.let { it1 ->
                                        AdapterWeek(
                                            it1, requireContext()
                                        )
                                    }
                                    if (reset != null) {
                                        adapterSearchEnd?.addTask(dataItems)
                                    }
                                    binding.rvWeek.adapter = adapterSearchEnd
                                }

                                is Resource.Error -> {
                                    binding.pdData.isVisible = false
                                    Toast.makeText(
                                        requireContext(),
                                        "Произошла ошибка попробуйте зайти позже!!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                is Resource.Loading -> {
                                    binding.pdData.isVisible = true
                                }
                                is Resource.Error -> {
                                    binding.pdData.isVisible = false
                                }
                            }
                        }
                }
                else -> {}
            }
        }
    }
}


/*

    override fun onDateSelected(selectedDate: LocalDate, endDate: LocalDate) {
        val token = "Bearer ${Pref(requireContext()).isPassword()}"
        binding.tvData.text = "$selectedDate : $endDate"

        // Преобразование в OffsetDateTime с использованием зоны UTC
        val offsetStartDate = OffsetDateTime.of(selectedDate.atStartOfDay(), ZoneOffset.UTC)
        val offsetEndDate = OffsetDateTime.of(endDate.atStartOfDay(), ZoneOffset.UTC)

        repository.getRees(token, 1000, offsetStartDate.toString(), offsetEndDate.toString())
            .observe(viewLifecycleOwner) { ser ->
                when (ser) {
                    is Resource.Success -> {
                        binding.tvD.text = "Поиск"
                        binding.pdData.isVisible = false
                        val dataReestrs: List<Qery>? = ser.data
                        val dataItems: List<DataItem>? =
                            dataReestrs?.flatMap { dat -> dat.data ?: emptyList() }
                        val reset: List<Reestretail>? =
                            dataItems?.flatMap { qwe -> qwe.reestrDetails }

                        val adapterSearchEnd = dataItems?.let { it1 ->
                            AdapterSearchEnd(
                                it1, requireContext()
                            )
                        }

                        if (reset != null) {
                            adapterSearchEnd?.addTask(dataItems)
                        }
                        binding.cearch.adapter = adapterSearchEnd
                    }
                    is Resource.Error -> {
                        binding.pdData.isVisible = false
                        Toast.makeText(
                            requireContext(),
                            "Произошла ошибка попробуйте зайти позже!!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    is Resource.Loading -> {
                        binding.pdData.isVisible = true
                    }
                    else -> {}
                }
            }
    }*/