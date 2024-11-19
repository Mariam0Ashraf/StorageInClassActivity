package com.example.networkapp

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var requestQueue: RequestQueue
    lateinit var titleTextView: TextView
    lateinit var descriptionTextView: TextView
    lateinit var numberEditText: EditText
    lateinit var showButton: Button
    lateinit var comicImageView: ImageView
    lateinit var comicPlaceholderTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestQueue = Volley.newRequestQueue(this)
        titleTextView = findViewById(R.id.comicTitleTextView)
        descriptionTextView = findViewById(R.id.comicDescriptionTextView)
        numberEditText = findViewById(R.id.comicNumberEditText)
        showButton = findViewById(R.id.showComicButton)
        comicImageView = findViewById(R.id.comicImageView)
        comicPlaceholderTextView = findViewById(R.id.comicNumberEditText)

        showButton.setOnClickListener {
            val comicId = numberEditText.text.toString()
            if (comicId.isNotEmpty()) {
                downloadComic(comicId)
            } else {
                Toast.makeText(this, "Please enter a comic number", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun downloadComic(comicId: String) {
        val url = "https://xkcd.com/$comicId/info.0.json"
        showLoadingPlaceholder()

        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                showComic(response)
            },
            { error ->
                comicPlaceholderTextView.apply {
                    visibility = View.VISIBLE
                }
                Toast.makeText(this, "Error loading comic", Toast.LENGTH_SHORT).show()
            }
        )
        requestQueue.add(request)
    }

    private fun showComic(comicObject: JSONObject) {
        comicPlaceholderTextView.visibility = View.GONE

        titleTextView.text = comicObject.getString("title")
        descriptionTextView.text = comicObject.getString("alt")

        Picasso.get()
            .load(comicObject.getString("img"))
            .into(comicImageView, object : com.squareup.picasso.Callback {
                override fun onSuccess() {
                    comicPlaceholderTextView.visibility = View.GONE
                }

                override fun onError(e: Exception?) {
                    comicPlaceholderTextView.apply {
                        visibility = View.VISIBLE
                    }
                }
            })
    }

    private fun showLoadingPlaceholder() {
        comicPlaceholderTextView.apply {
            visibility = View.VISIBLE
            text = "Loading comic..."
        }
    }
}
