package com.kamak.skripsweetnewwes;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * This file is part of SqliteImage
 * <p/>
 * Created by GuoJunjun <junjunguo.com> on March 22, 2015.
 */
public class MyImage {
    private String title, description, path,longitude,username,iduser,lattitude;
    private long datetimeLong;
    private SimpleDateFormat df = new SimpleDateFormat("MMMM d, yy  h:mm");

    public MyImage(String title, String description, String path,
                   long datetimeLong) {
        this.title = title;
        this.description = description;
        this.path = path;
        this.iduser = iduser;
        this.username = username;
        this.datetimeLong = datetimeLong;
    }

    public MyImage() {
    }

    /**
     * Gets title.
     *
     * @return Value of title.
     */
    public String getTitle() { return title; }

    /**
     * Gets datetime.
     *
     * @return Value of datetime.
     */
    public Calendar getDatetime() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(datetimeLong);
        return cal;
    }

    /**
     * Sets new datetimeLong.
     *
     * @param datetimeLong New value of datetimeLong.
     */
    public void setDatetime(long datetimeLong) {
        this.datetimeLong = datetimeLong;
    }

    /**
     * Sets new datetime.
     *
     * @param datetime New value of datetime.
     */
    public void setDatetime(Calendar datetime) {
        this.datetimeLong = datetime.getTimeInMillis();
    }

    /**
     * Gets description.
     *
     * @return Value of description.
     */
    public String getDescription() { return description; }

    public String getIduser() { return iduser; }
    public String getUsername() { return username; }

    /**
     * Sets new title.
     *
     * @param title New value of title.
     */
    public void setTitle(String title) { this.title = title; }

    /**
     * Gets datetimeLong.
     *
     * @return Value of datetimeLong.
     */
    public long getDatetimeLong() { return datetimeLong; }

    /**
     * Sets new description.
     *
     * @param description New value of description.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    public void set(String longitude) {
        this.longitude = longitude;
    }
    public void setLattitude(String lattitude) {
        this.lattitude = lattitude;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public void setIduser(String iduser) {
        this.iduser = iduser;
    }

    /**
     * Sets new path.
     *
     * @param path New value of path.
     */

    public void setPath(String path) { this.path = path; }

    /**
     * Gets path.
     *
     * @return Value of path.
     */
    public String getPath() { return path; }

    @Override public String toString() {
        return  df.format(getDatetime().getTime());
    }
    public String setTitle() {
        return  title;
    }


}
