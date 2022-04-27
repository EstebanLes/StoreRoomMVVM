package com.example.storeroom3

interface OnClickListener {
    fun onClick (storeId: Long)
    fun onFavoriteStore(storeEntity: StoreEntity)
    fun onDeleteStore(storeEntity: StoreEntity)
}
