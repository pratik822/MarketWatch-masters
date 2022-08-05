package marketwatch.com.app.marketwatch

import android.R.attr
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast

import android.content.Intent

import com.google.gson.Gson

import com.google.firebase.auth.AuthResult

import androidx.annotation.NonNull

import com.google.android.gms.tasks.OnCompleteListener

import android.R.attr.password
import android.text.Html
import android.text.TextUtils
import android.util.Log
import android.view.View
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import marketwatch.com.app.marketwatch.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity() {
    lateinit var binding:ActivityLoginBinding;

    private var mFirebaseAuth: FirebaseAuth? = null
    private var mDatabaseReference: DatabaseReference? = null
    private var mFirebaseDatabase: FirebaseDatabase? = null
    var list: List<User> = ArrayList()
    var mAppPreference: AppPreference? = null
    private lateinit var analytics: FirebaseAnalytics
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLoginBinding.inflate(layoutInflater)
        MobileAds.initialize(this) {}
        setContentView(binding.root)
        supportActionBar!!.title = Html.fromHtml("<font color='#ffffff'>Market Watch</font>")
        mAppPreference=AppPreference(this)
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)


        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase?.getReference();
        binding.progressBar.setVisibility(View.GONE)
        analytics = Firebase.analytics
        analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, "Login_Activity")
        }

        binding.button.setOnClickListener {
            if (!TextUtils.isEmpty(binding.edtEmail.text.toString().trim()) && !TextUtils.isEmpty(
                    binding.edtPassword.text.toString().trim()
                )
            )
            {
                binding.progressBar.setVisibility(View.VISIBLE)
                mFirebaseAuth?.signInWithEmailAndPassword(
                    binding.edtEmail.text.toString().trim(),
                    binding.edtPassword.text.toString().trim()
                )
                    ?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d("mytask", task.toString())
                            val query: Query =
                                mDatabaseReference!!.child("users").orderByChild("email")
                                    .equalTo(binding.edtEmail.text.toString().trim())
                            val valueEventListener: ValueEventListener =
                                object : ValueEventListener {
                                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                                        for (task in dataSnapshot.children) {
                                            mAppPreference?.email =
                                                binding.edtEmail.text.toString().trim()
                                            mAppPreference?.setuserName(
                                                task.getValue(
                                                    User::class.java
                                                )?.userId!!
                                            )
                                            Log.d(
                                                "myusers", Gson().toJson(
                                                    task.getValue(
                                                        User::class.java
                                                    )
                                                )
                                            )
                                        }
                                    }

                                    override fun onCancelled(databaseError: DatabaseError) {}
                                }
                            query.addValueEventListener(valueEventListener)
                            startActivity(Intent(this@LoginActivity, Chat_activity::class.java))
                            finish()
//
                            binding.progressBar.setVisibility(View.GONE)
                            startActivity(Intent(this@LoginActivity, Chat_activity::class.java))
                            finish()
                        } else {
                            Toast.makeText(this@LoginActivity, "Login failed", Toast.LENGTH_SHORT)
                                .show()
                            binding.progressBar.setVisibility(View.GONE)
                        }
                    }
            }else{
                Toast.makeText(this,"Fields cannot be blank",Toast.LENGTH_LONG).show();
            }
        }
    }
}