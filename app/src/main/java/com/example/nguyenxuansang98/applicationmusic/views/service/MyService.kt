package com.example.nguyenxuansang98.applicationmusic.views.service

import android.app.Activity
import android.app.AlertDialog
import android.app.Service
import android.app.TimePickerDialog
import android.content.Intent
import android.media.MediaPlayer
import android.os.*
import android.util.Log
import android.view.LayoutInflater
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TimePicker
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.nguyenxuansang98.applicationmusic.R
import com.example.nguyenxuansang98.applicationmusic.model.entity.Song
import com.example.nguyenxuansang98.applicationmusic.views.activity.MainActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_timer.*
import kotlinx.android.synthetic.main.dialog_timer.view.*
import java.text.SimpleDateFormat

class MyService  : Service() {
    var positions :Int =0
    var iBinder = BoundMyService()
    var mediaPlayer: MediaPlayer? = null
    var annimation : Animation?=null
    var timepicker : TimePickerDialog?=null
    override fun onBind(intent: Intent?): IBinder? {
        return iBinder
    }

    override fun onCreate() {
        super.onCreate()
    }

    class BoundMyService : Binder(){
        fun getService():MyService{
            return MyService()
        }
    }
    fun playMedia(activity: Activity){
        if (mediaPlayer!!.isPlaying) {
            mediaPlayer!!.pause()
            activity.button_play.setBackgroundResource(R.drawable.play)
            activity.button_play_playmusic.setBackgroundResource(R.drawable.play)
        } else {
            mediaPlayer!!.start()
            activity.button_play.setBackgroundResource(R.drawable.pause)
            activity.button_play_playmusic.setBackgroundResource(R.drawable.pause)
        }
    }

    override fun onDestroy() {
        if(mediaPlayer!=null){
            mediaPlayer!!.stop()
        }
        super.onDestroy()
    }
    fun MediaPlayer(activity: MainActivity,data : ArrayList<Song>,position:Int) {
        positions = position
        mediaPlayer = MediaPlayer.create(activity, data.get(positions).file)
        Glide.with(activity).load(data.get(positions).linkImageSong).into(activity.image_play_music)
        activity.text_name_song_play_music.text = data.get(positions).nameSong
        activity.text_singer_play_music.text = data.get(positions).singer
        Glide.with(activity).load(data.get(positions).linkImageSong).into(activity.circleImageView)
        activity.text_name_music_playmusic.text = data.get(positions).nameSong
        activity.text_singer_playmusic.text = data.get(positions).singer
        setTime(activity)
        updateTimeSong(activity,data)
        annimation = AnimationUtils.loadAnimation(activity,R.anim.image_rotate)
    }
    fun MediaPlayerRepeat(activity: MainActivity,data : ArrayList<Song>,position:Int) {
        positions = position
        mediaPlayer = MediaPlayer.create(activity, data.get(positions).file)
        Glide.with(activity).load(data.get(positions).linkImageSong).into(activity.image_play_music)
        activity.text_name_song_play_music.text = data.get(positions).nameSong
        activity.text_singer_play_music.text = data.get(positions).singer
        Glide.with(activity).load(data.get(positions).linkImageSong).into(activity.circleImageView)
        activity.text_name_music_playmusic.text = data.get(positions).nameSong
        activity.text_singer_playmusic.text = data.get(positions).singer
        setTime(activity)
        repeatSong(activity,data)
        annimation = AnimationUtils.loadAnimation(activity,R.anim.image_rotate)
    }
    fun nextMini(activity: MainActivity,data : ArrayList<Song>){
        positions++
        if (positions > data.size - 1) {
            positions = 0
        }
        if (mediaPlayer!!.isPlaying) {
            mediaPlayer!!.stop()
        }
        MediaPlayer(activity,data,positions)
        mediaPlayer!!.start()
    }
    fun backMini(activity: MainActivity,data : ArrayList<Song>){
        positions--
        if (positions < 0) {
            positions = data.size - 1
        }
        if (mediaPlayer!!.isPlaying) {
            mediaPlayer!!.stop()
        }
        MediaPlayer(activity,data,positions)
        mediaPlayer!!.start()
    }

    fun checkMedia(activity: MainActivity,data : ArrayList<Song>,position:Int){
        if(mediaPlayer==null)
        {
            MediaPlayer(activity,data,position)
            mediaPlayer!!.start()
            activity.button_play.setBackgroundResource(R.drawable.pause)
            activity.button_play_playmusic.setBackgroundResource(R.drawable.pause)
           activity.image_play_music.startAnimation(annimation)
        }
        if(mediaPlayer!=null){
            mediaPlayer!!.stop()
            MediaPlayer(activity,data,position)
            mediaPlayer!!.start()
            activity.button_play.setBackgroundResource(R.drawable.pause)
            activity.button_play_playmusic.setBackgroundResource(R.drawable.pause)
            activity.image_play_music.startAnimation(annimation)
        }
    }
    fun setTime(activity: MainActivity){
        var simpleDateFormat : SimpleDateFormat = SimpleDateFormat("mm:ss")
        activity.text_time_finish_play_music.text = simpleDateFormat.format(mediaPlayer!!.duration)
        activity.seekbar_play_music.max = mediaPlayer!!.duration
    }
    fun seekTo(activity: MainActivity){
        mediaPlayer!!.seekTo(activity.seekbar_play_music!!.progress)
    }
    fun updateTimeSong(activity: MainActivity,data : ArrayList<Song>){
        var handler = Handler()
        handler.postDelayed(object :Runnable{
            override fun run() {
                var simpleDateFormat = SimpleDateFormat("mm:ss")
                activity.text_time_start_play_music.text = simpleDateFormat.format(mediaPlayer!!.currentPosition)
                activity.seekbar_play_music.progress = mediaPlayer!!.currentPosition
                mediaPlayer!!.setOnCompletionListener(object : MediaPlayer.OnCompletionListener{
                    override fun onCompletion(mp: MediaPlayer?) {
                        positions++
                        if (positions > data.size - 1) {
                            positions = 0
                        }
                        if (mediaPlayer!!.isPlaying) {
                            mediaPlayer!!.stop()
                        }
                        MediaPlayer(activity,data,positions)
                        mediaPlayer!!.start()
                    }
                })
                handler.postDelayed(this,500)
            }
        },100)
    }
    fun repeatSong(activity: MainActivity,data : ArrayList<Song>){
        var handler = Handler()
        handler.postDelayed(object :Runnable{
            override fun run() {
                var simpleDateFormat = SimpleDateFormat("mm:ss")
                activity.text_time_start_play_music.text = simpleDateFormat.format(mediaPlayer!!.currentPosition)
                activity.seekbar_play_music.progress = mediaPlayer!!.currentPosition
                mediaPlayer!!.setOnCompletionListener(object : MediaPlayer.OnCompletionListener{
                    override fun onCompletion(mp: MediaPlayer?) {
                        if (mediaPlayer!!.isPlaying) {
                            mediaPlayer!!.stop()
                        }
                        MediaPlayerRepeat(activity,data,positions)
                        mediaPlayer!!.start()
                    }
                })
                handler.postDelayed(this,500)
            }
        },100)
    }
    fun timer(activity: MainActivity,data : ArrayList<Song>){
        var minute:String? = null
        val diaLogView = LayoutInflater.from(activity).inflate(R.layout.dialog_timer, null)
        val builder = AlertDialog.Builder(activity).setView(diaLogView).show()
        diaLogView.button_timerr.setOnClickListener {
            minute = diaLogView.edit_timer.text.toString()
            var timer = object :CountDownTimer(minute.toString().toLong()*1000,1000){
                override fun onFinish() {
                    playMedia(activity)
                    activity.button_play_playmusic.setBackgroundResource(R.drawable.play)
                    activity.button_play.setBackgroundResource(R.drawable.play)
                    activity.button_timer.setBackgroundResource(R.drawable.clock)
                }

                override fun onTick(millisUntilFinished: Long) {

                }

            }.start()
            builder.dismiss()
        }
        diaLogView.button_backtimer.setOnClickListener {
            builder.dismiss()
            activity.button_timer.setBackgroundResource(R.drawable.clock)
        }
    }

}