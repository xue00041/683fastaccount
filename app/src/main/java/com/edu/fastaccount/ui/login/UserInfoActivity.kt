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
import com.edu.fastaccount.data.User
import com.edu.fastaccount.data.UserManager
import com.edu.fastaccount.utils.SPUtil.put

/**
 * UserInfoActivity
 */
class UserInfoActivity : AppCompatActivity() {
    private var ivBack: ImageView? = null
    private var btn_update: Button? = null
    private var ed_useName: EditText? = null
    private var ed_password: EditText? = null
    private var user: User? = null
    var dbHelper: DBHelper? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_user)
        ivBack = findViewById(R.id.iv_back)
        btn_update = findViewById(R.id.btn_update)
        ed_useName = findViewById(R.id.ed_useName)
        ed_password = findViewById(R.id.ed_password)
        user = UserManager.instance.user
        ed_useName!!.setText(user!!.name)
        ed_password!!.setText(user!!.pwd)
        dbHelper = DBHelper(this)
        ivBack!!.setOnClickListener(View.OnClickListener { finish() })
        btn_update!!.setOnClickListener(View.OnClickListener {
            val name = ed_useName!!.getText().toString().trim { it <= ' ' }
            if (name == null || TextUtils.isEmpty(name)) {
                Toast.makeText(this@UserInfoActivity, "Please Input User Name", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
            val pwd = ed_password!!.getText().toString().trim { it <= ' ' }
            if (pwd == null || TextUtils.isEmpty(pwd)) {
                Toast.makeText(this@UserInfoActivity, "Please Input  Password", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
            val isRit = dbHelper!!.updateUser(name, pwd, user!!.id)
            if (isRit) {
                user!!.name = name
                user!!.pwd = pwd
                UserManager.instance.user = user
                put(this@UserInfoActivity, "name", name)
                put(this@UserInfoActivity, "pwd", pwd)
                finish()
            } else {
                Toast.makeText(this@UserInfoActivity, "User information modification failed, please try again", Toast.LENGTH_SHORT).show()
            }
        })
    }
}