package com.ishanknijhawan.incablet_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_photo.*

class PhotoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo)

        val url = intent.getStringExtra("url")
        Glide.with(this).load(url).placeholder(R.drawable.ic_outline_account_circle_24)
            .into(photo_view)
    }
}