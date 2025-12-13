package com.group1.pandqapplication.shared.data.local.entity

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class LocationEntity : RealmObject {
    @PrimaryKey
    var id: String = ""
    var name: String = ""
    var address: String = ""
    var latitude: Double = 0.0
    var longitude: Double = 0.0
}
