package me.jlhp.sivale.model.server;

/**
 * Created by jjherrer on 04/03/2015.
 */
public interface SiValeData {
    boolean isSessionExpired();
    boolean isError();
    String getError();
}
