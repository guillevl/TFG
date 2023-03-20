package com.example.tfg.User

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tfg.R

class EventsAdapter (val OnClick: () -> Unit): RecyclerView.Adapter<EventsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_evento, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //  holder.bind(data[position])
        holder.itemView.setOnClickListener {
            OnClick()
        }
    }

    override fun getItemCount(): Int = 7

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        //  fun bind(item:AgentsResponse.Agent) {

        //  itemView.setOnClickListener {
        //     Log.v("Pulso sobre", item.displayName.toString())

        //  }
    }


}

