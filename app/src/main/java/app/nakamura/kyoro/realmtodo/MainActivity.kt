package app.nakamura.kyoro.realmtodo

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import io.realm.Realm
import io.realm.RealmResults
import io.realm.Sort
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


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

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            R.id.addButton -> {

                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
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

    fun readAll(): RealmResults<Book> {
        return realm.where(Book::class.java).findAll().sort("createdAt", Sort.DESCENDING)
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
