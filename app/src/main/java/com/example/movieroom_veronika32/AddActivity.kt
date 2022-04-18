package com.example.movieroom_veronika32

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.movieroom_veronika32.room.Constant
import com.example.movieroom_veronika32.room.Movie
import com.example.movieroom_veronika32.room.MovieDb
import kotlinx.android.synthetic.main.activity_add.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddActivity : AppCompatActivity() {

    val db by lazy {MovieDb(this)}
    private var movieId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        setupView()
        setUpListener()
    }

    fun setupView(){
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val intentType = intent.getIntExtra("intent_type", 0)
        when (intentType) {
            Constant.TYPE_CREATE -> {
                supportActionBar!!.title = "CREATE NEW"
                btn_save.visibility = View.VISIBLE
                btn_update.visibility = View.GONE
            }
            Constant.TYPE_READ -> {
                supportActionBar!!.title = "READ DATA"
                btn_save.visibility = View.GONE
                btn_update.visibility = View.GONE
                getMovie()
            }
            Constant.TYPE_UPDATE -> {
                supportActionBar!!.title = "EDIT DATA"
                btn_save.visibility = View.GONE
                btn_update.visibility = View.VISIBLE
                getMovie()
            }
        }
    }

    fun setUpListener(){
        btn_save.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                db.movieDao().addMovie(
                    Movie(
                        0,
                        et_title.text.toString(),
                        et_description.text.toString()
                    )
                )
                finish()
            }
        }
        btn_update.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                db.movieDao().updateMovie(
                    Movie(
                        movieId,
                        et_title.text.toString(),
                        et_description.text.toString()
                    )
                )
                finish()
            }
        }
    }

    fun getMovie(){
        movieId = intent.getIntExtra("intent_id", 0)
        CoroutineScope(Dispatchers.IO).launch {
            val movies = db.movieDao().getMovie(movieId)[0]
            et_title.setText(movies.title)
            et_description.setText(movies.desc)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}