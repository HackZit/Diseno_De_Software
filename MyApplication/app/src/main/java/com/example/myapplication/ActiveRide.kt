package com.example.myapplication

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Polyline
import kotlinx.android.synthetic.main.activity_active_ride.*
import java.sql.Connection
import java.sql.ResultSet
import java.util.*
import kotlin.collections.ArrayList


class ActiveRide : AppCompatActivity() {


    private val ip = "ec2-54-165-184-219.compute-1.amazonaws.com" // this is the host ip that your data base exists on you can use 10.0.2.2 for local host                                                    found on your pc. use if config for windows to find the ip if the database exists on                                                    your pc

    private val port = "5432" // the port sql server runs on

    private val Classes = "net.sourceforge.jtds.jdbc.Driver" // the driver that is required for this connection use                                                                           "org.postgresql.Driver" for connecting to postgresql

    private val database = "d47r312ehrchj" // the data base name

    private val username = "ysugackagnmvja" // the user name

    private val password = "d4907e1eaacb044bee14a4e58e951584db64c73c4664712cbb450e49b7e418d9" // the password

    private val url = "jdbc:postgresql://$ip:$port/$database?ssl=true&sslmode=require&sslfactory=org.postgresql.ssl.NonValidatingFactory" // the connection url string


    private var connection: Connection? = null

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    var names: String? = null
    var dirs: String? = null
    var freesp: Int? = null
    var notfresp: Int? = null
    var parkingsid: String? = null
    var carros: ArrayList<String>? = null
    var carsid: ArrayList<Int>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_active_ride)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        // Get a handle to the fragment and register the callback.

        resdate.setOnClickListener {
            showDatePickerDialog()
        }
        etTime1.setOnClickListener {
            showTimePickerDialog()
        }
        val rides = intent.getSerializableExtra("rides") as rideshowev
        names = rides.nam
        dirs = rides.dir
        freesp = rides.libres
        notfresp = rides.ocupado
        parkingsid = rides.parkingid
        findViewById<TextView>(R.id.namep).text = names
        findViewById<TextView>(R.id.address).text = dirs
        findViewById<TextView>(R.id.frees).text = freesp.toString()

        connection=(this.application as GlobalClass).getConnection()

        // poblacion del dropdown
        val spinnerCarros = findViewById<Spinner>(R.id.dropDownCarros)

        //seponenlosnombresdeloscarrosdentrodelarray
        val useridsql=(this.application as GlobalClass).getSomeVariable()
        Log.println(Log.DEBUG,"debug", "$useridsql es lo que saca en useridsql")

        val sql="SELECT * FROM car_data WHERE userid = $useridsql"
        Log.println(Log.DEBUG,"debug", "$sql es lo que manda")

        val rs=connection?.createStatement()?.executeQuery(sql)
        Log.println(Log.DEBUG,"debug", "$rs es lo que responde")
        carros = ArrayList<String>()
        carsid = ArrayList<Int>()

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

    private fun showDatePickerDialog() {
        val newFragment = DatePickerFragment.newInstance(DatePickerDialog.OnDateSetListener { _, year, month, day ->
            val dayStr = day
            val monthStr = (month + 1)

            val selectedDate = "$monthStr/$dayStr/$year"
            resdate.setText(selectedDate)
        })

        newFragment.show(supportFragmentManager, "datePicker")
    }

    private fun showTimePickerDialog() {
        val timePicker = TimePickerFragment { onTimeSelected(it) }
        timePicker.show(supportFragmentManager, "timePicker")
    }

    private fun onTimeSelected(time: String) {
        etTime1.setText("$time")
    }

    fun book(view: View) {

        try {
            Class.forName(Classes)
            connection = (this.application as GlobalClass).getConnection()
            notfresp = notfresp?.plus(1)
            val username = (this.application as GlobalClass).getSomeVariable()
            val sql1 ="UPDATE parking_details Set numberofspotsoccupied=$notfresp WHERE parkingsid=$parkingsid"
            with(connection) {
                this?.createStatement()?.execute(sql1)
                Log.println(Log.DEBUG,"debug", "sql1"+sql1)
                //this?.commit()
            }
            val sql5 = "SELECT * FROM parking_spot WHERE parkingsid = $parkingsid"
            val rs = connection?.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)?.executeQuery(sql5)
            var newspot: Int? = null
            if(rs!=null){
                if (!rs.isBeforeFirst() ) {
                    System.out.println("No data")
                    newspot = 1
                } else {
                    Log.println(Log.DEBUG,"debug", "entro")
                    newspot = 1
                    while(!rs.isLast){
                        rs.next()
                        val spotid=Integer.parseInt(rs.getString(1))
                        val isactive=Integer.parseInt(rs.getString(8))
                        Log.println(Log.DEBUG,"debug", "SpotID "+ spotid +" isactive "+ isactive + " newspot "+ newspot)
                        if (spotid == newspot && isactive == 1) {
                            rs.beforeFirst()
                            newspot += 1
                            Log.println(Log.DEBUG,"debug", "NEWSPOT MAS 1")
                        }

                    }
                }

            }

            val sql4 = "SELECT COUNT(*) as count FROM parking_history"
            val rs1 = connection?.createStatement()?.executeQuery(sql4)

            var phistory = 0
            if (rs1 != null) {
                rs1.next()
                val count: Int = rs1.getInt("count")
                phistory = count + 1
            }

            val spinnerCarros = findViewById<Spinner>(R.id.dropDownCarros)
            val pid = Integer.parseInt(parkingsid)
            val carid=carsid?.get(spinnerCarros.selectedItemPosition)
            val  time = findViewById<EditText>(R.id.etTime1).text.toString()
            val  date = findViewById<EditText>(R.id.resdate).text.toString()
            Log.println(Log.DEBUG,"debug", "carsid"+carid)
            val user = Integer.parseInt(username)
            val sql2 = "INSERT INTO parking_spot(spotid, carid, parktime, parkinghistoryid, parkingsid, datepark, userid, isactive) VALUES ($newspot, $carid,'$time',$phistory,$pid, '$date', $user, '1')"
            with(connection) {
                this?.createStatement()?.execute(sql2)
                Log.println(Log.DEBUG,"debug", "sql2"+sql2)
                //this?.commit()
            }


            val sql3 = "INSERT INTO parking_history(spotid,userid,parkingsid) VALUES ($newspot,$user,$pid)"
            with(connection) {
                this?.createStatement()?.execute(sql3)
                //this?.commit()
            }

            val intent= Intent(this, rideshows::class.java)
            startActivity(intent)

        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
            //Toast.makeText(this, "Class fail", Toast.LENGTH_SHORT).show()
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

    class TimePickerFragment(val listener:(String) -> Unit) : DialogFragment(), TimePickerDialog.OnTimeSetListener {
        override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
            listener("$hourOfDay:$minute")
        }

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val c = Calendar.getInstance()
            val hour = c.get(Calendar.HOUR_OF_DAY)
            val minute = c.get(Calendar.MINUTE)
            val picker = TimePickerDialog(requireActivity(), this, hour, minute, true)
            return picker


        }
    }

}