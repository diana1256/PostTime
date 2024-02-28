package work.time.worktim.ui.fragment

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import java.time.OffsetDateTime
import java.time.ZoneOffset
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import work.time.worktim.R
import work.time.worktim.data.RemoteRepository
import work.time.worktim.data.Resource
import work.time.worktim.databinding.FragmentStatisticBinding
import work.time.worktim.ui.util.Pref
import work.time.worktim.data.model.DataItem
import work.time.worktim.data.model.DataReestr
import work.time.worktim.data.model.Qery
import work.time.worktim.data.model.Reestretail
import work.time.worktim.ui.util.loadImage
import java.time.LocalDate


class StatisticFragment : Fragment() {

    private lateinit var binding: FragmentStatisticBinding
    private val repository = RemoteRepository()

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
         binding = FragmentStatisticBinding.inflate(inflater,container,false)


        binding.ivBack.setOnClickListener {
           findNavController().navigate(R.id.homeFragment)
        }

        binding.btnyb.setOnClickListener {
            findNavController().navigate(R.id.weekFragment)
        }
        initView()
        onViewModel()
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initView() {
        repository.login(Pref(requireContext()).isLogin()).observe(viewLifecycleOwner) { date ->
            when(date){
                is Resource.Success ->{
                    binding.pdData.isVisible = false
                    val token = "Bearer ${date.data.token}"
                    repository.getReestr(token).observe(viewLifecycleOwner) {resut ->
                        run {
                            when (resut) {
                                is Resource.Success -> {
                                    binding.pdData.isVisible = false
                                    val dataReestrs: List<DataReestr>? = resut.data
                                    val dataItems: List<DataItem>? = dataReestrs?.flatMap { data -> data.data ?: emptyList() }

                                    val adapterSearchEnd = dataItems?.let {
                                        AdapterSearchEnd(
                                            it, requireContext()
                                        )
                                    }
                                        binding.cearch.adapter = adapterSearchEnd

                                }

                                is Resource.Loading -> {
                                    binding.pdData.isVisible = true
                                }

                                is Resource.Error -> {
                                    binding.pdData.isVisible = false
                                    Toast.makeText(
                                        requireContext(),
                                        "Произошла ошибка попробуйте зайти позже!!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                                else -> {}
                            }
                        }
                    }
                }
                is Resource.Loading ->{
                    binding.pdData.isVisible = true
                }
                is Resource.Error ->{
                    Toast.makeText(requireContext(), "Произошла ошибка попробуйте зайти позже!!", Toast.LENGTH_SHORT).show()
                    binding.pdData.isVisible = false
                }

                else -> {}
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun onViewModel() {
        val token = "Bearer ${Pref(requireContext()).isPassword()}"
        if (isInternetAvailable()) {
            repository.getUserData(token).observe(viewLifecycleOwner) { result ->
                when (result) {
                    is Resource.Loading -> {
                        binding.pdData.isVisible = true
                    }
                    is Resource.Error -> {
                        binding.pdData.isVisible = false
                        Toast.makeText(
                            requireContext(),
                            "Загрузка не прошла включите интернет!!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    is Resource.Success -> {
                        binding.pdData.isVisible = false
                        val originalString = result.data.department
                        val stringWithoutOtdel =
                            originalString.replaceFirst("Отдел", "").trim()
                        binding.tvZagTwo.text = stringWithoutOtdel
                        result.data.avatar.let { binding.ivProfile.loadImage(it) }
                        binding.tvFio.text =
                            "${result.data.first_name} ${result.data.last_name}"
                    }
                }
            }
        } else {
            Toast.makeText(
                requireContext(),
                "Нет подключения к интернету",
                Toast.LENGTH_SHORT
            ).show()
        }
    }



    private fun isInternetAvailable(): Boolean {
        val connectivityManager = requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
}