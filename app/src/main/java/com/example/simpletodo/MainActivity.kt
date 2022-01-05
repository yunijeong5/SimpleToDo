package com.example.simpletodo

import android.media.metrics.Event
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.EventLog
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.apache.commons.io.FileUtils
import org.w3c.dom.Text
import java.io.File
import java.io.IOError
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    var listOfTasks = mutableListOf<String>()
    lateinit var adapter: TaskItemAdapter

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    fun onComposeAction(mi: MenuItem) {
        var builder = AlertDialog.Builder(this)

        // builder.setCancelable(true)
        builder.setTitle("Use Guide")
        builder.setMessage("Long press each task to delete.")
        builder.setPositiveButton("Ok") {
            dialog, id ->
            dialog.cancel()
        }
        var info = builder.create()
        info.show()
    }

    fun deleteEvent(position: Int) {
        var builder = AlertDialog.Builder(this)
        builder.setTitle("Confirm Delete")
        builder.setMessage("Do you want to delete this task?")
        builder.setPositiveButton("Yes") { dialog, id ->
            listOfTasks.removeAt(position)
            adapter.notifyDataSetChanged()
            saveItems()
            dialog.cancel()
        }
        builder.setNegativeButton("No"
        ) { dialog, id ->
            dialog.cancel()
        }
        var alert = builder.create()
        alert.show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val onLongClickListener = object : TaskItemAdapter.OnLongClickListener {
            override fun onItemLongClicked(position: Int) {
                deleteEvent(position)
//                // 1. Remove the item from the list
//                listOfTasks.removeAt(position)
//
//                // 2. Notify the adapter that our data set has changed
//                adapter.notifyDataSetChanged()
//
//                saveItems()
            }
        }



//        // detect when the user clicks on the add button
//        findViewById<Button>(R.id.button).setOnClickListener {
//            Log.i("Yuni", "User clicked on button")
//        }

        loadItems()

        // Look up the recyclerView in layout
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        // Create adapter passing in the sample user data
        adapter = TaskItemAdapter(listOfTasks, onLongClickListener)
        // Attach the adapter to the recyclerview to populate items
        recyclerView.adapter = adapter
        // Set layout manager to position the items
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Set up the button and input field, so that the user can enter a task and add it to the list
        val inputTextField = findViewById<EditText>(R.id.addTaskField)
        findViewById<Button>(R.id.button).setOnClickListener {
            // 1. Grab the text user typed into @id/addTaskField
            val userInputtedTask = inputTextField.text.toString()

            // 2. Add the string to our list of tasks: listOfTasks
            listOfTasks.add(userInputtedTask)
            // Notify the adapter that the data has been updated
            adapter.notifyItemInserted(listOfTasks.size - 1)

            // 3. Reset text field
            inputTextField.setText("")

            saveItems()
        }
    }

    // Save the data that the use has inputted
    // Save data by writing and reading from a file

    // Get the file we need
    fun getDataFile() : File {
        // Every line represents a task in out list of tasks
        return File(filesDir, "data.txt")
    }

    // Load the items by reading every line in the data file
    fun loadItems() {
        try {
            listOfTasks = FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        } catch(ioException: IOException) {
            ioException.printStackTrace()
        }

    }

    // Save items by writing into our data file
    fun saveItems() {
        try {
            FileUtils.writeLines(getDataFile(), listOfTasks)
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }


}
