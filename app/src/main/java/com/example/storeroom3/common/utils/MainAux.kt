package com.example.storeroom3.common.utils

import com.example.storeroom3.common.entities.StoreEntity

interface MainAux {
    fun hideFab(isVisible: Boolean = false)

    fun addStore (storeEntity: StoreEntity)
    fun updateStore (storeEntity: StoreEntity)
}