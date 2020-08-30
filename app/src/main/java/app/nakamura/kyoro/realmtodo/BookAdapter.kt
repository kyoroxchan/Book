package app.nakamura.kyoro.realmtodo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter
import kotlinx.android.synthetic.main.list_item.view.*
import java.text.SimpleDateFormat
import java.util.*

class BookAdapter(
    private val context: Context,
    private var bookList: OrderedRealmCollection<Book>?,
    private var listener: OnItemClickListener,
    private val autoUpdate: Boolean
) :
    RealmRecyclerViewAdapter<Book, BookAdapter.BookViewHolder>(bookList, autoUpdate) {

    override fun getItemCount(): Int = bookList?.size ?: 0

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book: Book = bookList?.get(position) ?: return

        holder.container.setOnClickListener {
            listener.onItemClick(book)
        }

        holder.imageView.setImageResource(book.imageId)
        holder.nameTextView.text = book.name
        holder.tagTextView.text = book.tag
        holder.dateTextView.text =
            SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.JAPANESE).format(book.createdAt)

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): BookViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.list_item, viewGroup, false)
        return BookViewHolder(v)
    }

    class BookViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val container: LinearLayout = view.container
        val imageView: ImageView = view.imageView
        val nameTextView: TextView = view.nameTextView
        val tagTextView: TextView = view.tagTextView
        val dateTextView: TextView = view.dateTextView
    }

    interface OnItemClickListener {
        fun onItemClick(item: Book)
    }

}