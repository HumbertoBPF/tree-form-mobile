package com.example.formtreeapp.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View.GONE
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.formtreeapp.adapters.FormAdapter
import com.example.formtreeapp.R
import com.example.formtreeapp.databinding.ActivityFormsBinding
import com.example.formtreeapp.models.GetFormsResponse
import com.example.formtreeapp.retrofit.FormTreeApiHelper
import com.example.formtreeapp.utils.checkSession
import com.example.formtreeapp.utils.getAuthorizationHeader
import com.example.formtreeapp.utils.logout
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FormsActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityFormsBinding.inflate(layoutInflater)
    }

    private val formTreeApiService by lazy {
        FormTreeApiHelper().service
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            checkSession(this@FormsActivity)
        }

        val formRecyclerView = binding.formRecyclerView
        val circularProgressIndicator = binding.circularProgressIndicator

        lifecycleScope.launch(IO) {
            val authorizationHeader = getAuthorizationHeader(this@FormsActivity)
            formTreeApiService.getForms(authorizationHeader).enqueue(object : Callback<GetFormsResponse> {
                override fun onResponse(call: Call<GetFormsResponse>, response: Response<GetFormsResponse>) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()

                        if (responseBody != null) {
                            circularProgressIndicator.visibility = GONE
                            formRecyclerView.adapter = FormAdapter(
                                this@FormsActivity,
                                responseBody.items
                            )
                        }
                    } else {
                        onFailure()
                    }
                }

                override fun onFailure(call: Call<GetFormsResponse>, t: Throwable) {
                    onFailure()
                }
            })
        }

        setContentView(binding.root)
    }

    private fun onFailure() {
        lifecycleScope.launch {
            Toast.makeText(
                this@FormsActivity, R.string.error_loading_forms, LENGTH_SHORT
            ).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.authenticated_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout_icon -> {
                lifecycleScope.launch {
                    logout(this@FormsActivity)
                }
                true
            }
            R.id.info_icon -> {
                val intent = Intent(this@FormsActivity, InfoActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}