package com.example.storeroom3.mainModule.model

import com.example.storeroom3.StoreApplication
import com.example.storeroom3.common.entities.StoreEntity
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class MainInteractor {

    interface StoresCallback {
        fun getStoresCallback(stores: MutableList<StoreEntity>)
    }

    //esta funcion utiliza unit para que no devuelva nada y no tenga que pasarle nada
    fun getStores(callback: (MutableList<StoreEntity>) -> Unit) {
        doAsync {
            val storeList = StoreApplication.database.storeDao().getAllStore()
            uiThread {
                callback(storeList)
            }
        }
    }

    fun deleteStore(storeEntity: StoreEntity, callback: (StoreEntity) -> Unit) {
        doAsync {
            StoreApplication.database.storeDao().deleteStore(storeEntity)
            uiThread {
                callback(storeEntity)
            }
        }
    }

    fun updateStore(storeEntity: StoreEntity, callback: (StoreEntity) -> Unit) {
        doAsync {
            StoreApplication.database.storeDao().updateStore(storeEntity)
            uiThread {
                callback(storeEntity)
            }
        }
    }

}