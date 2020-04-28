package com.example.nguyenxuansang98.applicationmusic.model.entity

import java.io.File

class Song {
    val linkImageSong: String
    var nameSong: String
    var singer: String
    var file: Int

    constructor(linkImageSong: String, nameSong: String, singer: String, file: Int) {
        this.linkImageSong = linkImageSong
        this.nameSong = nameSong
        this.singer = singer
        this.file = file
    }
}