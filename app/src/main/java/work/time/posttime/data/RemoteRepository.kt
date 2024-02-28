package work.time.worktim.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import okhttp3.RequestBody
import work.time.worktim.data.model.Auth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import work.time.worktim.data.model.Coordin
import work.time.worktim.data.model.DataReestr
import work.time.worktim.data.model.Qery
import work.time.worktim.data.model.ResultData

class RemoteRepository {

    fun login(email: String): LiveData<Resource<Auth>> {
        val liveData = MutableLiveData<Resource<Auth>>()
        liveData.value = Resource.Loading

        RetrofitClient.api.login(email).enqueue(object : Callback<Auth> {
            override fun onResponse(call: Call<Auth>, response: Response<Auth>) {
                if (response.isSuccessful) {
                    val auth = response.body()
                    if (auth != null) {
                        liveData.value = Resource.Success(auth)
                    } else {
                        liveData.value = Resource.Error("Empty response body")
                    }
                } else {
                    liveData.value = Resource.Error("Unsuccessful response: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Auth>, t: Throwable) {
                liveData.value = Resource.Error(t.message ?: "Unknown error")
            }
        })

        return liveData
    }

    fun getUserData(token: String): LiveData<Resource<ResultData>> {
        val liveData = MutableLiveData<Resource<ResultData>>()

        liveData.value = Resource.Loading

        RetrofitClient.api.getUserData(token).enqueue(object :Callback<ResultData>{
            override fun onResponse(call: Call<ResultData>, response: Response<ResultData>) {
                if (response.isSuccessful) {
                    val auth = response.body()
                    if (auth != null) {
                        liveData.value = Resource.Success(auth)
                    } else {
                        liveData.value = Resource.Error("Empty response body")
                    }
                } else {
                    liveData.value = Resource.Error("Unsuccessful response: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ResultData>, t: Throwable) {
                liveData.value = Resource.Error(t.message ?: "Unknown error")
            }
        })

        return liveData
    }

    fun postCameGone(tokenMB: RequestBody,authorization: String): LiveData<Resource<String>>{
        val liveData = MutableLiveData<Resource<String>>()
        liveData.value = Resource.Loading
        RetrofitClient.api.postCameGone(tokenMB, authorization).enqueue(object :Callback<String>{
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    val auth = response.body()
                    if (auth != null) {
                        liveData.value = Resource.Success(auth)
                    } else {
                        liveData.value = Resource.Error("Empty response body")
                    }
                } else {
                    liveData.value = Resource.Error("Unsuccessful response: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                liveData.value = Resource.Error(t.message ?: "Unknown error")
            }
        })

        return liveData
    }

    fun  getReestr(token: String): LiveData<Resource<List<DataReestr>>>{
        val liveData = MutableLiveData<Resource<List<DataReestr>>>()
        liveData.value = Resource.Loading
        RetrofitClient.api.getReestr(token).enqueue(object :Callback<List<DataReestr>>{
            override fun onResponse(call: Call<List<DataReestr>>, response: Response<List<DataReestr>>) {
                if (response.isSuccessful) {
                    val auth = response.body()
                    if (auth != null) {
                        liveData.value = Resource.Success(auth)
                    } else {
                        liveData.value = Resource.Error("Empty response body")
                    }
                } else {
                    liveData.value = Resource.Error("Unsuccessful response: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<DataReestr>>, t: Throwable) {
                liveData.value = Resource.Error(t.message ?: "Unknown error")
            }
        })

        return liveData
    }

    fun postRemote(tokenMB: RequestBody,authorization: String): LiveData<Resource<String>>{
        val liveData = MutableLiveData<Resource<String>>()
        liveData.value = Resource.Loading
        RetrofitClient.api.postRemote(tokenMB, authorization).enqueue(object :Callback<String>{
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    val auth = response.body()
                    if (auth != null) {
                        liveData.value = Resource.Success(auth)
                    } else {
                        liveData.value = Resource.Error("Empty response body")
                    }
                } else {
                    liveData.value = Resource.Error("Unsuccessful response: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                liveData.value = Resource.Error(t.message ?: "Unknown error")
            }
        })

        return liveData
    }


    fun getRees(token: String, page: Int, dateStart: String, dateEnd: String): LiveData<Resource<List<Qery>>>{
        val liveData = MutableLiveData<Resource<List<Qery>>>()
        liveData.value = Resource.Loading
        RetrofitClient.api.getRees(token,page,dateStart,dateEnd).enqueue(object :Callback<List<Qery>>{
            override fun onResponse(call: Call<List<Qery>>, response: Response<List<Qery>>) {
                if (response.isSuccessful) {
                    val auth = response.body()
                    if (auth != null) {
                        liveData.value = Resource.Success(auth)
                    } else {
                        liveData.value = Resource.Error("Empty response body")
                    }
                } else {
                    liveData.value = Resource.Error("Unsuccessful response: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Qery>>, t: Throwable) {
                liveData.value = Resource.Error(t.message ?: "Unknown error")
            }
        })

        return liveData
    }


    fun getQRCode(token: String): LiveData<Resource<String>>{
        val liveData = MutableLiveData<Resource<String>>()
        liveData.value = Resource.Loading
        RetrofitClient.api.getQRCode(token).enqueue(object :Callback<String>{
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    val auth = response.body()
                    if (auth != null) {
                        liveData.value = Resource.Success(auth)
                    } else {
                        liveData.value = Resource.Error("Empty response body")
                    }
                } else {
                    liveData.value = Resource.Error("Unsuccessful response: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                liveData.value = Resource.Error(t.message ?: "Unknown error")
            }
        })

        return liveData
    }
}