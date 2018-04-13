package com.kamak.skripsweetnewwes;

/**
 * Created by Dwi Pratiwi on 14/10/2017.
 */

public class DataAdapter
{
    public String ImageURL;
    public String ImageTitle;
    public String tanggal;
    public String isikomen;


    public String getImageUrl() {

        return ImageURL;
    }

    public void setImageUrl(String imageServerUrl) {

        this.ImageURL = imageServerUrl;
    }

    public String getImageTitle() {

        return ImageTitle;
    }

    public void setImageTitle(String Imagetitlename) {

        this.ImageTitle = Imagetitlename;
    }
    public String getTanggal() {

        return tanggal;
    }
    public void setTanggal(String tanggal) {

        this.tanggal = tanggal;
    }
    public String getIsikomen() {

        return isikomen;
    }
    public void setIsikomen(String isikomen) {

        this.isikomen = isikomen;
    }
}