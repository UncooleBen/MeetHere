package com.webapp.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class Message {

    private String _username;
    private String _content;
    private Date _time;
    private UUID _uuid;
    private String _ago;
    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private String _path;
    private String _uuidstr;

    public Message(String username, String content, Date time) {
        this._content = content;
        this._username = username;
        this._time = time;
        this._uuid = UUID.randomUUID();
        this._uuidstr = this._uuid.toString();
        this._ago = "Error";
        this._path = null;
    }

    public Message(UUID uuid, String username, String content, Date time, String path) {
        this._username = username;
        this._content = content;
        this._time = time;
        this._uuid = uuid;
        this._uuidstr = uuid.toString();
        this._path = path;
    }

    public Message(UUID uuid, String username, String content, Date time) {
        this._content = content;
        this._username = username;
        this._time = time;
        this._uuid = uuid;
        this._uuidstr = uuid.toString();
        this._path = null;
    }

    public String get_username() {
        return _username;
    }

    public String get_content() {
        return _content;
    }

    public Date get_time() {
        return _time;
    }

    public UUID get_uuid() {
        return this._uuid;
    }

    public String get_path() {
        return this._path;
    }

    public String get_uuidstr() {
        return this._uuidstr;
    }

    public void set_ago(long millisec) {
        long duration = millisec - this._time.getTime();
        duration /= 1000;
        if (duration < 1) {
            this._ago = "Just Now";
        } else if (duration < 60) {
            this._ago = duration + " Second(s) Ago";
        } else if (duration < 3600) {
            this._ago = duration / 60 + " Minute(s) Ago";
        } else if (duration < 3600 * 24) {
            this._ago = duration / 3600 + " Hours(s) Ago";
        } else {
            this._ago = format.format(this._time);
        }
    }

    public String get_ago() {
        return this._ago;
    }

    public void set_path(String _path) {
        this._path = _path;
    }

    @Override
    public String toString() {
        return "Message { username : " + this._username + " content : " + this._content + " date : " + this._time
                + " }";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (!Message.class.isAssignableFrom(obj.getClass()))
            return false;
        final Message target = (Message) obj;
        if (!this.get_uuid().equals(target.get_uuid())) {
            return false;
        }

        if (!this.get_time().toString().equals(target.get_time().toString())) {
            return false;
        }

        if (!this.get_username().equals(target.get_username())) {
            return false;
        }
        return this.get_content().equals(target.get_content());

    }
}
