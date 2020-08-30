package app.nakamura.kyoro.realmtodo

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class Book(
    @PrimaryKey open var id: String = UUID.randomUUID().toString(),
    open var name: String = "",
    open var tag: String = "",
    open var story: String = "",
    open var imageId: Int = 0,
    open var createdAt: Date = Date(System.currentTimeMillis())
) : RealmObject()
