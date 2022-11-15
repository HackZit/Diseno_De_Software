package com.example.myapplication

import android.Manifest
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.activity_register.*
import java.sql.Connection
import java.sql.SQLException
import java.util.*


class RegisterAtivity : AppCompatActivity() {



    private val ip = "ec2-54-165-184-219.compute-1.amazonaws.com" // this is the host ip that your data base exists on you can use 10.0.2.2 for local host                                                    found on your pc. use if config for windows to find the ip if the database exists on                                                    your pc

    private val port = "5432" // the port sql server runs on

    private val Classes = "net.sourceforge.jtds.jdbc.Driver" // the driver that is required for this connection use                                                                           "org.postgresql.Driver" for connecting to postgresql

    private val database = "d47r312ehrchj" // the data base name

    private val username = "ysugackagnmvja" // the user name

    private val password = "d4907e1eaacb044bee14a4e58e951584db64c73c4664712cbb450e49b7e418d9" // the password

    private val url = "jdbc:postgresql://$ip:$port/$database?ssl=true&sslmode=require&sslfactory=org.postgresql.ssl.NonValidatingFactory" // the connection url string


    private var connection: Connection? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        birthdate.setOnClickListener {
            showDatePickerDialog()
        }
    }


    private fun showDatePickerDialog() {
        val newFragment = DatePickerFragment.newInstance(DatePickerDialog.OnDateSetListener { _, year, month, day ->
            val dayStr = day
            val monthStr = (month + 1)

            val selectedDate = "$monthStr/$dayStr/$year"
            birthdate.setText(selectedDate)
        })

        newFragment.show(supportFragmentManager, "datePicker")
    }


    fun registeruser(view: View?) {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.INTERNET),
            PackageManager.PERMISSION_GRANTED
        )
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        try {
            Class.forName(Classes)
            connection = (this.application as GlobalClass).getConnection()
            //Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show()
            val  name= findViewById<EditText>(R.id.usertxt4).text.toString()
            val  lname= findViewById<EditText>(R.id.usertxt5).text.toString()
            val  mail= findViewById<EditText>(R.id.usertxt6).text.toString()
            val  date= findViewById<EditText>(R.id.birthdate).text.toString()
            val  pass= findViewById<EditText>(R.id.passwordtxt2).text.toString()
            val sql = "INSERT INTO users (name, lastname, dateofbirth, email, password) VALUES ('$name', '$lname', '$date', '$mail', '$pass')"
            Log.println(Log.DEBUG,"debug", "SQL " + sql);
            with(connection) {
                this?.createStatement()?.execute(sql)
                //this?.commit()
                Log.println(Log.DEBUG,"debug", "Conectado " + sql);

            }
            val intent= Intent(this, MainActivity::class.java)
            startActivity(intent)
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
            //Toast.makeText(this, "Class fail", Toast.LENGTH_SHORT).show()
        } catch (e: SQLException) {
            e.printStackTrace()
            //Toast.makeText(this, "Connected no " + e, Toast.LENGTH_LONG).show()
        }


    }

}

class DatePickerFragment : DialogFragment() {

    private var listener: DatePickerDialog.OnDateSetListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(requireActivity(), listener, year, month, day)
    }

    companion object {
        fun newInstance(listener: DatePickerDialog.OnDateSetListener): DatePickerFragment {
            val fragment = DatePickerFragment()
            fragment.listener = listener
            return fragment
        }
    }

}