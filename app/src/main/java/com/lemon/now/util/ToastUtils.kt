import android.content.Context
import android.widget.Toast

object ToastUtils {

    fun showShort(context: Context, message: String) {
        showToast(context, message, Toast.LENGTH_SHORT)
    }

    fun showLong(context: Context, message: String) {
        showToast(context, message, Toast.LENGTH_LONG)
    }

    private fun showToast(context: Context, message: String, duration: Int) {
        Toast.makeText(context, message, duration).show()
    }
}
