package com.example.tfg.User

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tfg.R
import com.example.tfg.api.UserListResponse

class RankingsAdapter (
    private val usrList: UserListResponse,
    val OnClick: () -> Unit
): RecyclerView.Adapter<RankingsAdapter.ViewHolder>() {
    var posicion= 1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_rankings, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var data = usrList[position]
        holder.bind(data)
        holder.itemView.setOnClickListener {
            OnClick()
        }
    }

    override fun getItemCount(): Int {
        return usrList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(data: UserListResponse.UserListResponseItem) {
            itemView.findViewById<TextView>(R.id.tvNombreUsrRanking).text = data.name +" "+ data.apellido
            itemView.findViewById<TextView>(R.id.tvUsernameUsrRanking).text = "@"+data.username
            itemView.findViewById<TextView>(R.id.tvPuntosUserRanking).text = data.points.toString()+"pts"
            itemView.findViewById<TextView>(R.id.tvNumeroRanking).text = posicion.toString()
            Glide.with(itemView)
                .load(data.foto_perfil)
                .into(itemView.findViewById<ImageView>(R.id.ivFotoPerfilRanking))
            posicion +=1
        }
    }
}

