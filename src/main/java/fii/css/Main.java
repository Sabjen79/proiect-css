package fii.css;

import fii.css.database.Database;

import java.sql.*;

public class Main {
    public static void main(String[] args) throws SQLException {
        Database.INSTANCE.initialize();
    }
}