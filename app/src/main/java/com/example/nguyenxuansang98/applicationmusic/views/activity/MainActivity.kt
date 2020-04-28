package com.example.nguyenxuansang98.applicationmusic.views.activity

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.SeekBar
import androidx.core.os.postDelayed
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.nguyenxuansang98.applicationmusic.R
import com.example.nguyenxuansang98.applicationmusic.model.entity.Song
import com.example.nguyenxuansang98.applicationmusic.model.inteface.setOnClickListenner
import com.example.nguyenxuansang98.applicationmusic.views.adapter.SongAdapter
import com.example.nguyenxuansang98.applicationmusic.views.service.MyService
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.image_play_music
import kotlinx.android.synthetic.main.activity_main.text_name_song_play_music
import kotlinx.android.synthetic.main.activity_main.text_singer_play_music
import java.nio.CharBuffer
import java.text.SimpleDateFormat


class MainActivity : AppCompatActivity(), setOnClickListenner {
    var mediaPlayer: MediaPlayer? = null
    var boundService : MyService ? = null
    var checkConnection = false
    var checkRepeat =1
    var checkRepeat1 =0
    fun dataSong(): ArrayList<Song> {
        return arrayListOf(
            Song(
                "https://img1.kpopmap.com/2020/01/Park-SeoJoon-as-Park-SaeRoi.jpg","BewhY 비와이 (Itaewon Class OST)",
                "Newly(새로이)",
                R.raw.a
            ),
            Song(
                "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQOiqqgfG6rt96FYA_PdCXks4JjwLdl71Hrtec9pR92S5OjRbWN&usqp=CAU","Diamond (Itaewon Class OST)",
                "Ha Hyun Woo",
                R.raw.diamon
            ),
            Song(
                "https://avatar-nct.nixcdn.com/song/2020/03/03/f/9/2/f/1583219529544_640.jpg","With Us (Itaewon Class OST)",
                "Verivery",
                R.raw.b
            ),
            Song(
                "https://avatar-nct.nixcdn.com/song/2020/02/04/2/a/4/0/1580799231699_640.jpg",
                        "Beginning (Itaewon Class OST)",
                "Gaho",
                R.raw.start
            ),
            Song(
"https://1.bp.blogspot.com/--1ytJlr1y3k/Xj8Vc08yXCI/AAAAAAAAnBE/5Jpi_UxNY8onw8Elt0uex8Cv1z2y1NFsACLcBGAsYHQ/s1600/folder.jpg","더 베인 (Itaewon Class OST)",
                "직진",
                R.raw.d
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (checkConnection==false){
            var inten = Intent(this,MyService::class.java)
            bindService(inten,serviceConnection,BIND_AUTO_CREATE)
        }
        constraint_playmusic.setOnClickListener {
            constraint_play_music.visibility = View.VISIBLE
            constraint_main.visibility = View.INVISIBLE
            constraint_playmusic.visibility = View.INVISIBLE
            image_play_music.startAnimation(boundService!!.annimation)
            var animSlideDown = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.layout_up);
            constraint_play_music.startAnimation(animSlideDown);
        }
        recycler_song.layoutManager = LinearLayoutManager(this)
        val adapter = SongAdapter(dataSong())
        recycler_song.adapter = adapter
        adapter.setOnClickListener(this)
        image_tb.setOnClickListener {
            var animSlideDown = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.layout_down);
            constraint_play_music.startAnimation(animSlideDown);
            constraint_play_music.visibility = View.INVISIBLE
            constraint_playmusic.visibility = View.VISIBLE
            constraint_main.visibility = View.VISIBLE
            circleImageView.startAnimation(boundService!!.annimation)
        }

        button_play.setOnClickListener {
            if(boundService!=null){
                boundService!!.playMedia(this)
            }
        }
        button_repeat.setOnClickListener {
                boundService!!.repeatSong(this,dataSong())
                button_repeat.setBackgroundResource(R.drawable.repeat1)
        }
        button_repeat.setOnLongClickListener {
            boundService!!.updateTimeSong(MainActivity(),dataSong())
            button_repeat.setBackgroundResource(R.drawable.repeat)
            false
        }
        button_play_playmusic.setOnClickListener {
            boundService!!.playMedia(this)
        }
        button_next_playmusic.setOnClickListener {
            boundService!!.nextMini(this,dataSong())
        }
        button_previous_playmusic.setOnClickListener {
            boundService!!.backMini(this,dataSong())
        }
        button_next.setOnClickListener {
            boundService!!.nextMini(this,dataSong())
        }
        button_previous.setOnClickListener {
            boundService!!.backMini(this,dataSong())
        }
        button_timer.setOnClickListener {
        boundService!!.timer(this,dataSong())
            button_timer.setBackgroundResource(R.drawable.clock1)
        }
        seekbar_play_music.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                boundService!!.seekTo(this@MainActivity)
            }
        })
    }
    var serviceConnection = object : ServiceConnection{
        override fun onServiceDisconnected(name: ComponentName?) {
            checkConnection=false
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            var iBinder :MyService.BoundMyService = service as MyService.BoundMyService
            boundService = iBinder.getService()
            checkConnection= true
        }

    }
    override fun onClickItemRecyclerView(view: View, position: Int) {
        //positions = position
        constraint_play_music.visibility = View.VISIBLE
        constraint_main.visibility = View.INVISIBLE
        constraint_playmusic.visibility = View.INVISIBLE
        var animSlideDown = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.layout_up);
        constraint_play_music.startAnimation(animSlideDown);
        if(checkConnection==true){
            boundService!!.checkMedia(this,dataSong(),position)
        }

    }

    override fun onDestroy() {
        unbindService(serviceConnection)
        super.onDestroy()
    }

    override fun onPause() {
        Log.e("A","onPause")
        super.onPause()
    }
}
