package com.sigmobile.services;

import com.orm.SugarRecord;

public class SavedStatistics extends SugarRecord {

    String lastRefreshed;

    public SavedStatistics() {

    }

    public String getLastRefreshed() {
        return lastRefreshed;
    }

    public void setLastRefreshed(String lastRefreshed) {
        this.lastRefreshed = lastRefreshed;
    }

    public SavedStatistics(String lr) {
        this.lastRefreshed = lr;
    }

}
