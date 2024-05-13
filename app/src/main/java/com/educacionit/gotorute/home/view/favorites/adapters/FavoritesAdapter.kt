package com.educacionit.gotorute.home.view.favorites.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.educacionit.gotorute.R
import com.educacionit.gotorute.congrats_route.model.db.entities.FavoriteRoute

class FavoritesAdapter :
    RecyclerView.Adapter<FavoritesAdapter.FavoritesViewHolder>() {
    private var favorites: List<FavoriteRoute> = listOf()

    fun setData(routes: List<FavoriteRoute>) {
        this.favorites = routes
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.favorite_card_item, parent, false)
        return FavoritesViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {
        val item = favorites[position]
        holder.startTextView.text = item.startPlace.displayName
        holder.destinationTextView.text = item.destinationPlace.getFormattedName()
        holder.dateTextView.text = item.date
    }

    override fun getItemCount(): Int {
        return favorites.size
    }

    class FavoritesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val startTextView: TextView = itemView.findViewById(R.id.text_origin)
        val destinationTextView: TextView = itemView.findViewById(R.id.text_destination)
        val dateTextView: TextView = itemView.findViewById(R.id.text_date)
    }
}