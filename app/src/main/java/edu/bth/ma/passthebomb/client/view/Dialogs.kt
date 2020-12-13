package edu.bth.ma.passthebomb.client.view

import android.content.Context
import android.text.InputType
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import edu.bth.ma.passthebomb.client.R

class Dialogs(val context: Context) {

    fun showStringInputDialog(title: String, block: (String) -> Unit){
        val builder: AlertDialog.Builder = AlertDialog.Builder(context, R.style.Theme_PassTheBombClient_Dialog)
        builder.setTitle(title)
        val input = EditText(context)
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)
        builder.setPositiveButton("OK", { dialog, which -> block(input.text.toString()) })
        builder.show()
    }

}