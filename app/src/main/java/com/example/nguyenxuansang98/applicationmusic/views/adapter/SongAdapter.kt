package com.example.nguyenxuansang98.applicationmusic.views.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nguyenxuansang98.applicationmusic.R
import com.example.nguyenxuansang98.applicationmusic.model.entity.Song
import com.example.nguyenxuansang98.applicationmusic.model.inteface.setOnClickListenner
import kotlinx.android.synthetic.main.music_item.view.*

class SongAdapter(val arrSong : ArrayList<Song>) : RecyclerView.Adapter<SongAdapter.ViewHolder>(){
    private var setOnClickListenner : setOnClickListenner? = null

    fun setOnClickListener(mClickListener :setOnClickListenner){
        setOnClickListenner = mClickListener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.music_item,parent,false))

    override fun getItemCount(): Int = arrSong.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindata()
        holder.itemView.setOnClickListener {
            setOnClickListenner!!.onClickItemRecyclerView(holder.view,position)
        }
    }

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view){
        fun bindata(){
            Glide.with(itemView.context).load(arrSong.get(adapterPosition).linkImageSong).into(itemView.image_music_item)
            itemView.text_name_music_item.text = arrSong.get(adapterPosition).nameSong
            itemView.text_singer_music_item.text = arrSong.get(adapterPosition).singer
        }
    }
}