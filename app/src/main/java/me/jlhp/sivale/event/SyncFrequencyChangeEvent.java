package me.jlhp.sivale.event;

/**
 * Created by JOSELUIS on 1/18/2016.
 */
public class SyncFrequencyChangeEvent {
    private int mSyncFrequency;

    public SyncFrequencyChangeEvent(int syncFrequency) {
        mSyncFrequency = syncFrequency;
    }

    public int getSyncFrequency() {
        return mSyncFrequency;
    }

    public void setSyncFrequency(int syncFrequency) {
        mSyncFrequency = syncFrequency;
    }
}
