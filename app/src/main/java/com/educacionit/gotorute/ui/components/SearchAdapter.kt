import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.educacionit.gotorute.R
import com.educacionit.gotorute.home.model.maps.Place

class SearchAdapter(private val context: Context) :
    RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    private var items: List<Place> = ArrayList()
    private var onPlaceClickListener: OnPlaceClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_search_result, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setItems(data: List<Place>) {
        items = data
        notifyDataSetChanged()
    }

    fun setOnPlaceClickListener(listener: OnPlaceClickListener) {
        this.onPlaceClickListener = listener
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewResult: TextView = itemView.findViewById(R.id.textViewResult)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onPlaceClickListener?.onPlaceClick(items[position])
                }
            }
        }

        fun bind(item: Place) {
            textViewResult.text = item.displayName
        }
    }

    interface OnPlaceClickListener {
        fun onPlaceClick(destinationPlace: Place)
    }
}
