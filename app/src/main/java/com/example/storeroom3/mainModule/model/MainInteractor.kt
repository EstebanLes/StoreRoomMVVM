package com.example.storeroom3.mainModule.model

import com.example.storeroom3.StoreApplication
import com.example.storeroom3.common.entities.StoreEntity
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class MainInteractor {

    interface StoresCallback {
        fun getStoresCallback(stores: MutableList<StoreEntity>)
    }

    fun getStoresCallback(callback: StoresCallback) {
        doAsync {
            val storeList = StoreApplication.database.storeDao().getAllStore()
            uiThread {
                callback.getStoresCallback(storeList)
            }
        }
    }
}