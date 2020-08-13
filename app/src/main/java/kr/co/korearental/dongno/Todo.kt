package kr.co.korearental.dongno

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Todo(
    @PrimaryKey var id: Long = 0,
    var cono_name: String = "",
    var cono_time: String = "",
    var cono_date: String = ""
) : RealmObject() {
}