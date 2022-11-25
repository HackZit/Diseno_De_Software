package com.example.myapplication

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback
import androidx.core.content.ContextCompat
import com.directions.route.*
import com.example.myapplication.PermissionUtils.PermissionDeniedDialog.Companion.newInstance
import com.example.myapplication.PermissionUtils.isPermissionGranted
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener
import com.google.android.gms.maps.GoogleMap.OnMyLocationClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.snackbar.Snackbar
import java.io.IOException
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

// Implement OnMapReadyCallback.
class SecondActivity: AppCompatActivity() {

    private val ip = "ec2-54-165-184-219.compute-1.amazonaws.com" // this is the host ip that your data base exists on you can use 10.0.2.2 for local host                                                    found on your pc. use if config for windows to find the ip if the database exists on                                                    your pc

    private val port = "5432" // the port sql server runs on

    private val Classes = "net.sourceforge.jtds.jdbc.Driver" // the driver that is required for this connection use                                                                           "org.postgresql.Driver" for connecting to postgresql

    private val database = "d47r312ehrchj" // the data base name

    private val username = "ysugackagnmvja" // the user name

    private val password = "d4907e1eaacb044bee14a4e58e951584db64c73c4664712cbb450e49b7e418d9" // the password

    private val url = "jdbc:postgresql://$ip:$port/$database?ssl=true&sslmode=require&sslfactory=org.postgresql.ssl.NonValidatingFactory" // the connection url string


    private var connection: Connection? = null
    var carros: ArrayList<String>? = null
    var carsid: ArrayList<Int>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set the layout file as the content view.
        setContentView(R.layout.activity_second)


        // poblacion del dropdown
        val spinnerCarros = findViewById<Spinner>(R.id.dropDownBooking)

        //seponenlosnombresdeloscarrosdentrodelarray
        val useridsql=(this.application as GlobalClass).getSomeVariable()
        Log.println(Log.DEBUG,"debug", "$useridsql es lo que saca en useridsql")

        val sql="SELECT * FROM parking_spot WHERE userid = $useridsql AND isactive = 1"
        Log.println(Log.DEBUG,"debug", "$sql es lo que manda")

        val rs=connection?.createStatement()?.executeQuery(sql)
        Log.println(Log.DEBUG,"debug", "$rs es lo que responde")
        carros = ArrayList()
        carsid = ArrayList()

        if(rs!=null){
            Log.println(Log.DEBUG,"debug", "entro")
            while(!rs.isLast){
                rs.next()
                val carBrand=rs.getString(4)
                val carModel=rs.getString(6)
                val plate=rs.getString(3)
                carros!!.add("$carBrand $carModel $plate")
                carsid!!.add(rs.getInt(1))
                Log.println(Log.DEBUG,"debug", "$carBrand $carModel $plate es lo que pondra")
            }
        }

        // Creating adapter for spinner
        val dataAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item, carros!!
        )

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerCarros.setAdapter(dataAdapter)
    }
    /*fun dir (){
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
            //val destinationview = findViewById<AutoCompleteTextView>(R.id.editTextTextPersonName2)
            val username= (this.application as GlobalClass).getSomeVariable()

            val sql1 = "SELECT COUNT(*) as count FROM direcciones WHERE USERNAME ='$username'"
            val rs1 = connection?.createStatement()?.executeQuery(sql1)
            println("RS1")
            if (rs1 != null) {
                rs1.next()
                val count: Int = rs1.getInt("count")
                println("Count "+ count)
                if (count == 0) {

                } else {
                    val sql = "SELECT * FROM direcciones WHERE USERNAME ='$username'"
                    val rs = connection?.createStatement()?.executeQuery(sql)
                    var destinos = arrayOf("")
                    if (rs != null) {
                        while (!rs.isLast) {
                            rs.next()
                            println("Destino " + rs.getString(3))
                            destinos = destinos.plus(rs.getString(3))
                        }
                    }
                    val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, destinos)
                    destinationview.setAdapter(adapter)
                }
            }

        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
            //Toast.makeText(this, "Class fail", Toast.LENGTH_SHORT).show()
        } catch (e: SQLException) {
            e.printStackTrace()
            //Toast.makeText(this, "Connected no " + e, Toast.LENGTH_LONG).show()
        }


    }*/

}