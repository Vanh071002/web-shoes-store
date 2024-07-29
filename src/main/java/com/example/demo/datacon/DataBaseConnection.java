            /*
             * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
             * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
             */
            package com.example.demo.datacon;

            import java.sql.Connection;
            import java.sql.DriverManager;
            import java.sql.SQLException;

            public class DataBaseConnection {

                /*USE BELOW METHOD FOR YOUR DATABASE CONNECTION FOR BOTH SINGLE AND MULTILPE SQL SERVER INSTANCE(s)*/
                /*DO NOT EDIT THE BELOW METHOD, YOU MUST USE ONLY THIS ONE FOR YOUR DATABASE CONNECTION*/

                private static DataBaseConnection instance;
                private Connection connection;

                private DataBaseConnection() throws Exception {
                    Class.forName("com.mysql.jdbc.Driver");
                    connection = DriverManager.getConnection(url, userID, password);
                }

                public static DataBaseConnection getInstance() throws Exception {
                    if (instance == null) {
                        instance = new DataBaseConnection();
                    }
                    return instance;
                }

                public Connection getConnection(){
                    try {
                        if(connection.isClosed()) {
                            Class.forName("com.mysql.jdbc.Driver");
                            connection = DriverManager.getConnection(url, userID, password);
                        }
                        return connection;
                    } catch (SQLException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
                private final String serverName = "localhost";
                private final String dbName = "/websneaker";
                private final String portNumber = "3306";
                private final String userID = "root";

                private final String password = "vietanh";

                private final String url = "jdbc:mysql://" + serverName + ":" + portNumber + dbName;
            }
