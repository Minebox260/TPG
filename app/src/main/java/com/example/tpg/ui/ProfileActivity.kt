package com.example.tpg.ui

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import com.example.tpg.R
import com.example.tpg.classes.Profile
import com.example.tpg.data.DataProvider
import com.squareup.picasso.Picasso
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.launch

class ProfileActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutInflater.inflate(R.layout.activity_profile, findViewById(R.id.content_frame), true)

        setActionBarTitle(getString(R.string.profil))
        val txtNomTechnicien: TextView = findViewById(R.id.txtNomTechnicien)
        val txtEmail: TextView = findViewById(R.id.txtEmail)
        val txtTelephone: TextView = findViewById(R.id.txtTelephone)
        val txtEntreprise: TextView = findViewById(R.id.txtEntreprise)
        val imageProfile: ImageView = findViewById(R.id.profileImage)

        val defaultPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        // On récupère l'ID fournie, ou, par défaut, celle de l'utilisateur connecté
        val profileId = intent.getStringExtra("userId") ?: defaultPreferences.getString("userId", "")
        val loggedInUserId = defaultPreferences.getString("userId", "")

        val logoutBtn: Button = findViewById(R.id.deconnecter_button)
        val appelBtn: Button = findViewById(R.id.appel_button)
        val logoutLayout: LinearLayout = findViewById(R.id.deconnecter_layout)
        val appelLayout: LinearLayout = findViewById(R.id.appel_layout)

        if (profileId == loggedInUserId) {
            logoutLayout.visibility = Button.VISIBLE
        }

        if (!profileId.isNullOrEmpty())
            getProfileData(
                profileId,
                txtNomTechnicien,
                txtEmail,
                txtTelephone,
                txtEntreprise,
                imageProfile,
                appelBtn,
                appelLayout,
                profileId != loggedInUserId)


        logoutBtn.setOnClickListener {
            logout()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun logout() {
        lifecycleScope.launch {
            val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this@ProfileActivity)
            val editor = sharedPreferences.edit()
            editor.clear()
            editor.apply()
            DataProvider.supabase.auth.signOut()
        }
    }

    private fun getProfileData(
        userId: String,
        txtNomTechnicien: TextView,
        txtEmail: TextView,
        txtTelephone: TextView,
        txtEntreprise: TextView,
        imageProfile: ImageView,
        appelBtn: Button,
        appelLayout: LinearLayout,
        showAppelBtn: Boolean) {

        lifecycleScope.launch {
            val profile = DataProvider.supabase.from("profiles").select {
                filter {
                    Profile::user_id eq userId
                }
            }.decodeSingleOrNull<Profile>()

            if (profile != null) {
                txtNomTechnicien.text = profile.name
                txtEmail.text = profile.email
                txtTelephone.text = profile.phone
                txtEntreprise.text = profile.entreprise
                if (!profile.profile_icon_url.isNullOrEmpty()) {
                    Picasso.get().load(profile.profile_icon_url).into(imageProfile)
                }

                if (showAppelBtn && profile.email.isNotEmpty()) {
                    appelLayout.visibility = Button.VISIBLE
                    appelBtn.setOnClickListener {
                        val uri = "msteams://teams.microsoft.com/l/call/0/0?users=${profile.email}&withVideo=true"
                        val myIntent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                        myIntent.`package` = "com.microsoft.teams"
                        startActivity(myIntent)
                    }
                }
            } else {
                Toast.makeText(this@ProfileActivity, "Impossible de trouver l'utilisateur", Toast.LENGTH_SHORT).show()
                finish()
                val intent = Intent(this@ProfileActivity, ScannerActivity::class.java)
                startActivity(intent)
            }
        }
    }
}