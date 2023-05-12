package com.example.tfg.Admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tfg.R
import com.example.tfg.api.UserListResponse

class ListaUsersAdapter(
    private val usrList: List<UserListResponse.UserListResponseItem>,
    val OnClick: (UserListResponse.UserListResponseItem) -> Unit
) : RecyclerView.Adapter<ListaUsersAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_users, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var data = usrList[position]
        holder.bind(data)
        holder.itemView.setOnClickListener {
            OnClick(data)
        }
    }

    override fun getItemCount(): Int {
        return usrList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(data: UserListResponse.UserListResponseItem) {
            itemView.findViewById<TextView>(R.id.tvNombreUsrLista).text = data.username
            //  itemView.setOnClickListener {
            //     Log.v("Pulso sobre", item.displayName.toString())

        }
    }
}

