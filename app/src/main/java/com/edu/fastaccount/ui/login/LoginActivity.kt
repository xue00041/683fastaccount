package com.edu.fastaccount.ui.login

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.edu.fastaccount.R
import com.edu.fastaccount.data.DBHelper
import com.edu.fastaccount.data.UserManager
import com.edu.fastaccount.ui.main.MainActivity
import com.edu.fastaccount.utils.SPUtil.get
import com.edu.fastaccount.utils.SPUtil.put

/**
 * LoginActivity
 */
class LoginActivity : AppCompatActivity() {
    private var login_name_et: EditText? = null
    private var login_code_et: EditText? = null
    private var tv_register: TextView? = null
    private var btn_login: Button? = null
    private var mDbHelper: DBHelper? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_login)
        mDbHelper = DBHelper(this)
        val oldName = get(this, "name", "") as String?
        val oldPwd = get(this, "pwd", "") as String?
        if (!TextUtils.isEmpty(oldName) && !TextUtils.isEmpty(oldPwd)) {
            val user = mDbHelper!!.login(oldName!!, oldPwd!!)
            if (user != null) {
                UserManager.instance.user = user
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        login_name_et = findViewById(R.id.login_name_et)
        login_code_et = findViewById(R.id.login_code_et)
        tv_register = findViewById(R.id.tv_register)
        btn_login = findViewById(R.id.btn_login)
        tv_register!!.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        })
        btn_login!!.setOnClickListener(View.OnClickListener {
            val name = login_name_et!!.getText().toString().trim { it <= ' ' }
            val pwd = login_code_et!!.getText().toString().trim { it <= ' ' }
            if (name == null || TextUtils.isEmpty(name)) {
                Toast.makeText(this@LoginActivity, "username plz", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
            if (pwd == null || TextUtils.isEmpty(pwd)) {
                Toast.makeText(this@LoginActivity, "password plz", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
            val user = mDbHelper!!.login(name, pwd)
            if (user != null) {
                UserManager.instance.user = user
                put(this@LoginActivity, "name", name)
                put(this@LoginActivity, "pwd", pwd)
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this@LoginActivity, "login failed", Toast.LENGTH_SHORT).show()
            }
        })
    }
}