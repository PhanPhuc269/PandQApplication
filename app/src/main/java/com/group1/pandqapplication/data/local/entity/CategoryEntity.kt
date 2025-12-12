package com.group1.pandqapplication.data.local.entity

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class CategoryEntity : RealmObject {
    @PrimaryKey
    var id: String = ""
    var name: String = ""
    var iconUrl: String = ""
}
