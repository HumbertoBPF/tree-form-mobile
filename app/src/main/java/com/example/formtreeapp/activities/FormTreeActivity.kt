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
import com.example.formtreeapp.R
import com.example.formtreeapp.adapters.OptionsAdapter
import com.example.formtreeapp.databinding.ActivityFormTreeBinding
import com.example.formtreeapp.models.Form
import com.example.formtreeapp.models.TreeNode
import com.example.formtreeapp.retrofit.FormTreeApiHelper
import com.example.formtreeapp.utils.checkSession
import com.example.formtreeapp.utils.getAuthorizationHeader
import com.example.formtreeapp.utils.logout
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FormTreeActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityFormTreeBinding.inflate(layoutInflater)
    }

    private val service by lazy {
        FormTreeApiHelper().service
    }

    private var currentNode: TreeNode? = null

    private val circularProgressIndicator by lazy {
        binding.circularProgressIndicator
    }

    private val currentNodeTextView by lazy {
        binding.currentNodeTextView
    }

    private val optionsRecyclerView by lazy {
        binding.optionsRecyclerView
    }

    private val endButton by lazy {
        binding.endButton
    }

    private val previousButton by lazy {
        binding.previousButton
    }

    private val decisionStack = mutableListOf<TreeNode>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val formId = intent.getStringExtra("form_id")

        if (formId == null) {
            finish()
            return
        }

        lifecycleScope.launch {
            checkSession(this@FormTreeActivity)
        }

        lifecycleScope.launch(IO) {
            val authorizationHeader = getAuthorizationHeader(this@FormTreeActivity)

            service.getForm(authorizationHeader, formId).enqueue(object : Callback<Form> {
                override fun onResponse(call: Call<Form>, response: Response<Form>) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()

                        if ((responseBody != null) && (responseBody.formTree != null)) {
                            circularProgressIndicator.visibility = GONE
                            currentNode = responseBody.formTree[0]
                            configureButtons()
                            updateOptions(currentNode!!)
                            return
                        }
                    }

                    onFailure()
                }

                override fun onFailure(call: Call<Form>, t: Throwable) {
                    onFailure()
                }

            })
        }

        setContentView(binding.root)
    }

    private fun configureButtons() {
        endButton.setOnClickListener { finish() }

        previousButton.setOnClickListener {
            if (decisionStack.size > 0) {
                val removedItem = decisionStack.removeLast()
                updateOptions(removedItem)
                return@setOnClickListener
            }

            finish()
        }
    }

    private fun onFailure() {
        lifecycleScope.launch {
            Toast.makeText(
                this@FormTreeActivity, R.string.error_loading_form, LENGTH_SHORT
            ).show()
        }
    }

    private fun updateOptions(node: TreeNode) {
        val children = node.children

        currentNodeTextView.text = node.label

        endButton.isEnabled = children.isEmpty()

        if (children.size > 1) {
            optionsRecyclerView.adapter = OptionsAdapter(
                this@FormTreeActivity,
                children
            ) { option ->
                decisionStack.add(node)
                updateOptions(option)
            }
        } else if (children.size == 1) {
            optionsRecyclerView.adapter = OptionsAdapter(
                this@FormTreeActivity,
                listOf(TreeNode("next", getString(R.string.next_option), children[0].children))
            ) { _ ->
                decisionStack.add(node)
                updateOptions(children[0])
            }
        } else {
            optionsRecyclerView.adapter = null
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
                    logout(this@FormTreeActivity)
                }
                true
            }
            R.id.info_icon -> {
                val intent = Intent(this@FormTreeActivity, InfoActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}