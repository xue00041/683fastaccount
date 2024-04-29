package com.edu.fastaccount.ui.account

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.edu.fastaccount.R
import com.edu.fastaccount.data.DBHelper
import com.edu.fastaccount.data.KeepAccountBean
import com.edu.fastaccount.data.UserManager
import com.edu.fastaccount.utils.TimeDateUtils
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Collections
import java.util.Date
import java.util.TimeZone

class AccountFragment : Fragment() {
    private var selectis: ArrayList<KeepAccountBean>? = null
    private lateinit var myAdapter: AccountAdapter
    private lateinit var iv_add: FloatingActionButton
    var mDbHelper: DBHelper? = null
    private lateinit var recyclerview: RecyclerView
    private val TAG = "jcy-AccountFragment"
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_account, container, false)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mDbHelper = DBHelper(requireContext())
        recyclerview = view.findViewById<RecyclerView>(R.id.recyclerview)

        iv_add = view.findViewById<FloatingActionButton>(R.id.fab_add) as FloatingActionButton
        myAdapter = AccountAdapter(this)
        val layoutManager = LinearLayoutManager(activity)
        recyclerview.layoutManager = layoutManager
        recyclerview.adapter = myAdapter

        iv_add.setOnClickListener {
            val intent = Intent(requireContext(), AddKeppAccountActivity::class.java)
            startActivity(intent)
        }
    }


    override fun onResume() {
        super.onResume()
        getDbData()
    }

    fun getDbData() {
        selectis = mDbHelper!!.selectis(UserManager.instance.user!!.id)
        Collections.reverse(selectis)
        dealAll(selectis!!)
        myAdapter.mDatas?.clear()
        myAdapter.mDatas?.addAll(selectis!!)
        myAdapter.notifyDataSetChanged()
    }

    private fun dealAll(accounts: List<KeepAccountBean>) {
        var all = 0.0
        for (bean in accounts) {
            all = all + bean.money
        }

    }


}