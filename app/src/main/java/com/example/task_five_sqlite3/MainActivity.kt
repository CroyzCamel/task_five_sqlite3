package com.example.task_five_sqlite3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.task_five_sqlite3.ui.theme.Task_five_sqlite3Theme

class MainActivity : ComponentActivity() {
    private lateinit var dbHelper: ContactDbHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dbHelper = ContactDbHelper(this@MainActivity)
        enableEdgeToEdge()
        setContent {
            Task_five_sqlite3Theme {
                ContactApp(dbHelper)
            }
        }
    }
}

@Composable
fun ContactApp(dbHelper: ContactDbHelper) {
    var contacts by remember { mutableStateOf(emptyList<Contact>()) }
    var newContactName by remember { mutableStateOf("") }
    var newContactPhone by remember { mutableStateOf("") }

    contacts = dbHelper.getAllContacts(dbHelper)

    Scaffold(
        modifier = Modifier.padding(16.dp)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            TextField(
                value = newContactName,
                onValueChange = { newContactName = it },
                label = { Text(text = "Name") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = newContactPhone, onValueChange = { newContactPhone = it },
                label = { Text("Phone") },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    dbHelper.addContact(dbHelper, newContactName, newContactPhone)
                    contacts = dbHelper.getAllContacts(dbHelper)
                    newContactName = ""
                    newContactPhone = ""
                }),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    dbHelper.addContact(dbHelper, newContactName, newContactPhone)
                    contacts = dbHelper.getAllContacts(dbHelper)
                    newContactName = ""
                    newContactPhone = ""
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(text = "Add Contact")
            }
            Spacer(modifier = Modifier.height(16.dp))
            ContactList(contacts = contacts, onDeleteContact = { contactId ->
                dbHelper.deleteContact(dbHelper, contactId)
                contacts = dbHelper.getAllContacts(dbHelper)
            })
        }
    }
}

@Composable
fun ContactList(contacts: List<Contact>, onDeleteContact: (Int) -> Unit) {
    LazyColumn {
        items(contacts) { contact ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(text = contact.name)
                    Text(text = contact.phone)
                }
                IconButton(onClick = { onDeleteContact(contact.id) }) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete Contact")
                }
            }
            HorizontalDivider()
        }
    }
}
