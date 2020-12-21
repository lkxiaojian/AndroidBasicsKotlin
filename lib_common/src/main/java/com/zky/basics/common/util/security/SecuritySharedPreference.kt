package com.zky.basics.common.util.security

import android.annotation.TargetApi
import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Build
import android.preference.PreferenceManager
import android.text.TextUtils
import com.zky.basics.common.util.security.EncryptUtil.Companion.getInstance
import java.util.*

/**
 * Created by lk
 * Date 2020-01-17
 * Time 15:58
 * Detail:
 */
class SecuritySharedPreference(private val mContext: Context, name: String?, mode: Int) :
    SharedPreferences {
    private var mSharedPreferences: SharedPreferences? = null
    override fun getAll(): Map<String, String?> {
        val encryptMap = mSharedPreferences!!.all
        val decryptMap: MutableMap<String, String?> = HashMap()
        for ((key, cipherText) in encryptMap) {
            if (cipherText != null) {
                decryptMap[key] = cipherText.toString()
            }
        }
        return decryptMap
    }

    /**
     * encrypt function
     * @return cipherText base64
     */
    private fun encryptPreference(plainText: String): String? {
        return getInstance(mContext)!!.encrypt(plainText)
    }

    /**
     * decrypt function
     * @return plainText
     */
    private fun decryptPreference(cipherText: String): String? {
        return getInstance(mContext)!!.decrypt(cipherText)
    }

    override fun getString(key: String, defValue: String): String? {
        val encryptValue = mSharedPreferences!!.getString(encryptPreference(key), null)
        return encryptValue?.let { decryptPreference(it) } ?: defValue
    }

    override fun getStringSet(key: String, defValues: Set<String>): Set<String>? {
        val encryptSet = mSharedPreferences!!.getStringSet(encryptPreference(key), null)
            ?: return defValues
        val decryptSet: MutableSet<String> = HashSet()
        for (encryptValue in encryptSet) {
            decryptSet.add(decryptPreference(encryptValue)!!)
        }
        return decryptSet
    }

    override fun getInt(key: String, defValue: Int): Int {
        val encryptValue = mSharedPreferences!!.getString(encryptPreference(key), null)
            ?: return defValue
        return decryptPreference(encryptValue)!!.toInt()
    }

    override fun getLong(key: String, defValue: Long): Long {
        val encryptValue = mSharedPreferences!!.getString(encryptPreference(key), null)
            ?: return defValue
        return decryptPreference(encryptValue)!!.toLong()
    }

    override fun getFloat(key: String, defValue: Float): Float {
        val encryptValue = mSharedPreferences!!.getString(encryptPreference(key), null)
            ?: return defValue
        return decryptPreference(encryptValue)!!.toFloat()
    }

    override fun getBoolean(key: String, defValue: Boolean): Boolean {
        val encryptValue = mSharedPreferences!!.getString(encryptPreference(key), null)
            ?: return defValue
        return java.lang.Boolean.parseBoolean(decryptPreference(encryptValue))
    }

    override fun contains(key: String): Boolean {
        return mSharedPreferences!!.contains(encryptPreference(key))
    }

    override fun edit(): SecurityEditor {
        return SecurityEditor()
    }

    override fun registerOnSharedPreferenceChangeListener(listener: OnSharedPreferenceChangeListener) {
        mSharedPreferences!!.registerOnSharedPreferenceChangeListener(listener)
    }

    override fun unregisterOnSharedPreferenceChangeListener(listener: OnSharedPreferenceChangeListener) {
        mSharedPreferences!!.unregisterOnSharedPreferenceChangeListener(listener)
    }

    /**
     * 处理加密过渡
     */
    fun handleTransition() {
        val oldMap = mSharedPreferences!!.all
        val newMap: MutableMap<String?, String?> = HashMap()
        for ((key, value) in oldMap) {
            newMap[encryptPreference(key)] = encryptPreference(value.toString())
        }
        val editor = mSharedPreferences!!.edit()
        editor.clear().commit()
        for ((key, value) in newMap) {
            editor.putString(key, value)
        }
        editor.commit()
    }

    /**
     * 自动加密Editor
     */
    inner class SecurityEditor : SharedPreferences.Editor {
        private val mEditor: SharedPreferences.Editor
        override fun putString(key: String, value: String): SharedPreferences.Editor {
            mEditor.putString(encryptPreference(key), encryptPreference(value))
            return this
        }

        override fun putStringSet(key: String, values: Set<String>): SharedPreferences.Editor {
            val encryptSet: MutableSet<String?> = HashSet()
            for (value in values) {
                encryptSet.add(encryptPreference(value))
            }
            mEditor.putStringSet(encryptPreference(key), encryptSet)
            return this
        }

        override fun putInt(key: String, value: Int): SharedPreferences.Editor {
            mEditor.putString(encryptPreference(key), encryptPreference(Integer.toString(value)))
            return this
        }

        override fun putLong(key: String, value: Long): SharedPreferences.Editor {
            mEditor.putString(
                encryptPreference(key),
                encryptPreference(java.lang.Long.toString(value))
            )
            return this
        }

        override fun putFloat(key: String, value: Float): SharedPreferences.Editor {
            mEditor.putString(
                encryptPreference(key),
                encryptPreference(java.lang.Float.toString(value))
            )
            return this
        }

        override fun putBoolean(key: String, value: Boolean): SharedPreferences.Editor {
            mEditor.putString(
                encryptPreference(key),
                encryptPreference(java.lang.Boolean.toString(value))
            )
            return this
        }

        override fun remove(key: String): SharedPreferences.Editor {
            mEditor.remove(encryptPreference(key))
            return this
        }

        /**
         * Mark in the editor to remove all values from the preferences.
         * @return this
         */
        override fun clear(): SharedPreferences.Editor {
            mEditor.clear()
            return this
        }

        /**
         * 提交数据到本地
         * @return Boolean 判断是否提交成功
         */
        override fun commit(): Boolean {
            return mEditor.commit()
        }

        /**
         * Unlike commit(), which writes its preferences out to persistent storage synchronously,
         * apply() commits its changes to the in-memory SharedPreferences immediately but starts
         * an asynchronous commit to disk and you won't be notified of any failures.
         */
        @TargetApi(Build.VERSION_CODES.GINGERBREAD)
        override fun apply() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                mEditor.apply()
            } else {
                commit()
            }
        }

        /**
         * constructor
         */
        init {
            mEditor = mSharedPreferences!!.edit()
        }
    }

    companion object {
        private val TAG = SecuritySharedPreference::class.java.name
    }

    /**
     * constructor
     * @param context should be ApplicationContext not activity
     * @param name file name
     * @param mode context mode
     */
    init {
        mSharedPreferences = if (TextUtils.isEmpty(name)) {
            PreferenceManager.getDefaultSharedPreferences(mContext)
        } else {
            mContext.getSharedPreferences(name, mode)
        }
    }
}