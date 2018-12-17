package me.projectx.needaticket.pojo;

public class LocalDatabase {
    private static String uID;
    private static LocalDatabase database = null;

    private LocalDatabase() {
    }

    public static String getuID() {
        return uID;
    }

    public static void setuID(String uID) {
        LocalDatabase.uID = uID;
    }

    public static LocalDatabase getInstance(){
        if(LocalDatabase.database == null)
            LocalDatabase.database = new LocalDatabase();
        return LocalDatabase.database;
    }
}
