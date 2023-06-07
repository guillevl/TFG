package com.example.tfg.Admin

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

class DetalleEventosAdapterMainAdmin(
    private val listaUsersEventosMainAdmin: List<EventData.Data.Attributes.Users.Data>,
    val OnClick: (String) -> Unit
): RecyclerView.Adapter<DetalleEventosAdapterMainAdmin.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_users, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var data = listaUsersEventosMainAdmin[position]
        holder.bind(data)
        holder.itemView.setOnClickListener {
            OnClick(data.id.toString())
        }
    }

    override fun getItemCount(): Int {
        return listaUsersEventosMainAdmin.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: EventData.Data.Attributes.Users.Data) {
            itemView.findViewById<TextView>(R.id.tvNombreUsrLista).text = item.attributes.name+ " "+item.attributes.apellido
            itemView.findViewById<TextView>(R.id.tvUsrnameUsrLista).text = "@"+item.attributes.username
            Glide.with(itemView)
                .load(item.attributes.foto_perfil)
                .into(itemView.findViewById<ImageView>(R.id.imgPerfilListaAdmin))
        }
    }
}

