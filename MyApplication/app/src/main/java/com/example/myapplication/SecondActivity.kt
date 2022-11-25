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
import kotlinx.android.synthetic.main.activity_second.*
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
    var parkings: ArrayList<String>? = null
    var cards: ArrayList<String>? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set the layout file as the content view.
        setContentView(R.layout.activity_second)

        connection = (this.application as GlobalClass).getConnection()

        // poblacion del dropdown
        val spinnerParking = findViewById<Spinner>(R.id.dropDownBooking)

        //se ponen los nombres de los parkeaderos dentro del array
        val useridsql=(this.application as GlobalClass).getSomeVariable()
        Log.println(Log.DEBUG,"debug", "$useridsql es lo que saca en useridsql")

        val sql="SELECT * FROM parking_spot WHERE userid = $useridsql AND isactive = '1'"
        Log.println(Log.DEBUG,"debug", "$sql es lo que manda")

        val rs=connection?.createStatement()?.executeQuery(sql)
        Log.println(Log.DEBUG,"debug", "$rs es lo que responde")

        parkings = ArrayList<String>()
        val parkigsID = ""
        if(rs!=null){
            Log.println(Log.DEBUG,"debug", "entro")
            while(!rs.isLast){
                rs.next()

                var parkname = ""
                val parkigsIDPrev=parkigsID
                val parkigsID=rs.getString(5)
                if(parkigsIDPrev !=parkigsID) {
                    val sql2 = "SELECT * FROM parking_locations WHERE parkingsid = $parkigsID"
                    Log.println(Log.DEBUG, "debug", "$sql2 es lo que manda2")

                    val rs2 = connection?.createStatement()?.executeQuery(sql2)
                    Log.println(Log.DEBUG, "debug", "$rs2 es lo que responde2")
                    if (rs2 != null) {
                        rs2.next()
                        parkname = rs2.getString(4)
                    }
                }
                val spotID = rs.getString(1)
                val parkDate = rs.getString(6)
                val parkTime = rs.getString(3)
                parkings!!.add("$parkname|$parkDate $parkTime #$spotID")
                Log.println(Log.DEBUG, "debug", "$parkname $parkDate #$spotID es lo que pondra park")
            }
        }

        // Creating adapter for spinner
        val dataAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item, parkings!!
        )
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerParking.setAdapter(dataAdapter)



        val spinnerCard = findViewById<Spinner>(R.id.dropDownCard)

        //lo mismo pero con las tarjetas
        val sql3="SELECT * FROM payment_methods WHERE userid = $useridsql"
        Log.println(Log.DEBUG,"debug", "$sql es lo que manda")

        val rs3=connection?.createStatement()?.executeQuery(sql3)
        Log.println(Log.DEBUG,"debug", "$rs es lo que responde")
        cards = ArrayList<String>()

        if(rs3!=null){
            Log.println(Log.DEBUG,"debug", "entro")
            while(!rs3.isLast){
                rs3.next()
                val cardNumber =rs3.getString(3)
                cards!!.add("$cardNumber")
                Log.println(Log.DEBUG,"debug", "$cardNumber es lo que pondra tarjeta")
            }
        }
        // Creating adapter for spinner
        val dataAdapter2: ArrayAdapter<String> = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item, cards!!
        )

        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerCard.setAdapter(dataAdapter2)
    }
    var selSpotID: String = ""
    var selSpotname: String = ""

    fun selectPark(view: View){

        val spinnerParking = findViewById<Spinner>(R.id.dropDownBooking)
        selSpotID = spinnerParking.selectedItem.toString().substringAfter("#")
        selSpotname = spinnerParking.selectedItem.toString().substringBefore("|")

        connection = (this.application as GlobalClass).getConnection()

        //se ponen los nombres de los parkeaderos dentro del array
        val useridsql=(this.application as GlobalClass).getSomeVariable()
        Log.println(Log.DEBUG,"debug", "$useridsql es lo que saca en useridsql")

        namebooked.setText(selSpotname)

        val sql2 = "SELECT * FROM parking_locations WHERE businessname  = '$selSpotname'"
        Log.println(Log.DEBUG, "debug", "$sql2 es lo que manda2")

        val rs2 = connection?.createStatement()?.executeQuery(sql2)
        Log.println(Log.DEBUG, "debug", "$rs2 es lo que responde2")

        if (rs2 != null) {
            rs2.next()
            addressbooked.setText(rs2.getString(2))
            totalprice.setText(rs2.getString(5))
        }


        val sql3 = "SELECT * FROM parking_spot WHERE spotid = $selSpotID  AND isactive = '1'"
        Log.println(Log.DEBUG, "debug", "$sql3 es lo que manda3")

        val rs3 = connection?.createStatement()?.executeQuery(sql3)
        Log.println(Log.DEBUG, "debug", "$rs3 es lo que responde3")

        if (rs3 != null) {
            rs3.next()
            Datebooked.setText(rs3.getString(6))
            timebook.setText(rs3.getString(3))

        }


        val carid = rs3?.getString(2)
        val sql4 = "SELECT * FROM car_data WHERE carid = $carid"
        Log.println(Log.DEBUG, "debug", "$sql4 es lo que manda4")

        val rs4 = connection?.createStatement()?.executeQuery(sql4)
        Log.println(Log.DEBUG, "debug", "$rs4 es lo que responde4")

        if (rs4 != null) {
            rs4.next()
            carbooked.setText(rs4.getString(3))
        }
    }
}