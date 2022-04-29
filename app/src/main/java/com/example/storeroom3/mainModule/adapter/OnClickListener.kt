package com.example.storeroom3.mainModule.adapter

import com.example.storeroom3.common.entities.StoreEntity

interface OnClickListener {
    fun onClick (storeId: Long)
    fun onFavoriteStore(storeEntity: StoreEntity)
    fun onDeleteStore(storeEntity: StoreEntity)
}
