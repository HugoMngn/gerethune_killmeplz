package com.example.gerethune_killmeplz

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "gerethune.db"
        private const val TABLE_NAME = "userInput"
        private const val COLUMN_ID = "id"
        private const val COLUMN_INPUTDATE = "inputDate"
        private const val COLUMN_INPUTMONTANT = "inputFloat"
        private const val COLUMN_INPUTCATEGORY = "inputCategory"
        private const val COLUMN_INPUTDESCRIPTION = "inputDescription"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = ("CREATE TABLE $TABLE_NAME ("
                + "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "$COLUMN_INPUTDATE TEXT, "
                + "$COLUMN_INPUTMONTANT REAL, "
                + "$COLUMN_INPUTCATEGORY TEXT, "
                + "$COLUMN_INPUTDESCRIPTION TEXT)")
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertInput(inputFloat: Float, inputDate: Date, inputCategory: String, inputDescription: String): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()

        // Format the date to store it as a string
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val dateString = dateFormat.format(inputDate)

        // Put each value in the corresponding column
        contentValues.put(COLUMN_INPUTDATE, dateString)
        contentValues.put(COLUMN_INPUTMONTANT, inputFloat)
        contentValues.put(COLUMN_INPUTCATEGORY, inputCategory)
        contentValues.put(COLUMN_INPUTDESCRIPTION, inputDescription)

        // Insert the row into the table
        val result = db.insert(TABLE_NAME, null, contentValues)
        return result != -1L
    }

    fun getAllInputs(): List<String> {
        val inputList = mutableListOf<String>()
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                val inputDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_INPUTDATE))
                val inputMontant = cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_INPUTMONTANT))
                val inputCategory = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_INPUTCATEGORY))
                val inputDescription = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_INPUTDESCRIPTION))

                // Combine the fields into a readable string format for display
                val inputText = "Date: $inputDate, Amount: $inputMontant, Category: $inputCategory, Description: $inputDescription"
                inputList.add(inputText)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return inputList
    }
}
