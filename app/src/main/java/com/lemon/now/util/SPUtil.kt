import android.content.Context
import android.content.SharedPreferences
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class SPUtil<T>(private val name: String, private val default: T) :
    ReadWriteProperty<Any?, T> {

    companion object {
        lateinit var preference: SharedPreferences

        fun setContext(context: Context) {
            preference = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
        }

        fun clearPreference() = preference.edit().clear().apply()

        fun clearPreference(key: String) = preference.edit().remove(key).commit()

        fun clearalle() = preference.edit().clear().commit()
    }


    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        return putPreference(name, value)
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return getPreference(name, default)
    }

    private fun getPreference(name: String, default: T): T = with(preference) {
        val res: Any = when (default) {
            is Long -> getLong(name, default)
            is String -> this.getString(name, default)!!
            is Int -> getInt(name, default)
            is Boolean -> getBoolean(name, default)
            is Float -> getFloat(name, default)
            else -> throw IllegalArgumentException("This type can be get from Preferences")
//            else -> deSerialization(getString(name,serialize(default)).toString())
        }
        return res as T
    }

    private fun putPreference(name: String, value: T) = with(preference.edit()) {
        when (value) {
            is Long -> putLong(name, value)
            is Int -> putInt(name, value)
            is String -> putString(name, value)
            is Boolean -> putBoolean(name, value)
            is Float -> putFloat(name, value)
            else -> throw IllegalArgumentException("This type can be saved into Preferences")
//            else -> putString(name,serialize(value))
        }.apply()
    }

    @Throws(IOException::class)
    private fun <T> serialize(obj: T): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        val objectOutputStream = ObjectOutputStream(
            byteArrayOutputStream
        )
        objectOutputStream.writeObject(obj)
        var serStr = byteArrayOutputStream.toString("ISO-8859-1")
        serStr = java.net.URLEncoder.encode(serStr, "UTF-8")
        objectOutputStream.close()
        byteArrayOutputStream.close()
        return serStr
    }

    @Throws(IOException::class, ClassNotFoundException::class)
    private fun <T> deSerialization(str: String): T {
        val redStr = java.net.URLDecoder.decode(str, "UTF-8")
        val byteArrayInputStream = ByteArrayInputStream(
            redStr.toByteArray(charset("ISO-8859-1"))
        )
        val objectInputStream = ObjectInputStream(
            byteArrayInputStream
        )
        val obj = objectInputStream.readObject() as T
        objectInputStream.close()
        byteArrayInputStream.close()
        return obj
    }
}
