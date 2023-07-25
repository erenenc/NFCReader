package com.example.nfcreader

import android.app.Activity
import android.content.Context
import android.nfc.NfcAdapter
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi

object NFCManager {

    private val TAG = NFCManager::class.java.simpleName

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun enableReaderMode(context : Context, activity : Activity, callback : NfcAdapter.ReaderCallback, flags : Int, extras : Bundle) {
        try {
            NfcAdapter.getDefaultAdapter(context).enableReaderMode(activity, callback, flags, extras)
        } catch (ex : UnsupportedOperationException) { Log.e(TAG,"UnsupportedOperationException ${ex.message}", ex)

        }
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun disableReaderMode(context : Context, activity : Activity) {
        try {
            NfcAdapter.getDefaultAdapter(context).disableReaderMode(activity)
        } catch (ex : UnsupportedOperationException) { Log.e(TAG,"UnsupportedOperationException ${ex.message}", ex)

        }
    }

    fun isSupported(context : Context) : Boolean {
        val nfcAdapter = NfcAdapter.getDefaultAdapter(context)
        return nfcAdapter != null
    }

    fun isNotSupported(context : Context) : Boolean {
        val nfcAdapter = NfcAdapter.getDefaultAdapter(context)
        return nfcAdapter == null
    }

    fun isEnabled(context : Context) : Boolean {
        val nfcAdapter = NfcAdapter.getDefaultAdapter(context)
        return nfcAdapter?.isEnabled ?: false
    }

    fun isNotEnabled(context : Context) : Boolean {
        val nfcAdapter = NfcAdapter.getDefaultAdapter(context)
        return nfcAdapter?.isEnabled?.not() ?: true
    }

    fun isSupportedAndEnabled(context : Context) : Boolean {
        return isSupported(context) && isEnabled(context)
    }
}