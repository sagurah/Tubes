package com.example.tubes.main_fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.tubes.R
import com.example.tubes.RVPlanAdapter
import com.example.tubes.api.PlansApi
import com.example.tubes.databinding.FragmentPlansBinding
import com.example.tubes.entity.ResponseData
import com.example.tubes.models.Plans
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class PlansFragment : Fragment() {
    private var adapter: RVPlanAdapter? = null
    private var _binding: FragmentPlansBinding? = null
    private val binding get() = _binding!!
    private var queue: RequestQueue? = null

    fun refresh(){
        val refresh = parentFragmentManager.beginTransaction()
        refresh.detach(this).attach(this).commit()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        queue = Volley.newRequestQueue(requireContext())
        _binding = FragmentPlansBinding.inflate(inflater, container, false)

        var view : View = binding.root
        return view
    }
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        return inflater.inflate(R.layout.fragment_plans, container, false)
//    }

//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        return inflater.inflate(R.layout.fragment_plans, container, false)
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
        binding.srPelanggan.setOnRefreshListener ({ allPlan() })
        binding.svPelanggan.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextChange(s: String?): Boolean {
                return false
            }

            override fun onQueryTextSubmit(s: String?): Boolean {
                adapter!!.filter.filter(s)
                return false
            }
        })

        val rvProduk = binding.rvPlan
        adapter = RVPlanAdapter(ArrayList(), requireContext(), this@PlansFragment)
        rvProduk.layoutManager = LinearLayoutManager(requireContext())
        rvProduk.adapter = adapter
        allPlan()
    }
    private fun allPlan() {
        binding.srPelanggan.isRefreshing = true
        val stringRequest: StringRequest = object :
            StringRequest(Method.GET, PlansApi.GET_ALL_URL, Response.Listener { response ->
                val gson = Gson()
                Log.d("logggggg", response.toString())
                val pelanggan: Array<Plans> =
                    gson.fromJson(response, ResponseData::class.java).data.toTypedArray()
                adapter!!.setData(pelanggan)
                adapter!!.filter.filter(binding.svPelanggan!!.query)
                binding.srPelanggan!!.isRefreshing = false

                if (!pelanggan.isEmpty())
                    Toast.makeText(requireContext(), "Data Berhasil Diambil", Toast.LENGTH_SHORT)
                        .show()
                else
                    Toast.makeText(requireContext(), "Data Masih Kosong", Toast.LENGTH_SHORT).show()

            }, Response.ErrorListener { error ->
                binding.srPelanggan.isRefreshing = false
                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        requireContext(),
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
                }
            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                return headers
            }
        }
        queue!!.add(stringRequest)
    }

    public fun deletePlan(id: Int){
        setLoading(true)
        val stringRequest: StringRequest = object :
            StringRequest(Method.DELETE, PlansApi.DELETE_URL + id, Response.Listener { response ->
                setLoading(false)
                val gson = Gson()
                val pelanggan  = gson.fromJson(response, Plans::class.java)

                if(pelanggan != null)
                    Toast.makeText(requireContext(), "Data berhasil diambil", Toast.LENGTH_SHORT).show()

                allPlan()
            }, Response.ErrorListener { error ->
                setLoading(false)
                try{
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        requireContext(),
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                catch (e: Exception){
                    Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
                }
            }){
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String>{
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                return headers
            }}
        queue!!.add(stringRequest)
    }

        private fun setLoading(isLoading: Boolean){
            if(isLoading){
                requireActivity().window.setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                )
                binding.layoutLoading.root.visibility = View.VISIBLE
            }
            else{
                requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                binding.layoutLoading.root.visibility = View.INVISIBLE
            }
        }

}
