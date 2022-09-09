package com.example.tubes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tubes.entity.Pelanggan

class PlansFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_plans, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(context)
        val adapter : RVPelangganAdapter = RVPelangganAdapter(Pelanggan.listofPelanggan)

        val rvPelanggan : RecyclerView = view.findViewById(R.id.rv_pelanggan)

        rvPelanggan.layoutManager = layoutManager
        rvPelanggan.setHasFixedSize(true)
        rvPelanggan.adapter = adapter
    }

}