package app.nakamura.kyoro.realmtodo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import io.realm.Realm
import io.realm.RealmResults
import io.realm.Sort
import kotlinx.android.synthetic.main.activity_main.*
import java.nio.file.Files.delete
import java.util.*
import android.content.Intent
import android.view.View

class MainActivity : AppCompatActivity() {

    private val realm: Realm by lazy {
        Realm.getDefaultInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //画面遷移ボタンタップ時
        onButton.setOnClickListener {
            val intent = Intent(this, AddActivity::class.java)
            startActivity(intent)
        }

        val bookList = readAll()

        // タスクリストが空だったときにダミーデータを生成する

        val adapter =
            BookAdapter(this, bookList, object : BookAdapter.OnItemClickListener {
                override fun onItemClick(item: Book) {
                    // クリック時の処理
                    val intent = Intent(applicationContext, DetailActivity::class.java)
                    intent.putExtra("BookId", item.id);
                    startActivity(intent)
                }
            }, true)

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

    fun createDummyData() {
        for (i in 0..10) {
            create("名前", "タグ","あらすじ")
        }
    }

    fun create(name: String, tag: String, story: String) {
        realm.executeTransaction {
            val book = it.createObject(Book::class.java, UUID.randomUUID().toString())
            book.name = name
            book.tag = tag
            book.story = story
        }
    }

    fun readAll(): RealmResults<Book> {
        return realm.where(Book::class.java).findAll().sort("createdAt", Sort.ASCENDING)
    }
    fun update(id: String, name: String) {
        realm.executeTransaction {
            val book = realm.where(Book::class.java).equalTo("id", id).findFirst()
                ?: return@executeTransaction
            book.name = name
        }
    }

    fun update(book: Book, name: String) {
        realm.executeTransaction {
            book.name = name
        }
    }

    fun delete(id: String) {
        realm.executeTransaction {
            val book = realm.where(Book::class.java).equalTo("id", id).findFirst()
                ?: return@executeTransaction
            book.deleteFromRealm()
        }
    }

    fun delete(book: Book) {
        realm.executeTransaction {
            book.deleteFromRealm()
        }
    }

    fun deleteAll() {
        realm.executeTransaction {
            realm.deleteAll()
        }
    }

}
