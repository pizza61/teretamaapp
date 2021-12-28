package com.example.teretamaapp

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.teretamaapp.room.Anime
import com.example.teretamaapp.room.AnimeViewModel

class AnimeAdapter(private val mList: List<Anime>, private var mContext: Context, private val mAnimeViewModel: AnimeViewModel) : RecyclerView.Adapter<AnimeAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.anime_view_design_list, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemViewModel = mList[position]

        holder.imageView.load(itemViewModel.imageUri)
        holder.item = itemViewModel
        holder.title.text = itemViewModel.title
        holder.details.text = mContext.getString(R.string.anime_details, itemViewModel.releaseYear, itemViewModel.studios, itemViewModel.episodeCount)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnCreateContextMenuListener {
        val imageView: ImageView = itemView.findViewById(R.id.anime_cover)
        val title: TextView = itemView.findViewById(R.id.anime_title)
        val details: TextView = itemView.findViewById(R.id.anime_details)
        lateinit var item: Anime

        init {
            itemView.setOnCreateContextMenuListener(this)
            itemView.setOnClickListener {
                // Open AniList page for this anime
                val url = "https://anilist.co/anime/" + item.anilistId

                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)

                mContext.startActivity(intent)
            }
        }

        override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
            menu!!.add(0, v!!.id, 1, R.string.item_remove).setOnMenuItemClickListener {
                mAnimeViewModel.delete(item)

                Toast.makeText(mContext, R.string.item_removed, Toast.LENGTH_SHORT).show()
                return@setOnMenuItemClickListener true
            }
        }
    }
}