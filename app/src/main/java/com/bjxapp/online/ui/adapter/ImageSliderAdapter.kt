
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lemon.now.online.R

class ImageSliderAdapter(private val images: List<Int>,private val postions: List<Int>,private val titles: List<Int>,private val onLastItemBoundListener: OnLastItemBoundListener) :
    RecyclerView.Adapter<ImageSliderAdapter.ImageSliderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageSliderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_image_slider, parent, false)
        return ImageSliderViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageSliderViewHolder, position: Int) {
        val imageRes = images[position]
        val postion = postions[position]
        val title = titles[position]
        holder.imageView.setBackgroundResource(imageRes)
        holder.postion.setBackgroundResource(postion)
        holder.title.setText(title)
        if(position==2){
            holder.next.text = "Get Started"
        }
        holder.next.setOnClickListener { onLastItemBoundListener.onLastItemBound() }

    }
    interface OnLastItemBoundListener {
        fun onLastItemBound()
    }
    override fun getItemCount(): Int {
        return images.size
    }

    inner class ImageSliderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.splash)
        val title: TextView = itemView.findViewById(R.id.title)
        val postion: ImageView = itemView.findViewById(R.id.postion)
        val next: TextView = itemView.findViewById(R.id.next)
    }
}
