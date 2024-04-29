package com.edu.fastaccount.ui.me

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.edu.fastaccount.R
import com.edu.fastaccount.data.User
import com.edu.fastaccount.data.UserManager
import com.edu.fastaccount.ui.login.LoginActivity
import com.edu.fastaccount.ui.login.UserInfoActivity
import com.edu.fastaccount.utils.SPUtil

class MeFragment : Fragment() {
    private var user: User? = null
    lateinit var tv_useName: TextView;
    lateinit var btn_edit: Button
    lateinit var btn_logout: Button
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_me, container, false)
        tv_useName = root.findViewById(R.id.tv_useName)
        btn_edit = root.findViewById(R.id.btn_edit)
        btn_logout = root.findViewById(R.id.btn_logout)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        user = UserManager.instance.user
        tv_useName.setText(user!!.name)
        btn_edit.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                val bulder =
                    AlertDialog.Builder(context!!)
                bulder.setTitle("Tip")
                bulder.setMessage("Whether to change user information?")
                bulder.setPositiveButton(
                    "Cancel"
                ) { dialog, which -> dialog.dismiss() }
                bulder.setNegativeButton(
                    "OK"
                ) { dialog, which ->
                    val intent = Intent(context!!, UserInfoActivity::class.java)
                    startActivity(intent)
                    dialog.dismiss()
                }
                bulder
                    .create()
                    .show()
            }
        })
        btn_logout.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                val bulder =
                    AlertDialog.Builder(context!!)
                bulder.setTitle("Tip")
                bulder.setMessage("Do you want to log out?")
                bulder.setPositiveButton(
                    "Cancel"
                ) { dialog, which -> dialog.dismiss() }
                bulder.setNegativeButton(
                    "Ok"
                ) { dialog, which ->
                    SPUtil.put(context!!, "name", "")
                    SPUtil.put(context!!, "pwd", "")
                    val intent = Intent(context!!, LoginActivity::class.java)
                    startActivity(intent)
                    activity!!.finish()
                    dialog.dismiss()
                }
                bulder
                    .create()
                    .show()
            }
        })
    }
}