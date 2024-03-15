package com.chavan.automessagereplier.presentation.custom_message.components


import android.content.ContentResolver
import android.content.Intent
import android.provider.ContactsContract
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun MultipleContactPicker(
    onContactsPicked: (List<String>) -> Unit
) {
    val context = LocalContext.current
    val getContent = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            val contacts = result.data?.data?.let { uri ->
                getContactsFromUri(context.contentResolver, uri)
            }
            contacts?.let { onContactsPicked(it) }
        }
    }

    IconButton(onClick = {
        val intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        getContent.launch(intent)
    }) {
        Icon(
            imageVector = Icons.Default.Contacts,
            contentDescription = "Contacts"
        )
    }
}

fun getContactsFromUri(contentResolver: ContentResolver, uri: android.net.Uri): List<String> {
    val contacts = mutableListOf<String>()
    contentResolver.query(uri, null, null, null, null)?.use { cursor ->
        val displayNameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
        if (displayNameIndex != -1) {
            while (cursor.moveToNext()) {
                val name = cursor.getString(displayNameIndex)
                contacts.add(name)
            }
        }
    }
    return contacts
}