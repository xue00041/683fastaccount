package com.edu.fastaccount.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.CursorFactory
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.edu.fastaccount.utils.TimeDateUtils
import com.edu.fastaccount.utils.TimeDateUtils.date2String
import java.util.*

class DBHelper(
    context: Context?,
    name: String?,
    factory: CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, name, factory, version) {

    //用户表
    /*表名*/
    private val TABLE_NAME_USER = "_user"

    /*id字段*/
    private val VALUE_ID = "_id"
    private val VALUE_NAME = "subject"
    private val VALUE_PWD = "body"

    /*创建表语句 语句对大小写不敏感 create table 表名(字段名 类型，字段名 类型，…)*/
    private val CREATE_USER = "create table " + TABLE_NAME_USER + "(" +
            VALUE_ID + " integer primary key," +
            VALUE_NAME + " text ," +
            VALUE_PWD + " text" +
            ")"
    private val TAG = "jcy-DBHelper"
    constructor(context: Context?) : this(context, "keepaccounts.db", null, 1) {
        Log.e(TAG, "-------> MySqliteHelper")
    }

    override fun onCreate(db: SQLiteDatabase) {
        val sql = "create table if not exists t_account" +
                "(id integer primary key autoincrement," +
                "type int(1) " +
                ",createTime text(100)" +
                ",money double(10)" +
                ",user_id integer" +
                ",costType text(100)" +
                ",location text(100))"
        db.execSQL(sql) //创建表
        //创建表
        db.execSQL(CREATE_USER)
        Log.e(TAG, "-------> onCreate")
    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {
        Log.e(
            TAG,
            "-------> onUpgrade  oldVersion = $oldVersion   newVersion = $newVersion"
        )
    }

    /**
     * 注册
     */
    fun register(pwd: String, name: String): Boolean {
        val cursor = writableDatabase.query(
            TABLE_NAME_USER,
            null,
            "$VALUE_NAME=? and $VALUE_PWD=?",
            arrayOf(name, pwd),
            null,
            null,
            null
        )
        if (cursor.count > 0) {
            cursor.close()
            return false
        }
        cursor.close()
        val values = ContentValues()
        values.put(VALUE_PWD, pwd)
        values.put(VALUE_NAME, name)
        //添加数据到数据库
        val index = writableDatabase.insert(TABLE_NAME_USER, null, values)
        writableDatabase.close()
        return if (index != -1L) {
            true
        } else {
            false
        }
    }

    /**
     * 登录
     */
    fun login(name: String, pwd: String): User? {
        val cursor = writableDatabase.query(
            TABLE_NAME_USER,
            null,
            "$VALUE_NAME=? and $VALUE_PWD=?",
            arrayOf(name, pwd),
            null,
            null,
            null
        )
        if (cursor.count == 0) {
            cursor.close()
            return null
        }
        cursor.moveToFirst()
        val user = User()
        user.id = cursor.getInt(cursor.getColumnIndex(VALUE_ID))
        user.name = cursor.getString(cursor.getColumnIndex(VALUE_NAME))
        user.pwd = cursor.getString(cursor.getColumnIndex(VALUE_PWD))
        cursor.close()
        writableDatabase.close()
        return user
    }

    fun updateUser(name: String?, pwd: String?, id: Int): Boolean {
        val values = ContentValues()
        values.put(VALUE_NAME, name)
        values.put(VALUE_PWD, pwd)

        //修改model的数据
        val index = writableDatabase.update(
            TABLE_NAME_USER,
            values,
            "$VALUE_ID=?",
            arrayOf("" + id)
        ).toLong()
        writableDatabase.close()
        return if (index != -1L) {
            true
        } else {
            false
        }
    }

    /**
     * 查询数据
     * 返回List
     */
    fun selectis(userId: Int): ArrayList<KeepAccountBean> {
        val list = ArrayList<KeepAccountBean>()
        val cursor = readableDatabase.query(
            "t_account",
            null,
            "user_id =?",
            arrayOf("" + userId),
            null,
            null,
            null
        )
        while (cursor!!.moveToNext()) {
            val userBean = KeepAccountBean()
            val id = cursor.getLong(cursor.getColumnIndex("id"))
            val type = cursor.getInt(cursor.getColumnIndex("type"))
            val createTime = cursor.getString(cursor.getColumnIndex("createTime"))
            val money = cursor.getDouble(cursor.getColumnIndex("money"))
            val costType = cursor.getString(cursor.getColumnIndex("costType"))
            val location = cursor.getString(cursor.getColumnIndex("location"))
            userBean.id = id
            userBean.type = type
            userBean.money = money
            userBean.costType = costType
            userBean.createTime = createTime
            userBean.location = location
            list.add(userBean)
            Log.e(
                TAG,
                "==============selectis======" + id + "================" + location + "================" + money
            );
        }
        cursor?.close()
        readableDatabase.close()
        return list
    }

    /**
     * 根据ID删除数据
     * id 删除id
     */
    fun delData(id: Long): Int {
        val inde = readableDatabase.delete(
            "t_account",
            "id = ?",
            arrayOf(id.toString())
        )
        Log.e(TAG, "==============deleted======================$inde")
        readableDatabase.close()
        return inde
    }

    /**
     * 根据ID修改数据
     * id 修改条码的id
     * bsid 修改的ID
     * name 修改的数据库
     */
    fun modifyData(
        id: Long,
        type: Int,
        money: Double,
        location: String?,
        costType: String?
    ): Int {
        val contentValues = ContentValues()
        contentValues.put("type", type)
        contentValues.put("money", money)
        contentValues.put(
            "createTime",
            date2String(Date(), TimeDateUtils.FORMAT_TYPE_3)
        )
        contentValues.put("location", location)
        contentValues.put("costType", costType)
        val index = readableDatabase.update(
            "t_account",
            contentValues,
            "id = ?",
            arrayOf(id.toString())
        )
        Log.e(TAG, "==============edited======================$index")
        readableDatabase.close()
        return index
    }

    /**
     * 添加数据
     * bsid 添加的数据ID
     * name 添加数据名称
     */
    fun insertData(
        type: Int,
        money: Double,
        costType: String?,
        location: String?,
        userId: Int
    ): Long {
        val contentValues = ContentValues()
        contentValues.put("type", type)
        contentValues.put("money", money)
        contentValues.put("costType", costType)
        contentValues.put(
            "createTime",
            date2String(Date(), TimeDateUtils.FORMAT_TYPE_3)
        )
        contentValues.put("location", location)
        contentValues.put("user_id", userId)
        val dataSize = readableDatabase.insert("t_account", null, contentValues)
                Log.e(TAG, "==============insertData======================"+location+"================"+money);
        readableDatabase.close()
        return dataSize
    }

    /**
     * 查询名字单个数据
     */
    fun selectisData(names: String): Boolean {
        //查询数据库
        val cursor = readableDatabase.query(
            "t_account",
            null,
            "name = ?",
            arrayOf(names),
            null,
            null,
            null
        )
        while (cursor.moveToNext()) {
            readableDatabase.close()
            return true
        }
        readableDatabase.close()
        return false
    }

    init {
        Log.e(TAG, "-------> MySqliteHelper")
    }
}