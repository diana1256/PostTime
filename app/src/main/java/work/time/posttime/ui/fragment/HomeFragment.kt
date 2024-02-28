@file:Suppress("NAME_SHADOWING")

package work.time.worktim.ui.fragment

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import work.time.worktim.R
import work.time.worktim.data.RemoteRepository
import work.time.worktim.data.Resource
import work.time.worktim.data.Tim
import work.time.worktim.databinding.FragmentHomeBinding
import work.time.worktim.ui.fragment.ScanerFragment.Companion.RESULT
import work.time.worktim.ui.util.Pref
import work.time.worktim.ui.util.loadImage
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var gpsUtils: GpsUtils
    private var isViewInitialized = false
    private val repository = RemoteRepository()
    private lateinit var clickCountRef: DatabaseReference // Add this line

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        gpsUtils = GpsUtils(requireContext())
        val currentDate = getCurrentDate()
        onViewModel()
        val userKey = Pref(requireContext()).isId()
        clickCountRef = FirebaseDatabase.getInstance().getReference("attendance/$currentDate/$userKey")
        binding.btnStatis.setOnClickListener{
          findNavController().navigate(R.id.statisticFragment)
        }

        binding.btnScaner.setOnClickListener {
           handleButtonClick()
        }

        val result = arguments?.getString(RESULT)
        if (result != null) {
            if (!isViewInitialized) {
                    initView()
                isViewInitialized = true
            }
        }

        binding.btnRemote.setOnClickListener {
            onRemote()
        }
        return binding.root
    }

    private fun handleButtonClick() {
        requestCameraPermission()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun onViewModel() {
        val token = "Bearer ${Pref(requireContext()).isPassword()}"
                            if (isInternetAvailable()) {
                                repository.getUserData(token).observe(viewLifecycleOwner) { result ->
                                    when (result) {
                                        is Resource.Loading -> {
                                            binding.pdSucses.isVisible = true
                                        }
                                        is Resource.Error -> {
                                            Toast.makeText(
                                                requireContext(),
                                                "Загрузка не прошла включите интернет!!",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            binding.pdSucses.isVisible = false
                                        }
                                        is Resource.Success -> {
                                           binding.tvZag.text = result.data.position
                                            result.data.avatar.let { binding.ivProfile.loadImage(it) }
                                            binding.tvFio.text =
                                                "${result.data.first_name} ${result.data.last_name}"
                                            binding.pdSucses.isVisible = false
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
    private fun onRemote() {
        val token = "Bearer ${Pref(requireContext()).isPassword()}"
        val tokenRequestBody = Pref(requireContext()).isPassword().toRequestBody("text/plain".toMediaType())
        if (tokenRequestBody != null){
            repository.postRemote(tokenRequestBody,token).observe(viewLifecycleOwner) { data ->
                when(data){
                    is Resource.Success ->{
                        binding.pdSucses.isVisible = false
                        Toast.makeText(
                            requireContext(),
                            "Вы теперь на удалёнке!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    is Resource.Error ->{
                        Log.i("ololo", "onFailure:${data.message}")
                        Toast.makeText(requireContext(), "Вам отказано выйти на удалёнку!", Toast.LENGTH_SHORT).show()
                        binding.pdSucses.isVisible = false
                    }
                    is Resource.Loading ->{
                        binding.pdSucses.isVisible = true
                    }
                }
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun initView() {
        val token = "Bearer ${Pref(requireContext()).isPassword()}"
        val currentDate = getCurrentDate()
        val userKey =  Pref(requireContext()).isId()
        clickCountRef = FirebaseDatabase.getInstance().getReference("attendance/$currentDate/$userKey")
        // Check if the view is not null before observing data changes
        viewLifecycleOwner.lifecycle.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
            fun onResume() {
                clickCountRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val tim = snapshot.getValue(Tim::class.java)
                        val clickCount = tim?.int ?: 0
                        if (clickCount < 2) {
                            repository.getQRCode(token)
                                .observe(viewLifecycleOwner) { resource ->
                                    when (resource) {
                                        is Resource.Success -> {
                                            // Обработка успешного завершения
                                            val token =
                                                "Bearer ${Pref(requireContext()).isPassword()}"
                                            val currentDate = getCurrentDate()
                                            val resul = arguments?.getString(RESULT)
                                            binding.pdSucses.isVisible = false
                                            val input = "${resul}_$currentDate"
                                            val hashed = md5(input)
                                            val tokenRequestBody =
                                                hashed.toRequestBody("text/plain".toMediaType())
                                            if (Pref(requireContext()).isRemote()) {
                                                Toast.makeText(
                                                    requireContext(),
                                                    "Вы уже находитесь на удалённом!",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            } else {
                                                repository.postCameGone(tokenRequestBody, token)
                                                    .observe(viewLifecycleOwner) { data ->
                                                        when (data) {
                                                            is Resource.Success -> {
                                                                checkTimeAndShowToast()
                                                                clickCountRef.setValue(Tim(clickCount + 1))
                                                                binding.pdSucses.isVisible = false
                                                            }
                                                            is Resource.Error -> {
                                                                binding.pdSucses.isVisible =
                                                                    false
                                                            }

                                                            is Resource.Loading -> {
                                                                binding.pdSucses.isVisible =
                                                                    true
                                                            }
                                                        }
                                                    }
                                            }
                                        }

                                        is Resource.Error -> {
                                            binding.pdSucses.isVisible = false
                                            Toast.makeText(
                                                requireContext(),
                                                "Ваш QR код не соответствует!!",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }

                                        is Resource.Loading -> {
                                            binding.pdSucses.isVisible = true
                                        }
                                    }
                                }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })

            }
        })
    }

            @RequiresApi(Build.VERSION_CODES.O)
            private fun markAttendance(currentClickCount: Int) {
                val updatedClickCount = currentClickCount + 1
                val tim = Tim(updatedClickCount)
                // Теперь добавим запись в базу данных
                clickCountRef.setValue(tim)
                    .addOnSuccessListener {
                        checkTimeAndShowToast()
                    }
                    .addOnFailureListener { error ->
                        Log.e("FirebaseError", "Failed to save attendance: $error")
                        Toast.makeText(
                            requireContext(),
                            "Ошибка при сохранении отметки!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }


            @RequiresApi(Build.VERSION_CODES.O)
            fun checkTimeAndShowToast() {
                val view = LayoutInflater.from(requireContext()).inflate(R.layout.diolog, null)
                val builder = AlertDialog.Builder(requireContext())
                builder.setView(view)
                val dialog = builder.create()
                dialog.show()

                val v = view.findViewById<TextView>(R.id.tv_timess)
                val we = view.findViewById<TextView>(R.id.tv_time_start)
                val qw = view.findViewById<TextView>(R.id.tv_data)
                val btn = view.findViewById<MaterialButton>(R.id.btny)


                dialog.window?.setGravity(Gravity.CENTER)
                dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

                btn.setOnClickListener {
                    dialog.dismiss()
                }

                dialog.setCancelable(true)

                clickCountRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val tim = snapshot.getValue(Tim::class.java)
                        val clickCount = tim?.int ?: 0
                        if (clickCount == 0) {
                            Log.d("YourTag", "Current Time: ${getCurrentTime()}")
                            v.text = "Начало рабочего дня"
                        } else if (clickCount == 1) {
                            Log.d("YourTag", "Current Time: ${getCurrentTime()}")
                            v.text = "Конец рабочего дня"
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })

                we.text = getCurrentTime()
                qw.text = getCurrentDae()
            }


            private fun requestCameraPermission() {
                if (ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.CAMERA
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    requestLocationPermission()
                } else {
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        arrayOf(Manifest.permission.CAMERA),
                        CAMERA_PERMISSION_REQUEST
                    )
                }
            }

            private fun requestLocationPermission() {
                if (ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    checkLocationServicesEnabled()
                } else {
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        LOCATION_PERMISSION_REQUEST
                    )
                }
            }

            private fun checkLocationServicesEnabled() {
                if (gpsUtils.isGpsEnabled()) {
                    navigateToMapsFragment()
                } else {
                    gpsUtils.buildGpsDialog()
                }
            }

            private fun navigateToMapsFragment() {
                clickCountRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val tim = snapshot.getValue(Tim::class.java)
                        val clickCount = tim?.int ?: 0
                        if (clickCount < 2) {
                            findNavController().navigate(R.id.mapKitFragment)
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Вы уже сегодня отметились!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.i("cdvfbg", "onCancelled:$error")
                    }
                })
            }


            @Deprecated("Deprecated in Java")
            override fun onRequestPermissionsResult(
                requestCode: Int,
                permissions: Array<out String>,
                grantResults: IntArray
            ) {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults)
                when (requestCode) {
                    CAMERA_PERMISSION_REQUEST -> {
                        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                            requestLocationPermission()
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Разрешение на камеру отклонено!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    LOCATION_PERMISSION_REQUEST -> {
                        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                            checkLocationServicesEnabled()
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Разрешение на определение местоположения отклонено!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }

            private fun md5(input: String): String {
                val md = MessageDigest.getInstance("MD5")
                val digest = md.digest(input.toByteArray())
                return digest.joinToString("") { "%02x".format(it) }
            }

            private fun getCurrentDate(): String {
                val dateFormat = SimpleDateFormat("dd_MM_yyyy", Locale.getDefault())
                val date = Date()
                return dateFormat.format(date)
            }

            private fun getCurrentDae(): String {
                val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                val date = Date()
                return dateFormat.format(date)
            }

            @RequiresApi(Build.VERSION_CODES.O)
            private fun getCurrentTime(): String {
                val currentTime = LocalTime.now()
                val formatter = DateTimeFormatter.ofPattern("HH:mm")
                return currentTime.format(formatter)
            }


            companion object {
                const val CAMERA_PERMISSION_REQUEST = 101
                const val LOCATION_PERMISSION_REQUEST = 102
            }


}

