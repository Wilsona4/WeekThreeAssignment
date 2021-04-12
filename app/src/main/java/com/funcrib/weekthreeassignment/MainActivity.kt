package com.funcrib.weekthreeassignment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

const val SELECTED_CONTACT_ID = 1

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*Get Contact from Phone Contacts*/
        btContacts.setOnClickListener {
            Toast.makeText(this, "Getting Contact", Toast.LENGTH_SHORT).show()
            /*Create intent and choose action*/
            val getContactIntent = Intent().apply {
                action = Intent.ACTION_PICK
                type = ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE
            }
            /*Implementing startActivity for result to retrieve contact*/
            if (getContactIntent.resolveActivity(packageManager) != null) {
                startActivityForResult(getContactIntent, SELECTED_CONTACT_ID)
            }
        }

        btDial.setOnClickListener {
            Toast.makeText(this, "Dialing ${textView.text}", Toast.LENGTH_SHORT).show()

            /*Create intent, choose action and provide date*/
            val dialIntent = Intent().apply {
                action = Intent.ACTION_DIAL
                data = Uri.parse("tel:${textView.text}")
            }

            /*Verify that the intent will resolve to an activity*/
            if (dialIntent.resolveActivity(packageManager) != null) {
                startActivity(dialIntent)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SELECTED_CONTACT_ID && resultCode == Activity.RESULT_OK) {
//            Create a contact with data received
            val contactUri = data!!.data
            val projection: Array<String> = arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER)
            if (contactUri != null) {
                contentResolver.query(contactUri, projection, null, null, null).use { cursor ->
                    // If the cursor returned is valid, get the phone number
                    if (cursor != null) {
                        if (cursor.moveToFirst()) {
                            val numberIndex =
                                cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                            val phoneNumber: String = cursor.getString(numberIndex)
                            // Set PhoneNumber to TextView
                            textView.text = phoneNumber
                        }
                    }
                }
            }
        }
    }
    /*Killing the process every time onDestroy is called*/
    override fun onDestroy() {
        super.onDestroy()
        finish()
    }
}