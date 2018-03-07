package com.dimanem.android.nearbyplaces.view.list

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.dimanem.android.nearbyplaces.R
import com.dimanem.android.nearbyplaces.entities.Place
import com.squareup.picasso.Picasso


/**
 * Created by dimanemets on 06/03/2018.
 */
class NearbyPlacesListAdapter(private var places: List<Place>) : RecyclerView.Adapter<NearbyPlacesListAdapter.PlaceViewHolder>() {

    override fun onBindViewHolder(holder: PlaceViewHolder?, position: Int) {
        val place = places[position]
        holder?.name?.text = place.name
        holder?.address?.text = place.address
        Picasso.with(holder?.name?.context).load(place.icon).into(holder?.icon)
        if (place.rating != null) {
            holder?.rating?.text = place.rating.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): PlaceViewHolder {
        val itemView = LayoutInflater.from(parent?.context)
                .inflate(R.layout.nearby_place_list_row, parent, false)

        return PlaceViewHolder(itemView)
    }

    override fun getItemCount(): Int = places.size

    fun setPlaces(places: List<Place>) {
        this.places = places
        notifyDataSetChanged()
    }

    class PlaceViewHolder : RecyclerView.ViewHolder {

        val name: TextView
        val rating: TextView
        val address: TextView
        val icon: ImageView

        constructor(itemView: View) : super(itemView) {
            name = itemView.findViewById(R.id.name)
            rating = itemView.findViewById(R.id.rating)
            address = itemView.findViewById(R.id.address)
            icon = itemView.findViewById(R.id.icon)
        }
    }
}
