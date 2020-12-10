package com.example.imagesearch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso

class ImageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)

        val dialogBtn = this.findViewById<Button>(R.id.dialogBtn)
        val dialogText = this.findViewById<TextView>(R.id.dialogText)
        val dialogImage = this.findViewById<ImageView>(R.id.dialogImg)

        val imageurl = intent.getStringExtra("imageurl")
        val datetime = intent.getStringExtra("datetime").substring(0,10)
        val displaysitename = intent.getStringExtra("displaysitename")

        Glide.with(this).load(imageurl).into(dialogImage)

        dialogText.text = datetime + "  " + displaysitename
        dialogBtn.setOnClickListener {
            finish()
        }

    }
}