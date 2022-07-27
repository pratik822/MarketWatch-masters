package marketwatch.com.app.marketwatch

import android.content.Context
import java.util.*

/**
 * Created by Pratik on 13-03-2018.
 */
class TimeAgo(protected var context: Context) {
    fun timeAgo(date: Date): String {
        return timeAgo(date.time)
    }

    fun timeAgo(millis: Long): String {
        val diff = Date().time - millis
        val r = context.resources
        val prefix = r.getString(R.string.time_ago_prefix)
        val suffix = r.getString(R.string.time_ago_suffix)
        val seconds = (Math.abs(diff) / 1000).toDouble()
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24
        val years = days / 365
        val words: String
        words = if (seconds < 45) {
            r.getString(R.string.time_ago_seconds, Math.round(seconds))
        } else if (seconds < 90) {
            r.getString(R.string.time_ago_minute, 1)
        } else if (minutes < 45) {
            r.getString(R.string.time_ago_minutes, Math.round(minutes))
        } else if (minutes < 90) {
            r.getString(R.string.time_ago_hour, 1)
        } else if (hours < 24) {
            r.getString(R.string.time_ago_hours, Math.round(hours))
        } else if (hours < 42) {
            r.getString(R.string.time_ago_day, 1)
        } else if (days < 30) {
            r.getString(R.string.time_ago_days, Math.round(days))
        } else if (days < 45) {
            r.getString(R.string.time_ago_month, 1)
        } else if (days < 365) {
            r.getString(R.string.time_ago_months, Math.round(days / 30))
        } else if (years < 1.5) {
            r.getString(R.string.time_ago_year, 1)
        } else {
            r.getString(R.string.time_ago_years, Math.round(years))
        }
        val sb = StringBuilder()
        if (prefix != null && prefix.length > 0) {
            sb.append(prefix).append(" ")
        }
        sb.append(words)
        if (suffix != null && suffix.length > 0) {
            sb.append(" ").append(suffix)
        }
        return sb.toString().trim { it <= ' ' }
    }
}