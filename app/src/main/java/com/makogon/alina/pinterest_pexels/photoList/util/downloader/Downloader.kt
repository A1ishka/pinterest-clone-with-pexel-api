package com.makogon.alina.pinterest_pexels.photoList.util.downloader

interface Downloader {
    fun downloadFile(url: String): Long
}