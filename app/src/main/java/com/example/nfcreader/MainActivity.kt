package com.example.nfcreader

import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.nfcreader.databinding.ActivityBinder
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), CompoundButton.OnCheckedChangeListener, NfcAdapter.ReaderCallback {

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    private var binder : ActivityBinder? = null
    private val viewModel : MainViewModel by lazy { ViewModelProvider(this)[MainViewModel::class.java] }

    override fun onCreate(savedInstanceState : Bundle?) {
        binder = DataBindingUtil.setContentView(this@MainActivity, R.layout.activity_main)
        binder?.viewModel = viewModel
        binder?.lifecycleOwner = this@MainActivity
        super.onCreate(savedInstanceState)
        binder?.toggleButton?.setOnCheckedChangeListener(this@MainActivity)
        Coroutines.main(this@MainActivity) { scope ->
            scope.launch(block = {
                binder?.viewModel?.observeNFCStatus()?.collectLatest(action = { status ->
                    Log.d(TAG, "observeNFCStatus $status")
                    if (status == NFCStatus.NoOperation) NFCManager.disableReaderMode(
                        this@MainActivity,
                        this@MainActivity
                    )
                    else if (status == NFCStatus.Tap) NFCManager.enableReaderMode(
                        this@MainActivity,
                        this@MainActivity,
                        this@MainActivity,
                        viewModel.getNFCFlags(),
                        viewModel.getExtras()
                    )
                })
            })
            scope.launch(block = {
                binder?.viewModel?.observeToast()?.collectLatest(action = { message ->
                    Log.d(TAG, "observeToast $message")
                    Toast.makeText(this@MainActivity, message, Toast.LENGTH_LONG).show()
                })
            })
            scope.launch(block = {
                binder?.viewModel?.observeTag()?.collectLatest(action = { tag ->
                    Log.d(TAG, "observeTag $tag")
                    binder?.textViewExplanation?.text = tag
                })
            })
        }
    }

    override fun onCheckedChanged(buttonView : CompoundButton?, isChecked : Boolean) {
        if (buttonView == binder?.toggleButton)
            viewModel.onCheckNFC(isChecked)
    }

    override fun onTagDiscovered(tag : Tag?) {
        binder?.viewModel?.readTag(tag)
    }

    /*private fun launchMainFragment() {
        if (getSupportFragmentManager().findFragmentByTag(MainFragment::class.java.getSimpleName()) == null)
            getSupportFragmentManager().beginTransaction()
                .add(R.id.frame_layout, MainFragment.newInstance(), MainFragment::class.java.getSimpleName())
                .addToBackStack(MainFragment::class.java.getSimpleName())
                .commit()
    }*/
}