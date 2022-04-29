package com.example.storeroom3.mainModule.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storeroom3.common.entities.StoreEntity
import com.example.storeroom3.mainModule.model.MainInteractor


class MainViewModel: ViewModel() {
    private var stores: MutableLiveData<List<StoreEntity>>
    private var interactor: MainInteractor

    init {
        interactor = MainInteractor()
        stores = MutableLiveData()
        loadStores()
    }

    fun getStores(): LiveData<List<StoreEntity>> {
        return stores
    }

    private fun loadStores() {
        interactor.getStoresCallback(object: MainInteractor.StoresCallback {
            override fun getStoresCallback(stores: MutableList<StoreEntity>) {
                this@MainViewModel.stores.value = stores
            }
        })
    }
}