package marketwatch.com.app.marketwatch.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import marketwatch.com.app.marketwatch.data.RespNotification
import marketwatch.com.app.marketwatch.databinding.ItemListNotificationBinding


class NewsPostAdapter(var context: Context, var notificationList: RespNotification):RecyclerView.Adapter<NewsPostAdapter.ViewHolder>() {
    var ResponseDataItem=1;
    class ViewHolder( var binding:ItemListNotificationBinding) : RecyclerView.ViewHolder(binding.root) {


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding=ItemListNotificationBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binding.tvTitle.setText(notificationList.get(position).title)
        holder.binding.tvDate.setText(notificationList.get(position).create_date)

    }
    fun getBitmapFromView(txt: String?) {
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(Intent.EXTRA_TEXT, txt)
        sendIntent.type = "text/plain"
        context.startActivity(sendIntent)
    }
    override fun getItemCount(): Int {
       return notificationList.size;
    }
}