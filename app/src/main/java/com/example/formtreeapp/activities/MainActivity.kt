package com.example.formtreeapp.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.formtreeapp.R
import com.example.formtreeapp.data_store.Settings
import com.example.formtreeapp.databinding.ActivityMainBinding
import com.example.formtreeapp.utils.loginCognitoUserPools
import com.example.formtreeapp.utils.logout
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val settings by lazy {
        Settings(this@MainActivity)
    }

    private val submitButton by lazy {
        binding.submitButton
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            settings.getToken().collect {token ->
                if (token != null) {
                    val intent = Intent(this@MainActivity, FormsActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }

        submitButton.setOnClickListener {
            login()
        }

        setContentView(binding.root)
    }

    private fun login() {
        val usernameTextInputLayout = binding.usernameTextInputLayout
        val usernameTextInputEditText = binding.usernameTextInputEditText

        val passwordTextInputLayout = binding.passwordTextInputLayout
        val passwordTextInputEditText = binding.passwordTextInputEditText

        val username = usernameTextInputEditText.text.toString()
        val password = passwordTextInputEditText.text.toString()

        submitButton.isEnabled = false

        lifecycleScope.launch(IO) {
            loginCognitoUserPools(username, password, { response ->
                lifecycleScope.launch {
                    submitButton.isEnabled = true
                    settings.setToken(response.authenticationResult?.idToken.toString())
                }
            }, {
                lifecycleScope.launch {
                    submitButton.isEnabled = true
                    usernameTextInputLayout.error = getString(R.string.wrong_credentials_error)
                    passwordTextInputLayout.error = getString(R.string.wrong_credentials_error)
                }
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.unauthenticated_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.info_icon -> {
                val intent = Intent(this@MainActivity, InfoActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}