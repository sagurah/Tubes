package com.example.tubes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tubes.entity.Pelanggan
import com.example.tubes.room.JadwalDB
import com.example.tubes.room.jadwal.Jadwal
import com.example.tubes.room.jadwal.JadwalDAO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlansFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_plans, container, false)
    }

    fun refresh(){
        val refresh = parentFragmentManager.beginTransaction()
        refresh.detach(this).attach(this).commit()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(context)
        val db by lazy { JadwalDB(requireContext()) }

        CoroutineScope(Dispatchers.IO).launch {
            val jadwal = db.JadwalDAO().getJadwal()
            val adapter: RVPlanAdapter = RVPlanAdapter(jadwal.toTypedArray())
            val rvJadwal: RecyclerView = view.findViewById(R.id.rv_plan)
            rvJadwal.layoutManager = layoutManager
            rvJadwal.setHasFixedSize(true)
            rvJadwal.adapter = adapter
            refresh()
        }

    }

}