package marketwatch.com.app.marketwatch

import android.content.Context
import android.content.SharedPreferences

/**
 * Created by ramdani on 9/18/16.
 */
class AppPreference(mContext: Context) {
    private val mPreferences: SharedPreferences
    private val PREF_NAME = "MyGroupChat"
    private val KEY_EMAIL = "email"
    private val KEY_USERNAME = "username"
    private val editor: SharedPreferences.Editor
    var email: String?
        get() = mPreferences.getString(KEY_EMAIL, null)
        set(email) {
            editor.putString(KEY_EMAIL, email)
            editor.commit()
        }

    fun setusername(email: String?) {
        editor.putString(KEY_USERNAME, email)
        editor.commit()
    }

    fun getusername(): String? {
        return mPreferences.getString(KEY_USERNAME, null)
    }

    fun clear() {
        editor.clear()
        editor.commit()
    }

    init {
        mPreferences = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        editor = mPreferences.edit()
    }
}