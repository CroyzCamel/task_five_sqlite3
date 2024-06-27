package com.example.task_five_sqlite3

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

class ContactDbHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "contact.db"
        const val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val SQL_CREATE_ENTRIES =
            "CREATE TABLE ${ContactContract.ContactEntry.TABLE_NAME} (" +
                    "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                    "${ContactContract.ContactEntry.COLUMN_NAME} TEXT," +
                    "${ContactContract.ContactEntry.COLUMN_PHONE} TEXT)"
        db?.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS ${ContactContract.ContactEntry.TABLE_NAME}")
        onCreate(db)
    }

    fun getAllContacts(dbHelper: ContactDbHelper): List<Contact> {
        val db = dbHelper.readableDatabase
        val projection = arrayOf(
            BaseColumns._ID,
            ContactContract.ContactEntry.COLUMN_NAME,
            ContactContract.ContactEntry.COLUMN_PHONE
        )

        val cursor = db.query(
            ContactContract.ContactEntry.TABLE_NAME,
            projection,
            null,
            null,
            null,
            null,
            null
        )

        val contacts = mutableListOf<Contact>()
        with(cursor) {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(BaseColumns._ID))
                val name =
                    getString(getColumnIndexOrThrow(ContactContract.ContactEntry.COLUMN_NAME))
                val phone =
                    getString(getColumnIndexOrThrow(ContactContract.ContactEntry.COLUMN_PHONE))
                contacts.add(Contact(id, name, phone))
            }
        }
        cursor.close()
        db.close()
        return contacts
    }

    fun addContact(dbHelper: ContactDbHelper, name: String, phone: String): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(ContactContract.ContactEntry.COLUMN_NAME, name)
            put(ContactContract.ContactEntry.COLUMN_PHONE, phone)
        }

        val id = db.insert(ContactContract.ContactEntry.TABLE_NAME, null, values)
        db.close()
        return id
    }

    fun deleteContact(dbHelper: ContactDbHelper, id: Int) {
        val db = dbHelper.writableDatabase
        db.delete(ContactContract.ContactEntry.TABLE_NAME,
            "${BaseColumns._ID} = ?",
            arrayOf(id.toString()))
        db.close()
    }
}



