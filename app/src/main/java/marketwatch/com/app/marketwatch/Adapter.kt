package marketwatch.com.app.marketwatch

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.analytics.FirebaseAnalytics
import marketwatch.com.app.marketwatch.Adapter.Viewholder

/**
 * Created by pratikb on 07-03-2018.
 */
class Adapter(var myctx: Context, var mydata: List<NewsData>) : RecyclerView.Adapter<Viewholder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_lists, null)
        return Viewholder(view)
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        holder.tv_stockname.text = mydata[position].storename
        holder.tv_posteddate.text = mydata[position].create_date
        holder.tv_buyprice.text = "BUY @" + mydata[position].buyprice
        if (mydata[position].stoploss.isEmpty()) {
            holder.tv_stoploss.text = "STOPLOSS " + "-"
        } else {
            holder.tv_stoploss.text = "STOPLOSS " + mydata[position].stoploss
        }
        if (mydata[position].target1.isEmpty()) {
            holder.tv_target_one.text = "-"
        } else {
            holder.tv_target_one.text = "@" + mydata[position].target1
        }
        if (mydata[position].target2.isEmpty()) {
            holder.tv_target_two.text = "-"
        } else {
            holder.tv_target_two.text = "@" + mydata[position].target2
        }
        if (mydata[position].target3.isEmpty()) {
            holder.tv_target_three.text = "-"
        } else {
            holder.tv_target_three.text = "@" + mydata[position].target3
        }
        if (mydata[position].type.equals("buy", ignoreCase = true)) {
            holder.tv_type.text = "BUY CALL"
        }
        holder.tv_duration.text = mydata[position].duration
        if (mydata[position].duration.contains("Hit")) {
            holder.tv_duration.setBackgroundColor(
                ContextCompat.getColor(
                    myctx,
                    android.R.color.holo_red_dark
                )
            )
        } else if (mydata[position].duration.contains("days")) {
            holder.tv_duration.setBackgroundColor(
                ContextCompat.getColor(
                    myctx,
                    android.R.color.darker_gray
                )
            )
        } else {
            holder.tv_duration.setBackgroundColor(
                ContextCompat.getColor(
                    myctx,
                    android.R.color.holo_green_dark
                )
            )
        }
    }

    override fun getItemCount(): Int {
        return mydata.size
    }

    inner class Viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tv_stockname: TextView
        var tv_posteddate: TextView
        var tv_buyprice: TextView
        var tv_stoploss: TextView
        var tv_target_one: TextView
        var tv_target_two: TextView
        var tv_target_three: TextView
        var tv_type: TextView
        var tv_duration: TextView
        private val iv_share: ImageView

        init {
            tv_stockname = itemView.findViewById<View>(R.id.tv_stockname) as TextView
            tv_posteddate = itemView.findViewById<View>(R.id.tv_posteddate) as TextView
            tv_buyprice = itemView.findViewById<View>(R.id.tv_buyprice) as TextView
            tv_stoploss = itemView.findViewById<View>(R.id.tv_stoploss) as TextView
            tv_target_one = itemView.findViewById<View>(R.id.tv_target_one) as TextView
            tv_target_two = itemView.findViewById<View>(R.id.tv_target_two) as TextView
            tv_target_three = itemView.findViewById<View>(R.id.tv_target_three) as TextView
            tv_type = itemView.findViewById<View>(R.id.tv_call) as TextView
            tv_duration = itemView.findViewById<View>(R.id.tv_status) as TextView
            iv_share = itemView.findViewById<View>(R.id.iv_share) as ImageView
            iv_share.setOnClickListener(object : View.OnClickListener {
                var mFirebaseAnalytics = FirebaseAnalytics.getInstance(myctx)
                override fun onClick(view: View) {
                    var msg = mydata[adapterPosition].storename
                    if (!mydata[adapterPosition].buyprice.isEmpty()) {
                        msg += " Buy@" + mydata[adapterPosition].buyprice + ""
                    }
                    if (!mydata[adapterPosition].stoploss.isEmpty() || mydata[adapterPosition].stoploss.isEmpty()) {
                        msg += " stoploss@" + mydata[adapterPosition].stoploss + " "
                    }
                    if (!mydata[adapterPosition].target1.isEmpty() || !mydata[adapterPosition].target1.contains(
                            "-"
                        )
                    ) {
                        msg += " target1@" + mydata[adapterPosition].target1 + " "
                    }
                    if (!mydata[adapterPosition].target2.isEmpty() || !mydata[adapterPosition].target2.contains(
                            "-"
                        )
                    ) {
                        msg += " target2@" + mydata[adapterPosition].target2 + " "
                    }
                    if (!mydata[adapterPosition].target3.isEmpty() || !mydata[adapterPosition].target3.contains(
                            "-"
                        )
                    ) {
                        msg += " target3@" + mydata[adapterPosition].target3 + " "
                    }
                    msg += " for get updated in stock market download our app https://play.google.com/store/apps/details?id=marketwatch.com.app.marketwatch"
                    Log.d("msg", msg!!)
                    val bundle = Bundle()
                    bundle.putString("share", mydata[adapterPosition].storename)
                    mFirebaseAnalytics.logEvent("share", bundle)
                    getBitmapFromView(msg)
                }
            })
        }
    }

    fun getBitmapFromView(txt: String?) {
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(Intent.EXTRA_TEXT, txt)
        sendIntent.type = "text/plain"
        myctx.startActivity(sendIntent)
    }
}