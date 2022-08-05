package marketwatch.com.app.marketwatch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Html
import com.google.android.gms.ads.AdView

import com.google.firebase.analytics.FirebaseAnalytics

import com.google.firebase.database.DatabaseReference

import com.google.firebase.database.FirebaseDatabase

import androidx.recyclerview.widget.LinearLayoutManager

import androidx.recyclerview.widget.RecyclerView

import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import com.firebase.ui.database.FirebaseRecyclerAdapter
import java.util.*
import android.view.View

import android.widget.TextView
import androidx.recyclerview.widget.DefaultItemAnimator

import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver

import android.view.LayoutInflater
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.Query
import com.google.android.gms.tasks.OnCompleteListener

import android.text.TextUtils
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import java.lang.Exception


class Chat_activity : AppCompatActivity() {
    private lateinit var btnSend: ImageView
    private lateinit var edtMessage: EditText
    private lateinit var  rvMessage: RecyclerView
    private lateinit var  linearLayoutManager: LinearLayoutManager
    private var mAppPreference: AppPreference? = null
    private var mFirebaseDatabase: FirebaseDatabase? = null
    private var mDatabaseReference: DatabaseReference? = null
    var ago: TimeAgo? = null
    var date: Date? = null
    private var adapter: FirebaseRecyclerAdapter<Message, ChatViewHolder>? = null
    var progress: ProgressBar? = null
    private var mAdView: AdView? = null
    private var mInterstitialAd: InterstitialAd? = null
    private lateinit var analytics: FirebaseAnalytics
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_activity)
        MobileAds.initialize(this) {}
        supportActionBar!!.title = Html.fromHtml("<font color='#ffffff'>Market Watch</font>")

        date = Date()
        ago = TimeAgo(this@Chat_activity)
        setupUI()
        intiRecycle()


        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            if (mInterstitialAd != null) {
                mInterstitialAd?.show(this)
            }
        },5000)
    }

    fun setupUI() {
        mAppPreference =  AppPreference(this);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase?.getReference();

        btnSend = findViewById<View>(R.id.btn_send) as ImageView
        edtMessage = findViewById<View>(R.id.edt_message) as EditText
        rvMessage = findViewById<RecyclerView>(R.id.rv_chat) as RecyclerView
        progress = findViewById<View>(R.id.progress) as ProgressBar
        mAdView=findViewById(R.id.adView);
        val adRequest = AdRequest.Builder().build()
        mAdView?.loadAd(adRequest)
        analytics = Firebase.analytics
        analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, "Chat_Activity")
        }
        InterstitialAd.load(this,"ca-app-pub-2227111631738399/4386459572", adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.d("tag", adError?.toString())
                mInterstitialAd=null;
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                Log.d("TAG", "Ad was loaded.")
                mInterstitialAd = interstitialAd


            }

        })
        progress?.visibility = View.GONE
        rvMessage.setHasFixedSize(true)
        linearLayoutManager =
            LinearLayoutManager(this@Chat_activity, LinearLayoutManager.VERTICAL, false)
        rvMessage.setLayoutManager(linearLayoutManager)
        rvMessage.setItemAnimator(DefaultItemAnimator())
        btnSend.setOnClickListener {
            val message = edtMessage.text.toString().trim { it <= ' ' }
            if (!TextUtils.isEmpty(message)) {
                val param: MutableMap<String, Any?> = HashMap()
                param["sender"] = mAppPreference!!.email
                param["message"] = message
                param["username"] = mAppPreference!!.getuserName()
                param["times"] = Date().toString()
                edtMessage.setText("")
                mDatabaseReference!!.child("chat")
                    .push()
                    .setValue(param)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            rvMessage.postDelayed(
                                { rvMessage.smoothScrollToPosition(rvMessage.adapter!!.itemCount - 1) },
                                800
                            )
                            // linearLayoutManager.scrollToPosition(adapter.getItemCount() - 1);
                            Log.d("SendMessage", "Sukses")
                        } else {
                            Log.d("SendMessage", "failed ")
                        }
                    }
            }
        }

    }

    override fun onStart() {
        super.onStart()
        rvMessage.adapter=adapter;
        adapter?.startListening();
    }

    override fun onStop() {
        super.onStop()
        adapter!!.stopListening()
    }

    fun intiRecycle() {
        val query: Query =
            mDatabaseReference!!.child("chat").limitToLast(50)
        val options: FirebaseRecyclerOptions<Message> = FirebaseRecyclerOptions.Builder<Message>()
            .setQuery(query, Message::class.java)
            .build()
      adapter = object : FirebaseRecyclerAdapter<Message, ChatViewHolder>(options) {
                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
                    // Create a new instance of the ViewHolder, in this case we are using a custom
                    // layout called R.layout.message for each item
                    val view: View = LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_row_chat, parent, false)
                    return ChatViewHolder(view)
                }

                override fun onBindViewHolder(
                    holder: ChatViewHolder,
                    position: Int,
                    model: Message
                ) {
                    holder?.tvMessage?.text = model.message
                    holder?.tvEmail?.text = model.username
                    try {
                        holder?.tv_ago?.text = ago!!.timeAgo(Date(model.times))

                    }catch (e:Exception){
                        e.printStackTrace()
                    }
                }
            }

        adapter?.registerAdapterDataObserver(object : AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                rvMessage.postDelayed({
                    if (adapter?.getItemCount()!! > 0) {
                        rvMessage.smoothScrollToPosition(rvMessage.adapter!!.itemCount - 1)
                    }
                }, 500)
            }
        })

    }
    class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvEmail: TextView = itemView.findViewById(R.id.tv_sender)
        var tvMessage: TextView = itemView.findViewById(R.id.tv_message)
        var tv_ago: TextView = itemView.findViewById(R.id.tv_ago)

    }
}