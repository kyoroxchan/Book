package app.nakamura.kyoro.Sakuhinkanriapuri

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_edit.*
import java.util.*

class EditActivity : AppCompatActivity() {

    var spinneritem: String = ""
    var bookId = ""

    private val realm: Realm by lazy {
        Realm.getDefaultInstance()
    }

    private val spinnerItems = arrayOf("ファンタジー", "恋愛", "アクション", "ホラー", "ヒューマンドラマ")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        if (BuildConfig.DEBUG && supportActionBar == null) {
            error("Assertion failed")
        }
        supportActionBar!!.setDisplayHomeAsUpEnabled(true) //show back button


        val adapter = ArrayAdapter(
            applicationContext,
            android.R.layout.simple_spinner_item,
            spinnerItems
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            //　アイテムが選択された時
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?, position: Int, id: Long
            ) {
                val spinnerParent = parent as Spinner
                val item = spinnerParent.selectedItem as String
                spinneritem = item
                val imageId = getImageId(item)
                imageView2.setImageResource(imageId)
                // Kotlin Android Extensions
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        //追加ボタン
        okUpdateButton.setOnClickListener {
            val name: String = nameText.text.toString()
            val tag: String = spinneritem
            val story: String = storyText.text.toString()
            val imageId = getImageId(tag)
            update(bookId ,name, tag, story, imageId)
            finish()
        }
        val intent = getIntent()
        bookId = intent.extras?.getString("BookId") ?: ""
        var book: Book = readAll(bookId)

        nameText.setText(book.name)
        imageView2.setImageResource(book.imageId)
        val index = getTagIndex(book.tag)
        spinner.setSelection(index)
        storyText.setText(book.story)
    }


    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
    fun readAll(bookId: String): Book {
        val b: Book? = realm.where(Book::class.java).equalTo("id", bookId).findFirst()
        return b!!
    }

    fun create(name: String, tag: String, story: String, imageId: Int) {
        realm.executeTransaction {
            val book = it.createObject(Book::class.java, UUID.randomUUID().toString())
            book.name = name
            book.tag = tag
            book.story = story
            book.imageId = imageId
        }
    }

    fun update(id: String, name: String, tag: String, story: String, imageId: Int) {
        realm.executeTransaction {
            val book = readAll(id)
            book.name = name
            book.tag = tag
            book.story = story
            book.imageId = imageId
        }
    }

    fun getImageId(tag: String):Int {
        var imageId = 0

        if (tag == "ファンタジー") {
            imageId = R.drawable.fantaji
        }
        if (tag == "恋愛") {
            imageId = R.drawable.renai
        }
        if (tag == "アクション") {
            imageId = R.drawable.akusilon
        }
        if (tag == "ホラー") {
            imageId = R.drawable.pien
        }
        if (tag == "ヒューマンドラマ") {
            imageId = R.drawable.human
        }
        return imageId
    }
    fun getTagIndex(tag: String):Int {
        var imageId = 0

        if (tag == "ファンタジー") {
            imageId = 0
        }
        if (tag == "恋愛") {
            imageId = 1
        }
        if (tag == "アクション") {
            imageId = 2
        }
        if (tag == "ホラー") {
            imageId = 3
        }
        if (tag == "ヒューマンドラマ") {
            imageId = 4
        }
        return imageId
    }
}
