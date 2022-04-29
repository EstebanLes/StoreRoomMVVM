package com.example.storeroom3.mainModule.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.storeroom3.R
import com.example.storeroom3.common.entities.StoreEntity
import com.example.storeroom3.databinding.ItemStoreBinding

class StoreAdapter(
    private var stores: MutableList<StoreEntity>,
    private var listener: OnClickListener
): RecyclerView.Adapter<StoreAdapter.ViewHolder>() {

    private lateinit var mContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext = parent.context

        val view = LayoutInflater.from(mContext).inflate(R.layout.item_store,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val store = stores[position]

        with(holder){
            setlistener(store)

            binding.tvName.text = store.name
            binding.chbFavorite.isChecked = store.isFavorite

            Glide.with(mContext)
                .load(store.photoUrl)//esto es para cargar la imagen de la url que le pasamos
                .diskCacheStrategy(DiskCacheStrategy.ALL)//esto es para que se guarde en cache la imagen
                .centerCrop()// esto es para que el tamaño de la imagen sea el mismo
                .into(binding.imgphoto)//esto es para que se muestre la imagen
        }
    }

    override fun getItemCount(): Int = stores.size

    fun setStore(stores: MutableList<StoreEntity>) {
        this.stores = stores
        notifyDataSetChanged()
    }

    fun add(storeEntity: StoreEntity) {
        if (!stores.contains(storeEntity)) {
            stores.add(storeEntity)
            notifyItemInserted(stores.size - 1)
        }
    }

    fun update(storeEntity: StoreEntity) {
        val index = stores.indexOf(storeEntity)
        if (index != -1) {
            stores[index] = storeEntity
            notifyItemChanged(index)
        }
    }

    fun delete(storeEntity: StoreEntity) {

    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemStoreBinding.bind(view)

        fun setlistener (storeEntity : StoreEntity){
            with(binding.root) {
                setOnClickListener { listener.onClick(storeEntity.id) }
                setOnLongClickListener {
                    listener.onDeleteStore(storeEntity)
                    true
                }
            }

            binding.chbFavorite.setOnClickListener {
                listener.onFavoriteStore(storeEntity)
            }
        }
    }
}

