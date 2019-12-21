package com.webapp.model;

/**
 * @author Juntao Peng
 */
public class Record {

    private int id;
    private long time;
    private long startDate;
    private long endDate;
    private int userId;
    private int buildingId;
    private boolean verified;

    public Record(
            int id, long time, long startDate, long endDate, int userId, int buildId, boolean verified) {
        this.id = id;
        this.time = time;
        this.startDate = startDate;
        this.endDate = endDate;
        this.userId = userId;
        this.buildingId = buildId;
        this.verified = verified;
    }

    public Record(long time, long startDate, long endDate, int userId, int buildId, boolean verified) {
        this.time = time;
        this.startDate = startDate;
        this.endDate = endDate;
        this.userId = userId;
        this.buildingId = buildId;
        this.verified = verified;
    }

    public Record() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(int buildingId) {
        this.buildingId = buildingId;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Record record = (Record) o;

        if (id != record.id) return false;
        if (time != record.time) return false;
        if (startDate != record.startDate) return false;
        if (endDate != record.endDate) return false;
        if (userId != record.userId) return false;
        if (buildingId != record.buildingId) return false;
        return verified == record.verified;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (int) (time ^ (time >>> 32));
        result = 31 * result + (int) (startDate ^ (startDate >>> 32));
        result = 31 * result + (int) (endDate ^ (endDate >>> 32));
        result = 31 * result + userId;
        result = 31 * result + buildingId;
        result = 31 * result + (verified ? 1 : 0);
        return result;
    }
}
