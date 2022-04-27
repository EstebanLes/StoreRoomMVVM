package com.example.storeroom3

import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.storeroom3.databinding.FragmentEditStoreBinding
import com.google.android.material.snackbar.Snackbar
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class EditStoreFragment : Fragment() {

    private lateinit var mBinding: FragmentEditStoreBinding
    private var mActivity: MainActivity? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentEditStoreBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    //esta funcion se hace una vez que ya se realizo el fregment para desde aca poder manipular todos los elementos del fregment
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = arguments?.getLong(getString(R.string.arg_id),0)
        if (id != null && id != 0L){
            Toast.makeText(activity,id.toString(), Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(activity,id.toString(), Toast.LENGTH_SHORT).show()
        }

        mActivity = activity as? MainActivity
        mActivity?.supportActionBar?.setDisplayHomeAsUpEnabled(true) //para que aparezca el boton de regresar
        mActivity?.supportActionBar?.title =
            getString(R.string.edit_stor_title_add) //para que aparezca el titulo de la ActionBar

        setHasOptionsMenu(true) //para que aparezca el menu de opciones

        mBinding.etPhotoUrl.addTextChangedListener{
            Glide.with(this)
                .load(mBinding.etPhotoUrl.text.toString())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(mBinding.imgphoto)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_save, menu) //para que aparezca el menu de opciones
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                mActivity?.onBackPressed()
                true
            }
            R.id.action_save -> {
                val store = StoreEntity(
                    name = mBinding.etName.text.toString().trim(),
                    phone = mBinding.etPhone.text.toString().trim(),
                    website = mBinding.etWebsite.text.toString().trim(),
                    photoUrl = mBinding.etPhotoUrl.text.toString().trim()
                )
                doAsync {
                    store.id = StoreApplication.database.storeDao().addStore(store)
                    uiThread {
                        mActivity?.addStore(store)

                        hidekeyboard()
//este toast sirve para mostrar un mensaje en la pantalla del usuario cuando se guarda una tienda
                        Toast.makeText(mActivity,R.string.edit_store_message_save_success,Toast.LENGTH_SHORT).show()

                        mActivity?.onBackPressed() //para que regrese a la pantalla anterior y no tener que borrar todos los campos
                    }
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }   //return super.onOptionsItemSelected(item)
    }

    private fun hidekeyboard() {
        val imm = mActivity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

    override fun onDestroyView() {
        hidekeyboard()
        super.onDestroyView()
    }

    override fun onDestroy() {
        mActivity?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        mActivity?.supportActionBar?.title = getString(R.string.app_name)
        mActivity?.hideFab(true)

        setHasOptionsMenu(false)
        super.onDestroy()
    }
}