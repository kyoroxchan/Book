package app.nakamura.kyoro.realmtodo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    private val realm: Realm by lazy {
        Realm.getDefaultInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val intent = getIntent()
        val bookId = intent.extras?.getString("BookId") ?: ""
        var book: Book = readAll(bookId)

        nameTextView.text = book.name
        tagTextView.text = book.tag
        storyTextView.text = book.story

        //戻るボタン
        backButton.setOnClickListener {
            finish()
        }
    }

    fun readAll(bookId: String): Book {
        val b: Book? = realm.where(Book::class.java).equalTo("id", bookId).findFirst()
        return b!!
    }
}
