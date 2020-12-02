package com.thescore

import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity: AppCompatActivity() {

    // add titlte to child classes
    fun setActionBarTitle(title: String){
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.title = title
    }

    //abstract method to show error layout and message
    abstract fun showErrorMessage(errorTitle: String)
}