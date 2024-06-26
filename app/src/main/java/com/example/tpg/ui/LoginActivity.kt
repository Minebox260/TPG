package com.example.tpg.ui

import android.Manifest
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.preference.PreferenceManager
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.example.tpg.R
import com.example.tpg.data.DataProvider
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.gotrue.auth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class User(val username: String, val password: String)

class LoginActivity : AppCompatActivity() {
    private lateinit var codeScanner: CodeScanner
    private val cameraRequestCode = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        setSupportActionBar(findViewById(R.id.my_toolbar))
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupPermissions()

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        val usernameEditText: EditText = findViewById(R.id.username)
        val passwordEditText: EditText = findViewById(R.id.password)

        val btnLogin: Button = findViewById(R.id.login_button)

        btnLogin.setOnClickListener {
            val email = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                login(email, password, sharedPreferences)
            } else {
                Toast.makeText(this, getString(R.string.connexion_manquant), Toast.LENGTH_SHORT).show()
            }
        }

        val scannerView = findViewById<CodeScannerView>(R.id.scanner_view)
        codeScanner = CodeScanner(this, scannerView)

        codeScanner.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
        codeScanner.formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat
        codeScanner.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
        codeScanner.scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
        codeScanner.isAutoFocusEnabled = true // Whether to enable auto focus or not
        codeScanner.isFlashEnabled = false // Whether to enable flash or not

        codeScanner.decodeCallback = DecodeCallback {
            runOnUiThread {
                val user: User
                try {
                    user = Json.decodeFromString<User>(it.text)
                } catch (e: Exception) {
                    Log.e("PMR", e.message ?: "")
                    Toast.makeText(this, getString(R.string.qr_code_invalide), Toast.LENGTH_SHORT).show()
                    codeScanner.startPreview()
                    return@runOnUiThread
                }
                login(user.username, user.password, sharedPreferences)
            }
        }
        codeScanner.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
            runOnUiThread {
                Toast.makeText(this, "Camera initialization error: ${it.message}",
                    Toast.LENGTH_LONG).show()
            }
        }

        scannerView.setOnClickListener {
            codeScanner.startPreview()
        }

    }

    private fun login(email: String, password: String, sharedPreferences: SharedPreferences) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                DataProvider.supabase.auth.signInWith(Email) {
                    this.email = email
                    this.password = password
                }

                withContext(Dispatchers.Main) {
                    Toast.makeText(this@LoginActivity, getString(R.string.connexion_reussie), Toast.LENGTH_SHORT).show()
                    val editor = sharedPreferences.edit()
                        val user = DataProvider.supabase.auth.retrieveUserForCurrentSession(false)
                        editor.putString("userId", user.id)
                        editor.putString("email", user.email)
                        editor.apply()
                    val myIntent = Intent(this@LoginActivity, MaintenanceActivity::class.java)
                    startActivity(myIntent)
                    finish()
                }

           } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@LoginActivity, getString(R.string.connexion_echoue), Toast.LENGTH_SHORT).show()
                    codeScanner.startPreview()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), cameraRequestCode)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == cameraRequestCode) {
            if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Camera permission is required to use the scanner", Toast.LENGTH_SHORT).show()
            }
        }
    }
}