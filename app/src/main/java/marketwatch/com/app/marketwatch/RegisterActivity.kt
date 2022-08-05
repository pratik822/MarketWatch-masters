package marketwatch.com.app.marketwatch

import android.R.attr
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.auth.FirebaseUser
import android.widget.Toast

import android.content.Intent

import com.google.firebase.auth.AuthResult

import androidx.annotation.NonNull

import com.google.android.gms.tasks.OnCompleteListener

import android.R.attr.password
import android.text.Html
import android.text.TextUtils
import android.view.View
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import marketwatch.com.app.marketwatch.databinding.ActivityRegisterBinding


class RegisterActivity : AppCompatActivity() {
    private lateinit var analytics: FirebaseAnalytics
    lateinit var binding: ActivityRegisterBinding;

    //initial
    private var mFirebaseDatabase: FirebaseDatabase? = null
    private var mFirebaseAuth: FirebaseAuth? = null
    private var mDatabaseReference: DatabaseReference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityRegisterBinding.inflate(layoutInflater);
        setContentView(binding.root)
        MobileAds.initialize(this) {}
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase?.getReference();
        binding.progress.visibility=View.GONE
        supportActionBar!!.title = Html.fromHtml("<font color='#ffffff'>Market Watch</font>")


        //initial
        mFirebaseAuth = FirebaseAuth.getInstance();
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)

        analytics = Firebase.analytics
        analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, "Registration_Activity")
        }

        binding.btnLogin.setOnClickListener {
          startActivity(Intent(this,LoginActivity::class.java));
        }

        binding.btnRegister?.setOnClickListener {

            if (TextUtils.isEmpty(binding.edtName.text?.toString()?.trim())) {
                binding.edtName.setError("Display name is required");
                return@setOnClickListener;
            }

            if (TextUtils.isEmpty(binding.edtEmail.text.toString().trim())) {
                binding.edtEmail.setError("Email is required");
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(binding.edtPassword.text.toString().trim())) {
                binding.edtPassword.setError("Password is required");
                return@setOnClickListener
            } else if (binding.edtPassword.length() < 6) {
                binding.edtPassword.setError("Password length should be more than 5 characters");
                return@setOnClickListener
            }

            mFirebaseAuth?.createUserWithEmailAndPassword(binding.edtEmail.text.toString().trim(), binding.edtPassword.text.toString().trim())
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            this@RegisterActivity,
                            "Register successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        val mAppPreference = AppPreference(this@RegisterActivity)
                        createNewUser(task.result.user!!)
                       // mAppPreference.setusername(edtName.getText().toString())
                      //  progress!!.visibility = View.INVISIBLE
                        startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                        finish()
                    } else {
                       // progress!!.visibility = View.INVISIBLE

                        Toast.makeText(
                            this@RegisterActivity,
                            "Please try again"+task.exception,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }
    private fun createNewUser(userFromRegistration: FirebaseUser) {
        val user = User()
        val email = userFromRegistration.email
        val userId: String = binding.edtName.text.toString().trim()
        user.email = email
        user.userId = userId
        mDatabaseReference?.child("users")?.push()?.setValue(user)
    }
}