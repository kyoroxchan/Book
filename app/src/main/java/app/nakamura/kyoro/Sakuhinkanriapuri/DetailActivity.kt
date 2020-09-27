package app.nakamura.kyoro.Sakuhinkanriapuri

import android.app.AlertDialog
import android.app.PendingIntent.getActivity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_detail.*


class DetailActivity : AppCompatActivity() {

    private val realm: Realm by lazy {
        Realm.getDefaultInstance()
    }

    private var bookId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        if (BuildConfig.DEBUG && supportActionBar == null) {
            error("Assertion failed")
        }
        supportActionBar!!.setDisplayHomeAsUpEnabled(true) //show back button


        val intent = getIntent()
        bookId = intent.extras?.getString("BookId") ?: ""
        val book: Book = readAll(bookId)

        nameTextView.text = book.name
        tagTextView.text = book.tag
        storyTextView.text = book.story
        imageView2.setImageResource(book.imageId)

        //戻るボタン
//        backButton.setOnClickListener {
//            finish()
//        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            R.id.updateButton -> {

                val intent = Intent(applicationContext, EditActivity::class.java)
                intent.putExtra("BookId", bookId)
                startActivity(intent)

                true
            }
            R.id.deleteButton -> {
                AlertDialog.Builder(this)
                    .setTitle("削除しますか?")
                    .setMessage("※削除した場合元に戻りません")
                    .setPositiveButton("削除", DialogInterface.OnClickListener { dialog, which ->
                        // OK button pressed
                        delete(bookId)
                        finish()
                    })
                    .setNegativeButton("Cancel", null)
                    .show()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }


    fun readAll(bookId: String): Book {
        val b: Book? = realm.where(Book::class.java).equalTo("id", bookId).findFirst()
        return b!!
    }
    fun delete(id: String) {
        realm.executeTransaction {
            val book =
                realm.where(Book::class.java).equalTo("id", id).findFirst()
                    ?: return@executeTransaction
            book.deleteFromRealm()
        }
    }
}
