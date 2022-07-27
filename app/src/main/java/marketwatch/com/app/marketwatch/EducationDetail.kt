package marketwatch.com.app.marketwatch

import android.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.MenuItem
import com.bumptech.glide.Glide
import marketwatch.com.app.marketwatch.databinding.ActivityEducationDetailBinding
import marketwatch.com.app.marketwatch.databinding.EducationListBinding
import android.content.Intent
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase


class EducationDetail : AppCompatActivity() {
    lateinit var binding:ActivityEducationDetailBinding;
    var url:String?="";
    var description:String?="";
    var title:String?=""
    private lateinit var analytics: FirebaseAnalytics
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityEducationDetailBinding.inflate(layoutInflater);
        setContentView(binding.root)
        MobileAds.initialize(this) {}
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        title= intent.extras?.getString("title")
        supportActionBar!!.title = Html.fromHtml("<font color='#ffffff'> "+title+" </font>")
        analytics = Firebase.analytics
        analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, "CandlestickPattern_Details")
        }
        val adRequest = AdRequest.Builder().build()
        binding.adViews.loadAd(adRequest)

        url= intent.extras?.getString("url")
        description=intent.extras?.getString("description")
        Glide.with(this).load(url).into(binding.iv)
        binding.tvText.setText(Html.fromHtml(description))

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}