package com.example.tfg.User

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tfg.R
import com.example.tfg.api.EventsNotFinishedResponse

class MisEventsAdapter (
    private val evtsNot: EventsNotFinishedResponse,
    val OnClick: (EventsNotFinishedResponse.Data) -> Unit)
    : RecyclerView.Adapter<MisEventsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_mis_eventos, parent, false)
        return ViewHolder(view)
    }
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
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
        val card = itemView.findViewById<CardView>(R.id.cadviewEvento)
          fun bind(data: EventsNotFinishedResponse.Data) {
              if (!data.attributes.isFinished){
                  card.setCardBackgroundColor(0xff44ff00.toInt())
              }else{
                  card.setCardBackgroundColor(0xffff0000.toInt())
              }

              itemView.findViewById<TextView>(R.id.txtNombreEventMisEvents).text = data.attributes.titulo_evento
              itemView.findViewById<TextView>(R.id.txtFechaEventoMisEventos).text = data.attributes.fecha_evento
              Glide.with(itemView)
                  .load(data.attributes.foto_evento)
                  .into(itemView.findViewById<ImageView>(R.id.ivFotoEventoMisEventos))
          }
    }
}

