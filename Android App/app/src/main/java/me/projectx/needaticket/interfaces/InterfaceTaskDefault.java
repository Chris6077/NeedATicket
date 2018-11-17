package me.projectx.needaticket.interfaces;

public interface InterfaceTaskDefault {
    void onPreExecute(Class resource);
    void onPostExecute(Object result, Class resource);
}
