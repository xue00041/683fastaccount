package com.edu.fastaccount.ui.account

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AppCompatActivity
import com.edu.fastaccount.R
import com.edu.fastaccount.data.DBHelper
import com.edu.fastaccount.data.UserManager

class AddKeppAccountActivity : AppCompatActivity() {
    private var spinner: Spinner? = null
    private var adapter: ArrayAdapter<String>? = null
    private var et_money: EditText? = null
    private var et_location: EditText? = null
    private var tv_title: TextView? = null
    private var bt_add: Button? = null
    private var positionChecked = 1
    private var id: Long = -999
    private var type = 0
    private var moneyChange = 0.0
    private var location: String? = null
    private var tAdapter: ArrayAdapter<String>? = null
    private var spinner_cost_type: Spinner? = null
    private var positionCost = 0
    private var mDbHelper: DBHelper? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_add_keep_account)
        mDbHelper = DBHelper(this)
        spinner = findViewById<View>(R.id.spinner) as Spinner
        spinner_cost_type = findViewById<View>(R.id.spinner_cost_type) as Spinner
        et_money = findViewById<View>(R.id.et_money) as EditText
        et_location = findViewById<View>(R.id.et_location) as EditText
        bt_add = findViewById<View>(R.id.bt_add) as Button
        tv_title = findViewById<TextView>(R.id.tv_title)
        bt_add!!.setOnClickListener {
            val location = et_location!!.text.toString().trim { it <= ' ' }
            val money = et_money!!.text.toString().trim { it <= ' ' }
            if (TextUtils.isEmpty(money)) {
                Toast.makeText(this@AddKeppAccountActivity, "The amount cannot be empty", Toast.LENGTH_LONG).show()
            }
            if (TextUtils.isEmpty(location)) {
                Toast.makeText(this@AddKeppAccountActivity, "Location cannot be empty", Toast.LENGTH_LONG).show()
            }
            if (id == -999L) {
                mDbHelper!!.insertData(
                    positionChecked,
                    money.toDouble(),
                    t[positionCost],
                    location,
                    UserManager.instance.user!!.id
                )
                Toast.makeText(this@AddKeppAccountActivity, "Successfully added", Toast.LENGTH_LONG).show()
            } else {
                mDbHelper!!.modifyData(
                    id,
                    positionChecked,
                    money.toDouble(),
                    location,
                    t[positionCost]
                )
            }
            finish()
        }
        adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            m
        )
        tAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            t
        )

        //设置下拉列表的风格
        adapter!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        tAdapter!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        //将adapter 添加到spinner中
        spinner!!.adapter = adapter
        spinner_cost_type!!.adapter = tAdapter
        id = intent.getLongExtra("id", -999)
        if (id == -999L) {
            bt_add!!.text = "ADD"
            tv_title!!.text="ADD"
        } else {
            type = intent.getIntExtra("type", -999)
            moneyChange = intent.getDoubleExtra("money", 0.0)
            location = intent.getStringExtra("location")
            val costType = intent.getStringExtra("costType")
            spinner!!.setSelection(type - 1, true)
            spinner_cost_type!!.setSelection(getIndex(costType), true)
            et_money!!.setText(moneyChange.toString() + "")
            et_location!!.setText(location)
            bt_add!!.text = "Update"
            tv_title!!.text="Update"
        }
        spinner!!.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                positionChecked = position + 1
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        spinner_cost_type!!.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                positionCost = position
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun getIndex(costType: String?): Int {
        for (i in t.indices) {
            if (t[i] == costType) return i
        }
        return 0
    }

    companion object {
        private val m = arrayOf("Expenditure", "Income")
        private val t = arrayOf("Catering", "Sports", "Entertainment", "Daily Use")
    }
}