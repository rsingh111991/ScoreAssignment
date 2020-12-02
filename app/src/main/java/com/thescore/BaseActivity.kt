package com.thescore

import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity: AppCompatActivity() {

    // add titlte to child classes
    fun setActionBarTitle(title: String){
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.title = title
    }

    fun unableBackButton(){
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = ""
    }
    //abstract method to show error layout and message
    abstract fun showErrorMessage(errorTitle: String)

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // handle arrow click here
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return true
    }
}