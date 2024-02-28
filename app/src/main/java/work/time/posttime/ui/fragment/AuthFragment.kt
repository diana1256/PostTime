package work.time.worktim.ui.fragment

import android.os.Bundle
import android.util.Log
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
import work.time.worktim.databinding.FragmentAuthBinding
import work.time.worktim.ui.util.Pref


class AuthFragment : Fragment() {

    private lateinit var binding: FragmentAuthBinding
    private val repository = RemoteRepository()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAuthBinding.inflate(inflater,container,false)
        binding.btnSend.setOnClickListener {
            initView()
        }
        return binding.root
    }

    private fun initView() {
        repository.login(binding.etLogin.text.toString()).observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    val auth = resource.data
                    binding.pdTime.isVisible = false
                    Pref(requireContext()).setLogin(binding.etLogin.text.toString())
                    findNavController().navigate(R.id.homeFragment)
                    Pref(requireContext()).setBoardingShowed(true)
                    binding.pdTime.isVisible = false
                    Pref(requireContext()).setRemote(auth.remote)
                    Pref(requireContext()).setId(auth.id.toString())
                    Pref(requireContext()).setPassword(auth.token)
                }

                is Resource.Error -> {
                    // Обработка ошибки
                    binding.pdTime.isVisible= false
                    val errorMessage = resource.message
                    Log.i("ololo", "onFailure:$errorMessage")
                    binding.etLogin.error = "Номер не правильный!"
                    Toast.makeText(requireContext(), "Произошла ошибка убедитесь что вы правильно ввели данные!!", Toast.LENGTH_SHORT).show()
                }

                is Resource.Loading -> {
                   binding.pdTime.isVisible= true
                }
            }
        }

    }

}