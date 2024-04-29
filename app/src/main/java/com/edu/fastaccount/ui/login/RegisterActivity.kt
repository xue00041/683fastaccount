package com.edu.fastaccount.ui.login

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.edu.fastaccount.R
import com.edu.fastaccount.data.DBHelper
import com.edu.fastaccount.ui.login.RegisterActivity

/**
 * RegisterActivity
 */
class RegisterActivity : AppCompatActivity() {
    private var ivBack: ImageView? = null
    private var btnRegister: Button? = null
    private var ed_useName: EditText? = null
    private var ed_password: EditText? = null
    private var mDbHelper: DBHelper? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_register)
        mDbHelper = DBHelper(this)
        ivBack = findViewById(R.id.iv_back)
        btnRegister = findViewById(R.id.btn_register)
        ed_useName = findViewById(R.id.ed_useName)
        ed_password = findViewById(R.id.ed_password)
        ivBack!!.setOnClickListener(View.OnClickListener { finish() })
        btnRegister!!.setOnClickListener(View.OnClickListener {
            val name = ed_useName!!.getText().toString().trim { it <= ' ' }
            if (name == null || TextUtils.isEmpty(name)) {
                Toast.makeText(this@RegisterActivity, "please enter user name", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
            val pwd = ed_password!!.getText().toString().trim { it <= ' ' }
            if (pwd == null || TextUtils.isEmpty(pwd)) {
                Toast.makeText(this@RegisterActivity, "please enter password", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
            val isRit = mDbHelper!!.register(pwd, name)
            if (isRit) {
                finish()
            } else {
                Toast.makeText(this@RegisterActivity, "registered failed, try again plz", Toast.LENGTH_SHORT).show()
            }
        })
    }
}