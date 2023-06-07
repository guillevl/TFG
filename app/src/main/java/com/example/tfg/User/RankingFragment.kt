package com.example.tfg.User

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tfg.Admin.ListaUsersAdapter
import com.example.tfg.R
import com.example.tfg.api.ApiRest
import com.example.tfg.api.UserListResponse
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RankingFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ranking, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ApiRest.initService()
        getRankingUsers()

    }
    private fun getRankingUsers() {
        val call = ApiRest.service.getRankingUsers()
        call.enqueue(object : Callback<UserListResponse> {
            override fun onResponse(
                call: Call<UserListResponse>,
                response: Response<UserListResponse>
            ) {
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    var usrList = body
                    view?.findViewById<TextView>(R.id.tvUsernameCartaRanking1)?.text = usrList[0].username
                    view?.findViewById<TextView>(R.id.tvPuntosCartaRanking1)?.text = usrList[0].points.toString()+"pts"
                    Glide.with(view!!)
                        .load(usrList[0].foto_perfil)
                        .into(view!!.findViewById<ImageView>(R.id.ivFotoCartaRanking1))
                    view?.findViewById<TextView>(R.id.tvUsernameCartaRanking2)?.text = usrList[1].username
                    view?.findViewById<TextView>(R.id.tvPuntosCartaRanking2)?.text = usrList[1].points.toString()+"pts"
                    Glide.with(view!!)
                        .load(usrList[1].foto_perfil)
                        .into(view!!.findViewById<ImageView>(R.id.ivFotoCartaRanking2))
                    view?.findViewById<TextView>(R.id.tvUsernameCartaRanking3)?.text = usrList[2].username
                    view?.findViewById<TextView>(R.id.tvPuntosCartaRanking3)?.text = usrList[2].points.toString()+"pts"
                    Glide.with(view!!)
                        .load(usrList[2].foto_perfil)
                        .into(view!!.findViewById<ImageView>(R.id.ivFotoCartaRanking3))
                    var rvUserInfo = view?.findViewById<RecyclerView>(R.id.rvRankings)
                    rvUserInfo?.layoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    rvUserInfo?.adapter = RankingsAdapter(usrList) {
                    }
                    Log.i("getAds", usrList.toString())
                } else {
                    Log.e("getAds", response.errorBody()?.string() ?: "Error getting user:")
                }
            }

            override fun onFailure(call: Call<UserListResponse>, t: Throwable) {
                Log.e("getAdsOnFailure", "Error: ${t.message}")
            }
        })
    }
}