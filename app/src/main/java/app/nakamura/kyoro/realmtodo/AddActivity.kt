package app.nakamura.kyoro.realmtodo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_add.*
import kotlinx.android.synthetic.main.activity_add.view.*
import kotlinx.android.synthetic.main.list_item.*
import java.util.*

class AddActivity : AppCompatActivity() {

    var spinneritem: String = ""

    private val realm: Realm by lazy {
        Realm.getDefaultInstance()
    }

    private val spinnerItems = arrayOf("ファンタジー", "恋愛", "アクション", "ホラー", "ヒューマンドラマ")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

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
        //戻るボタン
        backButton.setOnClickListener {
            finish()
        }

        //追加ボタン
        addButton.setOnClickListener {
            val name: String = nameText.text.toString()
            val tag: String = spinneritem
            val story: String = storyText.text.toString()
            val imageId = getImageId(tag)
            create(name, tag, story, imageId)
            finish()
        }
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
}
