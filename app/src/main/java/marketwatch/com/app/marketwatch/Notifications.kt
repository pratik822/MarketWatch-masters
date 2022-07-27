package marketwatch.com.app.marketwatch

import android.os.Bundle
import android.text.Html
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import com.pratik.marketwatchadmin.network.RetrofitClient
import kotlinx.coroutines.launch
import marketwatch.com.app.marketwatch.databinding.ActivityNotificationsBinding
import marketwatch.com.app.marketwatch.ui.adapter.NewsPostAdapter

class Notifications : AppCompatActivity() {
    lateinit var binding: ActivityNotificationsBinding
    lateinit var mLayoutManager: RecyclerView.LayoutManager;
    lateinit var adapter: NewsPostAdapter;
    private lateinit var analytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        MobileAds.initialize(this) {}
        supportActionBar!!.title = Html.fromHtml("<font color='#ffffff'>Market Updates</font>")
        analytics = Firebase.analytics
        analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, "NotificationPage")
        }

        val adRequest = AdRequest.Builder().build()
        binding.adViews.loadAd(adRequest)
        setAdapter();

        binding.swiperefresh.setOnRefreshListener {


            getNotificationData();
            binding.swiperefresh.isRefreshing = false;

        }

        getNotificationData();
    }

    private fun setAdapter() {
        mLayoutManager = LinearLayoutManager(this)
        binding.recycleview!!.layoutManager = mLayoutManager
        binding.recycleview!!.itemAnimator = DefaultItemAnimator();

    }

    private fun getNotificationData() {
        lifecycleScope.launch {
            var notificationList =
                RetrofitClient.getRetrofitInstance()?.getPostNotification()?.body();
            println("notificationList" + notificationList)
            adapter = NewsPostAdapter(this@Notifications, notificationList!!);
            binding.recycleview?.adapter = adapter;
        }
    }
}