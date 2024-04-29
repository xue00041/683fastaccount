package com.edu.fastaccount.data

class KeepAccountBean  {
    var id: Long? = null

    //    1为支出 2为收入
    var type = 0
    var createTime: String? = null
    var money = 0.0
    var costType: String? = null
    var location: String? = null
    override fun toString(): String {
        return "KeepAccountBean(id=$id, type=$type, createTime=$createTime, money=$money, costType=$costType, location=$location)"
    }

}