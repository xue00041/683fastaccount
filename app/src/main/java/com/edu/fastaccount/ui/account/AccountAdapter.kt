package com.edu.fastaccount.ui.account

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.edu.fastaccount.R
import com.edu.fastaccount.data.KeepAccountBean

class AccountAdapter(private val fragment: AccountFragment) :
    RecyclerView.Adapter<AccountAdapter.ViewHolder>() {

    var mDatas = ArrayList<KeepAccountBean>()

    inner class ViewHolder(convertView: View) : RecyclerView.ViewHolder(convertView) {
        var tv_time = convertView.findViewById<android.view.View?>(R.id.tv_time) as TextView?
        var tv_type = convertView.findViewById<android.view.View?>(R.id.tv_type) as TextView?
        var tv_money = convertView.findViewById<android.view.View?>(R.id.tv_money) as TextView?
        var tv_location =
            convertView.findViewById<android.view.View?>(R.id.tv_location) as TextView?
        var tv_cost_type =
            convertView.findViewById<android.view.View?>(R.id.tv_cost_type) as TextView?
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_keep_account, parent, false)
        val holder = ViewHolder(view)
        view.setOnClickListener {
            val position = holder.adapterPosition
            var keepAccountBean = mDatas[position]
            val intent = Intent(fragment.context, AddKeppAccountActivity::class.java)
            intent.putExtra("id", keepAccountBean.id)
            intent.putExtra("type", keepAccountBean.type)
            intent.putExtra("money", keepAccountBean.money)
            intent.putExtra("location", keepAccountBean.location)
            intent.putExtra("costType", keepAccountBean.costType)
            fragment.startActivity(intent)
        }
        view.setOnLongClickListener {
            val position = holder.adapterPosition
            var keepAccountBean = mDatas[position]
            val bulder =
                AlertDialog.Builder(fragment.requireContext())
            bulder.setTitle("Tip")
            bulder.setMessage("Do you want to delete it?")
            bulder.setPositiveButton(
                "Cancel"
            ) { dialog, which -> dialog.dismiss() }
            bulder.setNegativeButton(
                "OK"
            ) { dialog, which ->
                fragment.mDbHelper!!.delData(keepAccountBean!!.id!!)
                fragment.getDbData()
                dialog.dismiss()
            }
            bulder.create().show()
            true
        }
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val keepAccountBean = mDatas[position]
        holder.tv_location!!.setText(keepAccountBean.location)
        holder.tv_type!!.text = if (keepAccountBean.type === 1) "Expense" else "Income"
        holder.tv_time!!.setText(keepAccountBean.createTime)
        holder.tv_money!!.setText(keepAccountBean.money.toString())
        holder.tv_cost_type!!.setText(keepAccountBean.costType.toString())
    }

    override fun getItemCount() = mDatas.size

}
