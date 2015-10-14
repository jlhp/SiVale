package me.jlhp.sivale.event;

import me.jlhp.sivale.api.SiValeOperation;

/**
 * Created by JOSELUIS on 4/5/2015.
 */
public interface CurrentOperation {
    SiValeOperation getCurrentOperation();
    void setCurrentOperation(SiValeOperation currentOperation);
}
