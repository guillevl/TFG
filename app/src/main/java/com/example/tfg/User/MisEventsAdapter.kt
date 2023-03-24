package com.example.tfg.User

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.tfg.R

class MisEventsAdapter (val OnClick: () -> Unit): RecyclerView.Adapter<MisEventsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_mis_eventos, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
        holder.itemView.setOnClickListener {
            OnClick()
        }
    }

    override fun getItemCount(): Int = 7

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val card = itemView.findViewById<CardView>(R.id.cadviewEvento)
          fun bind() {
              card.setCardBackgroundColor(generateColor().toInt())
          }

        //  itemView.setOnClickListener {
        //     Log.v("Pulso sobre", item.displayName.toString())

        //  }
    }
    fun generateColor(): Long {
        val colors = arrayListOf(
            0xffff0000, 0xff44ff00
        )
        return colors.random()
    }

}

