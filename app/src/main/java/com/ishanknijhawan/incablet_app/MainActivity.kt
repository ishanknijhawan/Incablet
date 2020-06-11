package com.ishanknijhawan.incablet_app

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    var alActive = arrayListOf<CustomerItem>()
    var alLeft = arrayListOf<CustomerItem>()
    var alOnBoard = arrayListOf<CustomerItem>()
    var newList = arrayListOf<CustomerItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val actionBar = supportActionBar
        actionBar!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.colorPrimaryDark)))

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        layoutNoInternet.visibility = View.GONE
        progress_circular.visibility = View.GONE

        //if device is connected to internet, show customers else show no internet dialog
        if (internetAvailable()) {
            layoutNoInternet.visibility = View.GONE
            progress_circular.visibility = View.VISIBLE
            fetchResults()
            initDialog()
        } else {
            layoutNoInternet.visibility = View.VISIBLE
        }

        //recheck for internet connection on device
        btnRetry.setOnClickListener {
            if (internetAvailable()) {
                layoutNoInternet.visibility = View.GONE
                fetchResults()
                initDialog()
            } else {
                layoutNoInternet.visibility = View.VISIBLE
            }
        }

    }

    //check if internet connection is available on device
    @Throws(InterruptedException::class, IOException::class)
    fun internetAvailable(): Boolean {
        val command = "ping -c 1 google.com"
        return Runtime.getRuntime().exec(command).waitFor() == 0
    }

    //init welcome dialog
    private fun initDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Welcome")
            .setMessage(
                """
                1. Click image to view full sized picture
                2. Click on dropdown to view more details
            """.trimIndent()
            )
            .setPositiveButton("Got it") { dialog, which ->
                // Respond to positive button press
                dialog.dismiss()
            }
            .show()
    }

    //fetch results from API
    private fun fetchResults() {
        GlobalScope.launch {
            //work done on background thread
            val response = withContext(Dispatchers.IO) { Client.api.execute() }
            Log.d("result", "response is...")
            if (response.isSuccessful) {
                val data = Gson().fromJson(response.body?.string(), Customer::class.java)
                //back to main thread for ui binding
                launch(Dispatchers.Main) {
                    progress_circular.visibility = View.GONE

                    //split the data list into active, left and on board status
                    for (i in data.list) {
                        when (i.status) {
                            "active" ->
                                alActive.add(i)
                            "left" ->
                                alLeft.add(i)
                            else ->
                                alOnBoard.add(i)
                        }
                    }
                    //sort individual list on date criteria
                    sortByDate(alActive)
                    sortByDate(alLeft)
                    sortByDate(alOnBoard)

                    //merge back all three sublist into new list
                    newList.addAll(alActive)
                    newList.addAll(alLeft)
                    newList.addAll(alOnBoard)
                    //pass this list for binding data
                    bindData(newList)
                }
            }
        }
    }

    //this function sorts the list in ascending order of date
    private fun sortByDate(list: ArrayList<CustomerItem>) {
        Collections.sort(list, object : Comparator<CustomerItem> {
            var f: DateFormat = SimpleDateFormat("dd/MM/yyyy")

            override fun compare(p0: CustomerItem?, p1: CustomerItem?): Int {
                return try {
                    f.parse(p0!!.date).compareTo(f.parse(p1!!.date))
                } catch (e: ParseException) {
                    throw IllegalArgumentException(e)
                }
            }
        })
    }

    //bind the new list to the adapter and show on screen
    private fun bindData(list: ArrayList<CustomerItem>) {
        rvCustomer.layoutManager = LinearLayoutManager(this)
        rvCustomer.adapter = CustomerAdapter(list, this)
    }
}