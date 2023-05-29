package com.jay.memewave

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.jay.memewave.databinding.ActivityMainBinding
import kotlin.math.log
import com.android.volley.toolbox.JsonObjectRequest as JsonObjectRequest1

class MainActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private var memeUrl: String? = null

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Hide title bar
        try {
            this.supportActionBar!!.hide()
        } catch (e: NullPointerException) {

        }

        binding.ibtnNext.setOnClickListener {

            binding.progressBar.visibility = View.VISIBLE
            loadMeme()

        }

        binding.ibtnShare.setOnClickListener {

            if (memeUrl != null)
                shareMeme()
            else
                Toast.makeText(this, "Wait till a meme is loaded", Toast.LENGTH_SHORT).show()

        }

        // Load first meme
        loadMeme()

    }

    private fun shareMeme() {

        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_TEXT, "Hey, checkout this cool meme I found \n$memeUrl")
        intent.type = "text/plain"
        startActivity(Intent.createChooser(intent, "Share this meme using.."))
    }

    private fun loadMeme() {
        val url = Constants.MEME_API

        val jsonObjectRequest = JsonObjectRequest1(
            Request.Method.GET, url, null,
            { response ->

                // Get url of image
                var imageUrl = response.getString("url")
                memeUrl = imageUrl

                Glide.with(this)
                    .load(imageUrl)
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {

                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {

                            binding.progressBar.visibility = View.INVISIBLE
                            return false
                        }

                    })
                    .into(binding.ivMeme)


            },
            { _ ->
                // TODO: Handle error
            }
        )

        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

}