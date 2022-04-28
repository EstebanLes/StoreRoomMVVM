package com.example.storeroom3

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "StoreEntity")
data class StoreEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var name: String,
    var phone: String,
    var website: String = "",
    var photoUrl: String,
    var isFavorite: Boolean = false){

    // sistema de programacion orientado a objetos (POO) el cual hace que los objetos puedan ser comparados
    // con otros objetos de la misma clase o de otra clase que tengan el mismo tipo de datos
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as StoreEntity

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}



