package com.example.myapplication

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_reviewshistory.*
import kotlinx.android.synthetic.main.activity_rideshows.*
import java.math.BigInteger
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException


class reviewshistory : AppCompatActivity() {
    private val ip =
        "ec2-54-165-184-219.compute-1.amazonaws.com" // this is the host ip that your data base exists on you can use 10.0.2.2 for local host                                                    found on your pc. use if config for windows to find the ip if the database exists on                                                    your pc

    private val port = "5432" // the port sql server runs on

    private val Classes =
        "net.sourceforge.jtds.jdbc.Driver" // the driver that is required for this connection use                                                                           "org.postgresql.Driver" for connecting to postgresql

    private val database = "d47r312ehrchj" // the data base name

    private val username = "ysugackagnmvja" // the user name

    private val password =
        "d4907e1eaacb044bee14a4e58e951584db64c73c4664712cbb450e49b7e418d9" // the password

    private val url =
        "jdbc:postgresql://$ip:$port/$database?ssl=true&sslmode=require&sslfactory=org.postgresql.ssl.NonValidatingFactory" // the connection url string

    private var connection: Connection? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reviewshistory)

    }

    fun addpayment(view: View?) {

        try {
            Class.forName(Classes)
            connection = (this.application as GlobalClass).getConnection()
            //Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show()
            val  NAME = (this.application as GlobalClass).getSomeVariable()
            val userid = Integer.parseInt(NAME)
            val  cardname= findViewById<EditText>(R.id.cardname).text.toString()

            val  numbercard= findViewById<EditText>(R.id.cardnumber).text.toString()
            val cardnumber = BigInteger(numbercard)
            val  expdate= findViewById<EditText>(R.id.mmaaaa).text.toString()
            val  cvv= Integer.parseInt(findViewById<EditText>(R.id.cvv).text.toString())
            val  cardtype= findViewById<EditText>(R.id.cardtype).text.toString()
            val  billingaddr= findViewById<EditText>(R.id.billadr).text.toString()
            val  cellphone= findViewById<EditText>(R.id.cellphone).text.toString()
            val sql = "INSERT INTO payment_methods (userid, cardholder, cardnumber, expirationdate, cvv, cardtype, billingaddress, cellphonenumber) VALUES ($userid, '$cardname', $cardnumber, '$expdate', $cvv, '$cardtype', '$billingaddr', '$cellphone')"
            Log.println(Log.DEBUG,"debug", "SQL " + sql)
            with(connection) {
                this?.createStatement()?.execute(sql)
                //this?.commit()
                Log.println(Log.DEBUG,"debug", "Conectado " + sql);

            }
            Toast.makeText(this, "Correct Payment Added", Toast.LENGTH_SHORT).show()
            val intent= Intent(this, rideshows::class.java)
            startActivity(intent)
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
            Toast.makeText(this, "Error payment method", Toast.LENGTH_SHORT).show()
        } catch (e: SQLException) {
            e.printStackTrace()
            //Toast.makeText(this, "Connected no " + e, Toast.LENGTH_LONG).show()
        }
    }

    fun addcar(view: View?) {

        try {
            Class.forName(Classes)
            connection = (this.application as GlobalClass).getConnection()
            //Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show()
            val  NAME = (this.application as GlobalClass).getSomeVariable()
            val userid = Integer.parseInt(NAME)
            val  licenseplate= findViewById<EditText>(R.id.licenseplate).text.toString()
            val  carbrand= findViewById<EditText>(R.id.carbrand).text.toString()
            val  modelcar= findViewById<EditText>(R.id.modelcar).text.toString()
            val  cartype= findViewById<EditText>(R.id.cartype).text.toString()
            val sql = "INSERT INTO car_data (userid, licenseplate, carbrand, typeofcar, carmodel) VALUES ($userid, '$licenseplate', '$carbrand', '$cartype', '$modelcar')"
            Log.println(Log.DEBUG,"debug", "SQL " + sql)
            with(connection) {
                this?.createStatement()?.execute(sql)
                //this?.commit()
                Log.println(Log.DEBUG,"debug", "Conectado " + sql);

            }
            Toast.makeText(this, "Car Saved", Toast.LENGTH_SHORT).show()
            val intent= Intent(this, rideshows::class.java)
            startActivity(intent)
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
            Toast.makeText(this, "Car failed to save", Toast.LENGTH_SHORT).show()
        } catch (e: SQLException) {
            e.printStackTrace()
            //Toast.makeText(this, "Connected no " + e, Toast.LENGTH_LONG).show()
        }
    }

}