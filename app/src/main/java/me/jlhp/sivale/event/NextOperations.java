package me.jlhp.sivale.event;

import me.jlhp.sivale.api.SiValeOperation;

/**
 * Created by JOSELUIS on 4/4/2015.
 */
public interface NextOperations {
    SiValeOperation[] getNextOperations();
    void setNextOperations(SiValeOperation... nextOperation);
    boolean hasNextOperation();
}
