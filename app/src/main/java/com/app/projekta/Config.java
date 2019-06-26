package com.app.projekta;

public class Config {

    //your admin panel url and should 1 connection
    public static final String ADMIN_PANEL_URL = "http://192.168.100.243/projekta/"; //my ip address

    //your api key which obtained from admin panel
    public static final String API_KEY = "cda11EHdXxUGrNeAk64ZSz80avy32hYKt5bBCQFWjcqIwRoJlO";

    //set true to enable tab layout or set false to disable tab layout
    public static final boolean ENABLE_TAB_LAYOUT = true;

    //set true to turn on grid view in the channel list
    public static final boolean ENABLE_GRID_MODE = false;
    public static final int GRID_SPAN_COUNT = 3;

    //if you use RTL Language e.g : Arabic Language or other, set true
    public static final boolean ENABLE_RTL_MODE = false;

    //load more for next channel list
    public static final int LOAD_MORE = 15;

    //splash screen duration in millisecond
    public static final int SPLASH_TIME = 3000;

}