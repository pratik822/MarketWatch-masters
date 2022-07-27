package marketwatch.com.app.marketwatch.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import marketwatch.com.app.marketwatch.EducationDetail
import marketwatch.com.app.marketwatch.data.ChartResponseDataItem
import marketwatch.com.app.marketwatch.databinding.EducationListBinding
import java.lang.Exception

class EducationListAdapter(
    var context: Context,
    var item: List<ChartResponseDataItem>?,
    var list_bare: List<ChartResponseDataItem>?
) : RecyclerView.Adapter<EducationListAdapter.ViewHolder>() {
    class ViewHolder(var binding: EducationListBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       var binding=EducationListBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context).load(item?.get(position)?.image).into(holder.binding.imageView5)
         holder.binding.tv1.setText(item?.get(position)?.title?.trim())
     try {
         Glide.with(context).load(list_bare?.get(position)?.image).into(holder.binding.imageView8)
         holder.binding.tv2.setText(list_bare?.get(position)?.title?.trim())

         holder.binding.imageView5.setOnClickListener {
             context.startActivity(Intent(context,EducationDetail::class.java).putExtra("title",item?.get(position)?.title).putExtra("url",item?.get(position)?.image).putExtra("description",item?.get(position)?.description))
         }
         holder.binding.imageView8.setOnClickListener {
             context.startActivity(Intent(context,EducationDetail::class.java).putExtra("title",list_bare?.get(position)?.title).putExtra("url",list_bare?.get(position)?.image).putExtra("description",list_bare?.get(position)?.description))
         }
     }catch (e:Exception){
         e.printStackTrace()
     }


    }

    override fun getItemCount(): Int {
       return item?.size!!
    }
}