package work.time.worktim.ui.util

import android.content.Context
import android.content.SharedPreferences

class Pref ( context: Context) {
    val sharedPref: SharedPreferences =
        context.getSharedPreferences("presences", Context.MODE_PRIVATE)

    fun isLogin(): String {
        return sharedPref.getString("login", "").toString()
    }

    fun setLogin(login: String) {
        sharedPref.edit().putString("login",login ).apply()
    }

    fun isPassword(): String {
        return sharedPref.getString("password", "").toString()
    }

    fun setPassword(login: String) {
        sharedPref.edit().putString("password",login ).apply()
    }

    fun isId(): String {
        return sharedPref.getString("attendance", "").toString()
    }

    fun setId(login: String) {
        sharedPref.edit().putString("attendance",login ).apply()
    }

    fun isBoardingShowed():Boolean{
        return sharedPref.getBoolean("board",false)
    }

    fun setBoardingShowed(isSnow:Boolean){
        sharedPref.edit().putBoolean("board",isSnow).apply()
    }

    fun setRemote(isSnow: Boolean) {
        sharedPref.edit().putBoolean("remote", isSnow).apply()
    }

    fun isRemote():Boolean{
        return sharedPref.getBoolean("remote",false)
    }


}