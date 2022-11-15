package com.example.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.item_rides.view.*


class rideAdapter(private val mContext: Context, private val listarides: List<rideshowev>) : ArrayAdapter<rideshowev>(mContext, 0,listarides) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layout= LayoutInflater.from(mContext).inflate(R.layout.item_rides, parent,false)

        val ride = listarides[position]

        layout.name.text = ride.nam
        layout.dir.text = ride.dir
        layout.free.text = ride.libres
        layout.notfree.text = ride.ocupado
        layout.imageView.setImageResource(ride.imagen)


        return layout
    }
}