package com.ishanknijhawan.incablet_app

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView

class CustomerAdapter(val items: ArrayList<CustomerItem>, val context: Context) :
    RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.customer_layout, parent, false)
        )
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.apply {
            userName.text = items[position].name
            status.text = items[position].status

            when (items[position].status) {
                "active" -> status.setTextColor(context.resources.getColor(R.color.green))
                "left" -> status.setTextColor(context.resources.getColor(R.color.red))
                else -> status.setTextColor(context.resources.getColor(R.color.yellow))
            }

            if (items[position].gender == "f")
                gender.text = "Gender: Female"
            else
                gender.text = "Gender: Male"

            age.text = "Age: ${items[position].age}"
            date.text = "Age: ${items[position].date}"
        }

        val isExpanded = items[position].isExpanded
        holder.bottomLayout.visibility = if (isExpanded) View.VISIBLE else View.GONE

        if (holder.bottomLayout.visibility == View.VISIBLE) {
            holder.button.setImageResource(android.R.color.transparent)
            holder.button.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24)
        } else {
            holder.button.setImageResource(android.R.color.transparent)
            holder.button.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
        }

        //expands and collapses the layout
        holder.button.setOnClickListener {
            items[position].isExpanded = !items[position].isExpanded
            notifyItemChanged(position)
        }

        holder.itemView.setOnClickListener {
            items[position].isExpanded = !items[position].isExpanded
            notifyItemChanged(position)
        }

        //open profile picture on full screen
        holder.imageView.setOnClickListener {
            val intent =  Intent(context, PhotoActivity::class.java)
            intent.putExtra("url", items[position].img)
            context.startActivity(intent)
        }

        //pass profile picture into imageView
        Glide.with(context).load(items[position].img)
            .placeholder(R.drawable.ic_outline_account_circle_24).into(holder.imageView)
    }


}

//custom ViewHolder class
class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val imageView: CircleImageView = itemView.findViewById(R.id.profile_image)
    val userName: TextView = itemView.findViewById(R.id.tvName)
    val status: TextView = itemView.findViewById(R.id.tvStatus)
    val age: TextView = itemView.findViewById(R.id.tvAge)
    val gender: TextView = itemView.findViewById(R.id.tvGender)
    val button: ImageButton = itemView.findViewById(R.id.navigate)
    val date: TextView = itemView.findViewById(R.id.tvDate)
    val bottomLayout: RelativeLayout = itemView.findViewById(R.id.bottomLayout)
}
