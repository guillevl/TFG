package com.example.tfg.User

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tfg.R
import com.example.tfg.api.EventData

class DetalleEventosAdapterMisEventos(
    private val listaUsersEventosMisEventos: List<EventData.Data.Attributes.Users.Data>,
    val OnClick: () -> Unit
): RecyclerView.Adapter<DetalleEventosAdapterMisEventos.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_users, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var data = listaUsersEventosMisEventos[position]
        holder.bind(data)
        holder.itemView.setOnClickListener {
            OnClick()
        }
    }

    override fun getItemCount(): Int {
        return listaUsersEventosMisEventos.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: EventData.Data.Attributes.Users.Data) {
            itemView.findViewById<TextView>(R.id.tvNombreUsrLista).text = item.attributes.username
            Glide.with(itemView)
                .load(item.attributes.foto_perfil)
                .into(itemView.findViewById<ImageView>(R.id.imgPerfilListaAdmin))
        }
    }
}

