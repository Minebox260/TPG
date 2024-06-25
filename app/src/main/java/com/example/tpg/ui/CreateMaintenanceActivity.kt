package com.example.tpg.ui

import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import com.example.tpg.R
import com.example.tpg.classes.InsertMaintenance
import com.example.tpg.classes.MaintenanceId
import com.example.tpg.data.DataProvider
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date


class CreateMaintenanceActivity : BaseActivity() {

    private var maintenanceId: Int = 0
    private var imagePublicUrl: String? = null
    private lateinit var imageView: ImageView
    private lateinit var currentPhotoPath: String
    private lateinit var takePictureLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutInflater.inflate(R.layout.activity_create_maintenance, findViewById(R.id.content_frame), true)

        setActionBarTitle(getString(R.string.ajout_de_maintenance))

        getMaintenanceId()

        val operateurEmailTextView = findViewById<TextView>(R.id.txtOperateurEmail)
        val machineIdTextView = findViewById<TextView>(R.id.txtMachineId)
        val btnCreerMaintenance = findViewById<Button>(R.id.creer_maintenance_button)
        val btnPrendrePhoto = findViewById<Button>(R.id.prendre_photo_button)
        val edtDescription = findViewById<EditText>(R.id.edtDescription)
        imageView = findViewById(R.id.maintenanceImage)

        val machineId = intent.getStringExtra("machineId")
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val email = sharedPreferences.getString("email", "")

        operateurEmailTextView.text = email
        machineIdTextView.text = machineId

        btnPrendrePhoto.setOnClickListener {
            dispatchTakePictureIntent()
        }

        takePictureLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val file = File(currentPhotoPath)
                if (file.exists()) {
                    val bitmap = BitmapFactory.decodeFile(currentPhotoPath)
                    imageView.setImageBitmap(bitmap)

                    disableButton(btnCreerMaintenance)
                    val bucket = DataProvider.supabase.storage.from("maintenance_data")
                    val byteArray = file.readBytes()
                    lifecycleScope.launch {
                        try {
                        bucket.upload("$maintenanceId.jpg", byteArray, upsert = true)
                        imagePublicUrl = DataProvider.supabase.storage.from("maintenance_data").publicUrl("$maintenanceId.jpg")
                        enableButton(btnCreerMaintenance)
                        } catch (e: Exception) {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(this@CreateMaintenanceActivity, getString(R.string.erreur), Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }

        btnCreerMaintenance.setOnClickListener {
            createMaintenance(email, machineId, edtDescription.text.toString())
        }
    }

    private fun disableButton(button: Button) {
        button.isEnabled = false
        button.text = getString(R.string.chargement)
        button.setBackgroundColor(Color.GRAY)
    }

    private fun enableButton(button: Button) {
        button.isEnabled = true
        button.text = getString(R.string.ajouter_la_maintenance)
        button.setBackgroundColor(ContextCompat.getColor(this, R.color.primary))
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    null
                }
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "com.example.tpg.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    takePictureLauncher.launch(takePictureIntent)
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            currentPhotoPath = absolutePath
        }
    }

    private fun getMaintenanceId() {
        lifecycleScope.launch {
            try {
                maintenanceId = DataProvider.supabase.from("maintenances").select(columns = Columns.list("id")) {
                    order(column = "id", order = Order.DESCENDING)
                }.decodeSingle<MaintenanceId>().id + 1
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@CreateMaintenanceActivity, getString(R.string.erreur), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun createMaintenance(email: String?, machineId: String?, description: String) {
        if (email != null && machineId != null && maintenanceId != 0) {
            lifecycleScope.launch {
                try {
                    val userId = DataProvider.supabase.auth.retrieveUserForCurrentSession().id

                    val maintenance = InsertMaintenance(maintenanceId, description, userId, machineId, imagePublicUrl)

                    DataProvider.supabase.from("maintenances").insert(maintenance)

                    val intent = Intent(this@CreateMaintenanceActivity, MaintenanceActivity::class.java)
                    intent.putExtra("machineId", machineId)
                    startActivity(intent)
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@CreateMaintenanceActivity, getString(R.string.erreur), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}