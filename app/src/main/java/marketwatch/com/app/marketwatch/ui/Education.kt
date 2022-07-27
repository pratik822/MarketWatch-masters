package marketwatch.com.app.marketwatch.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.ads.AdRequest
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.pratik.marketwatchadmin.network.RetrofitClient
import kotlinx.coroutines.launch
import marketwatch.com.app.marketwatch.databinding.ActivityEducationBinding
import marketwatch.com.app.marketwatch.ui.adapter.EducationListAdapter

class Education : AppCompatActivity() {
    lateinit var binding:ActivityEducationBinding;
    lateinit var educationListAdapter: EducationListAdapter;
    lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var analytics: FirebaseAnalytics
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityEducationBinding.inflate(layoutInflater);
        setContentView(binding.root)
        supportActionBar!!.title = Html.fromHtml("<font color='#ffffff'>बेसिक कैंडलस्टिक पैटर्न</font>")
        analytics = Firebase.analytics
        analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, "CandlestickPatterns_Listing")
        }
        val adRequest = AdRequest.Builder().build()
        binding.adViews.loadAd(adRequest)
        setUpAdapter();
        getEducationData();
    }
    fun setUpAdapter(){
        linearLayoutManager= LinearLayoutManager(this);
        binding.rv.layoutManager=linearLayoutManager;
    }
    fun getEducationData(){
        lifecycleScope.launch {
            var body=RetrofitClient.getRetrofitInstance().getChartDetails().body();
            var list_bull=body?.filter { s->s.type=="0" }
            var list_bare=body?.filter { s->s.type=="1" }
            educationListAdapter= body?.let { EducationListAdapter(this@Education, list_bull,list_bare) }!!;
            binding.rv.adapter=educationListAdapter;

        }

    }
}