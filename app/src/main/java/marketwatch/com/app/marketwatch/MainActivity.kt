package marketwatch.com.app.marketwatch

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Html
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.google.gson.Gson
import com.pratik.marketwatchadmin.data.ResponseDataItem
import com.pratik.marketwatchadmin.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import marketwatch.com.app.marketwatch.ui.adapter.PostAdapter
import java.util.*
import com.google.android.gms.ads.RequestConfiguration
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import android.os.Build
import android.view.animation.Animation
import com.google.android.material.animation.AnimationUtils
import com.google.android.material.floatingactionbutton.FloatingActionButton
import marketwatch.com.app.marketwatch.ui.Education


class MainActivity : AppCompatActivity(), OnRefreshListener {
    private var recycleview: RecyclerView? = null
    private var mySwipeRefreshLayout: SwipeRefreshLayout? = null
    var mLayoutManager: LinearLayoutManager? = null
    private var progressBar: ProgressBar? = null
    var isRefresh = false
    var doubleBackToExitPressedOnce = false
    private var mAdView: AdView? = null
    var responselist:ArrayList<ResponseDataItem>?=null
    lateinit var adapter:PostAdapter;
    private lateinit var analytics: FirebaseAnalytics
    private var mInterstitialAd: InterstitialAd? = null
    var version = Build.VERSION.SDK_INT
    lateinit var floatingActionButton:FloatingActionButton;
    lateinit var floatingActionButton1:FloatingActionButton;
    lateinit var animation: Animation;



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        MobileAds.initialize(this) {}

        Firebase.messaging.unsubscribeFromTopic("market")
            .addOnCompleteListener { task ->
                var msg = "unSubscribed"
                if (!task.isSuccessful) {
                    msg = "unSubscribe failed"
                }
                Log.d("TAG--", msg)
            }


//        val configuration = RequestConfiguration.Builder()
//            .setTestDeviceIds(Arrays.asList("181d6825-3f66-4816-8a47-fd7f3ea8f68b")).build()
 //       MobileAds.setRequestConfiguration(configuration)
        analytics = Firebase.analytics
        Firebase.messaging.subscribeToTopic("market-test")
            .addOnCompleteListener { task ->
                var msg = "Subscribed"
                if (!task.isSuccessful) {
                    msg = "Subscribe failed"
                }
                Log.d("TAG", msg)
            }

        supportActionBar!!.title = Html.fromHtml("<font color='#ffffff'>Market Watch</font>")
        progressBar = findViewById<View>(R.id.progressBar) as ProgressBar
        progressBar!!.visibility = View.INVISIBLE
        animation=android.view.animation.AnimationUtils.loadAnimation(this,R.anim.shake)
        // Log.d(TAG, "Refreshed token: " + refreshedToken);
        mySwipeRefreshLayout = findViewById<View>(R.id.swiperefresh) as SwipeRefreshLayout
        floatingActionButton=findViewById(R.id.floatingActionButton);
        floatingActionButton1=findViewById(R.id.floatingActionButton_tutorial);

        floatingActionButton1.animation=animation
        mySwipeRefreshLayout!!.setOnRefreshListener {
            getPostData();
            mySwipeRefreshLayout!!.isRefreshing=false;
        }
        mAdView = findViewById<View>(R.id.adView) as AdView
        val adRequest = AdRequest.Builder().build()
        mAdView!!.loadAd(adRequest)
        InterstitialAd.load(this,"ca-app-pub-2227111631738399/4386459572", adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.d("tag", adError?.toString())
                mInterstitialAd = null
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                Log.d("TAG", "Ad was loaded.")
                mInterstitialAd = interstitialAd


            }


        })
        setadapter();
        getPostData();

        floatingActionButton.setOnClickListener {
            startActivity(Intent(this,Notifications::class.java))
        }

        floatingActionButton1.setOnClickListener {
            startActivity(Intent(this,Education::class.java))
        }



        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            if (mInterstitialAd != null) {
                mInterstitialAd?.show(this)
            }
        },4000)


    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        val menuInflater = menuInflater
//        menuInflater.inflate(R.menu.menu, menu)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when (item.itemId) {
//            R.id.menu_chat -> {
////                val pass = Intent(this@MainActivity, RegisterActivity::class.java)
////                startActivity(pass)
//            }
//        }
//        return super.onOptionsItemSelected(item)
//    }

    private fun setadapter() {
        recycleview = findViewById<View>(R.id.recycleview) as RecyclerView
        mLayoutManager = LinearLayoutManager(this)
        recycleview!!.layoutManager = mLayoutManager
        recycleview!!.itemAnimator = DefaultItemAnimator()
    }
     private fun getPostData(){
         progressBar?.visibility=View.VISIBLE
         GlobalScope.launch(Dispatchers.Main) {
             responselist= RetrofitClient.getRetrofitInstance().getPost().body()
             println("aaa"+ Gson().toJson(responselist))
             adapter= PostAdapter(this@MainActivity,responselist!!);
             recycleview?.adapter=adapter;
             progressBar?.visibility=View.GONE

         }

     }

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }
        doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()
        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }

    override fun onRefresh() {
        isRefresh = true
    }

}