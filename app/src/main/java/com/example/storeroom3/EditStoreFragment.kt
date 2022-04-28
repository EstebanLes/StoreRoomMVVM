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
    private var mActivity: MainActivity? =
        null//esta variable es para poder acceder a los metodos de la actividad
    private var mIsEditMode: Boolean =
        false// esta variable nos indica si estamos en modo edición o no
    private var mStoreEntity: StoreEntity? =
        null// esta variable indica la tienda que estamos editando

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle? ): View {
        mBinding = FragmentEditStoreBinding.inflate(inflater, container, false)

        return mBinding.root
    }

    //esta funcion se hace una vez que ya se realizo el fregment para desde aca poder manipular todos los elementos del fregment
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = arguments?.getLong(getString(R.string.arg_id), 0)
        if (id != null && id != 0L) {
            mIsEditMode = true
            getStore(id)
        } else {
            mIsEditMode = false
            mStoreEntity = StoreEntity(name = "", phone = "", photoUrl = "")
        }

        mActivity = activity as? MainActivity
        mActivity?.supportActionBar?.setDisplayHomeAsUpEnabled(true) //para que aparezca el boton de regresar
        mActivity?.supportActionBar?.title =
            getString(R.string.edit_stor_title_add) //para que aparezca el titulo de la ActionBar

        setHasOptionsMenu(true) //para que aparezca el menu de opciones

        mBinding.etPhotoUrl.addTextChangedListener {
            Glide.with(this)
                .load(mBinding.etPhotoUrl.text.toString())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(mBinding.imgphoto)
        }
    }

    //esta funcion ejecuta una consulta a la base de datos para obtener los datos de la tienda
    private fun getStore(id: Long) {
        doAsync {
            mStoreEntity = StoreApplication.database.storeDao().getStoreById(id)
            uiThread { if (mStoreEntity != null) setUiStore(mStoreEntity!!) }
        }
    }

    private fun setUiStore(storeEntity: StoreEntity) {
        with(mBinding) {
            etName.setText(storeEntity.name)
            etPhone.setText(mStoreEntity?.phone ?: "")
            etWebsite.setText(mStoreEntity?.website ?: "")
            etPhotoUrl.setText(mStoreEntity?.photoUrl ?: "")
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
                if (mStoreEntity != null && validateFields()) {
                    with(mStoreEntity!!) {
                        name = mBinding.etName.text.toString().trim()
                        phone = mBinding.etPhone.text.toString().trim()
                        website = mBinding.etWebsite.text.toString().trim()
                        photoUrl = mBinding.etPhotoUrl.text.toString().trim()
                    }

                    doAsync {
                        if (mIsEditMode)StoreApplication.database.storeDao().updateStore(mStoreEntity!!)
                        else mStoreEntity?.id = StoreApplication.database.storeDao().addStore(mStoreEntity!!)

                        uiThread {

                            hidekeyboard()//para que se oculte el teclado

                            if (mIsEditMode){
                                mActivity?.updateStore(mStoreEntity!!)

                                Snackbar.make( mBinding.root,
                                    getString(R.string.edit_stor_msg_update_succsess),
                                    Snackbar.LENGTH_SHORT).show()

                            }else {
                                mActivity?.addStore( mStoreEntity!!)
//este toast sirve para mostrar un mensaje en la pantalla del usuario cuando se guarda una tienda
                                Toast.makeText(mActivity, R.string.edit_store_message_save_success, Toast.LENGTH_SHORT).show()

                                mActivity?.onBackPressed() //para que regrese a la pantalla anterior y no tener que borrar todos los campos
                            }
                        }
                    }
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
//funcion de validacion de los campos de la pantalla de edicion de tienda
    private fun validateFields(): Boolean {
        var isValid = true
            if (mBinding.etPhotoUrl.text.toString().trim().isEmpty()) {
                mBinding.tilPhotoUrl.error = getString(R.string.helper_required)
                mBinding.etPhotoUrl.requestFocus()//esto es para que haga el foco en este campo
                isValid = false
            }
            if (mBinding.etPhone.text.toString().trim().isEmpty()) {
                mBinding.tilPhone.error = getString(R.string.helper_required)
                mBinding.etPhotoUrl.requestFocus()//esto es para que haga el foco en este campo
                isValid = false
            }
            if (mBinding.etName.text.toString().trim().isEmpty()) {
                mBinding.tilName.error = getString(R.string.helper_required)
                mBinding.etPhotoUrl.requestFocus()//esto es para que haga el foco en este campo
                isValid = false
            }

        return isValid
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