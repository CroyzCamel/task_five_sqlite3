package com.example.task_five_sqlite3

import android.provider.BaseColumns

object ContactContract {
    object ContactEntry: BaseColumns {
        const val TABLE_NAME = "contacts"
        const val COLUMN_NAME = "name"
        const val COLUMN_PHONE = "phone"
    }
}

