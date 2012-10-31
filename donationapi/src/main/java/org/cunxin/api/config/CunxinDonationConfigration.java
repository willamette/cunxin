package org.cunxin.api.config;

public class CunxinDonationConfigration {

}

class MongoConfiguration {
    private String _host;
    private int _port;
    private String _user;
    private String _password;
    private String _database;

    MongoConfiguration(String host, int port, String user, String password, String database) {
        _host = host;
        _port = port;
        _user = user;
        _password = password;
        _database = database;
    }

    public String getHost() {
        return _host;
    }

    public int getPort() {
        return _port;
    }

    public String getUser() {
        return _user;
    }

    public String getPassword() {
        return _password;
    }

    public String getDatabase() {
        return _database;
    }
}

