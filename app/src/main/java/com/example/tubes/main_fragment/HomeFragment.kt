package com.example.tubes.main_fragment

import android.Manifest
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tubes.*
import com.master.permissionhelper.PermissionHelper

class HomeFragment : Fragment() {

    private var permissionHelper: PermissionHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        permissionHelper = PermissionHelper(this, arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE), 100)
        permissionHelper?.denied {
            if(it){
                Log.d(TAG, "Permission denied")
                permissionHelper?.openAppDetailsActivity()
            } else{
                Log.d(TAG, "Permission denied")
            }
        }

        permissionHelper?.requestAll {
            Log.d(TAG, "Permission granted")
        }

        permissionHelper?.requestIndividual {
            Log.d(TAG, "Some permission granted")
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionHelper?.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

}