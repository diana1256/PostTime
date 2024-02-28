package work.time.worktim.ui.util

import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import work.time.worktim.R

fun View.loadImage(url: String){
    Glide.with(this).load(url).placeholder(R.drawable.profile).circleCrop().into(this as ImageView)
}