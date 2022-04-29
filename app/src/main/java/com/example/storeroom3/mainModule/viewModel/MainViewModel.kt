package com.example.storeroom3.mainModule.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storeroom3.common.entities.StoreEntity
import com.example.storeroom3.mainModule.model.MainInteractor


class MainViewModel : ViewModel() {

    private var storesList: MutableList<StoreEntity> = mutableListOf()
    private var interactor: MainInteractor = MainInteractor()

    private val stores: MutableLiveData<List<StoreEntity>> by lazy {
        MutableLiveData<List<StoreEntity>>().also {
            loadStores()
        }
    }

    fun getStores(): LiveData<List<StoreEntity>> {
        return stores
    }

    private fun loadStores() {
//        interactor.getStoresCallback(object: MainInteractor.StoresCallback {
//            override fun getStoresCallback(stores: MutableList<StoreEntity>) {
//                this@MainViewModel.stores.value = stores
//            }
//        })
        // funcion de la interfaz de interactor para obtener los datos de la base de datos
        interactor.getStores {
            stores.value = it
        }
    }

    fun deleteStore(storeEntity: StoreEntity) {
        interactor.deleteStore(storeEntity) {
            val index = storesList.indexOf(storeEntity)
            if (index != -1) {
                storesList.removeAt(index)
                stores.value = storesList
            }
        }

    }fun updateStore(storeEntity: StoreEntity) {
        storeEntity.isFavorite = !storeEntity.isFavorite
        interactor.updateStore(storeEntity) {
            val index = storesList.indexOf(storeEntity)
            if (index != -1) {
                storesList.set(index, storeEntity)
                stores.value = storesList
            }
        }
    }
}