package com.example.tfg.User

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tfg.R
import com.example.tfg.api.EventsNotFinishedResponse
import com.example.tfg.api.UserListResponse

class EventsAdapter(
    private val evtsNot: EventsNotFinishedResponse,
    val OnClick: (EventsNotFinishedResponse.Data) -> Unit)
    : RecyclerView.Adapter<EventsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_evento, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //  holder.bind(data[position])
        var data = evtsNot.data[position]
        holder.bind(data)
        holder.itemView.setOnClickListener {
            OnClick(data)
        }
    }

    override fun getItemCount(): Int {
        return evtsNot.data.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(data: EventsNotFinishedResponse.Data) {
            itemView.findViewById<TextView>(R.id.tituloEventoMainUsr).text = data.attributes.titulo_evento
            //  itemView.setOnClickListener {
            //     Log.v("Pulso sobre", item.displayName.toString())

        }
    }


}

