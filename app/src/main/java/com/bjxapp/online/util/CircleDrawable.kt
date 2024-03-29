
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape

class CircleDrawable(color: Int) : ShapeDrawable(OvalShape()) {

    private val paint = Paint()

    init {
        paint.color = color
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        canvas.drawCircle(bounds.centerX().toFloat(), bounds.centerY().toFloat(), (bounds.width() / 2).toFloat(), paint)
    }
}
