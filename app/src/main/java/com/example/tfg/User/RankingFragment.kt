package com.example.tfg.User

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tfg.R
import com.google.android.material.bottomnavigation.BottomNavigationView

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
        var rvUserInfo = view.findViewById<RecyclerView>(R.id.rvRankings)
        rvUserInfo.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rvUserInfo.adapter = RankingsAdapter{}
    }
}