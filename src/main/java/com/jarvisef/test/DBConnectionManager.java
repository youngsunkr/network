/*
http://javaservice.net/~java/bbs/data/qna2/1030996512+/DBConnectionManager.java

DBConnectionPool의 구현


데이터베이스 커넥션 풀을 구현하기위해서 사용되는 클래스는 DBHandler.java와  DBConnectionManager.java 이다.
DBHandler클래스는 사용가능한 Connection객체를 미리 저장하고 반화해주는 역활을 한다.

DBConnectionManager 클래스는 실제 데이터베이스 커넥션풀을 사용하는 jsp페이지나 서블릿 클래스가 사용하는 클래스이다.
DBConnectionManager 클래스는 DBHandler클래스의 객체를 관리하고 사용자들의 요청에 따라 DBHandler클래스로부터
데이터베이스 연결된 Connection객체를 구해주거나 사용된 Connection객체를 다시  DBConnectionManager에 반환하는 역활을 한다.

DBConnectionManager.java    -----------> DBHandler.java --------------> Oracle  or DB2 or MySQl 등등.....


-- package의 사용 ---

import는 사용 하고자 하는 class가 다른 package에 있을 때한 사용 합니다.
또한 import되는 class는 반드시 public class이어야 하고 public 생성자를 가져야 합니다.

*/
package com.jarvisef.test;

/**
 * Created by youngsunkr on 2015-06-10.
 */
/*
 * Copyright (c) 1998 by Gefion software.
 *
 * Permission to use, copy, and distribute this software for
 * NON-COMMERCIAL purposes and without fee is hereby granted
 * provided that this copyright notice appears in all copies.
 *
 */

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.Date;

/**
 * This class is a Singleton that provides access to one or many
 * connection pools defined in a Property file. A client gets
 * access to the single instance through the static getInstance()
 * method and can then check-out and check-in connections from a pool.
 * When the client shuts down it should call the release() method
 * to close all open connections and do other clean up.
 */

/*
DBConnectionManager 클래스는 커넥션 풀 을 관리한다.
DBConnectionManager 클래스의 인스턴스는 오직 한개만 필요하기 때문에 싱클톤패턴을 사용하여 구현한다.
DBConnectionManager 클래스는 static필드를 사용하여 오직 한개의 인스턴스만을 사용한다.
*/


public class DBConnectionManager {
    static private DBConnectionManager instance;       // The single instance

    static private int clients;  // 풀을 사용하는 클라이언트 개수를 추적할때 사용된다. 클라이언트가 getInstance()를 호출할때마다 변수를 증가시키고 release()를 호출할때마다 감소시켜서 어떤 클라이언트풀의 자원을 사용하지 않을때 모든 ConnectionPool객체를 부드럽게 셧다운할수있도록한다.

    private Vector drivers = new Vector();
    private PrintWriter log;
    private Hashtable pools = new Hashtable(); // DBHandler객체를 저장하는 곳


    /*
    DBConnectionManager 클래스를 사용하려는 jsp 페이지나 서블릿 또는 기타 다른 클래스는 다음과 같이
    getInstance()메소드를 사용하여 DBConnectionManager 클래스의 인스턴스를 구한다.
    clients가 DBConnectionManager.getInstance()메소드를 처음 호출하면 DBConnectionManager 클래스의 생성자가 호출되고
    생성자는 이어서 init()메소드를 호출한다.
    init()메소드는  db.properties 파일로 부터 데이터베이스 커넥션 풀을 초기화하기 위한 정보를 읽어 들인다.
    */
    static synchronized public DBConnectionManager getInstance() {
        if (instance == null) {
            instance = new DBConnectionManager();
        }
        clients++;
        return instance;
    }

    /**
     * A private constructor since this is a Singleton
     */
    private DBConnectionManager() {
        init();
    }

    /**
     * Returns a connection to the named pool.
     *
     * @param name The pool name as defined in the properties file
     * @param con The Connection
     */
    /*

	*/
    public void freeConnection(String name, Connection con) {
        DBConnectionPool pool = (DBConnectionPool) pools.get(name);
        if (pool != null) {
            pool.freeConnection(con);
        }
    }

    /**
     * Returns an open connection. If no one is available, and the max
     * number of connections has not been reached, a new connection is
     * created.
     *
     * @param name The pool name as defined in the properties file
     * @return Connection The connection or null
     */

	/*
	getConnection() 메소드는 사용하고자하는 커넥션풀의 이름을 파라미터로 입력받은 후 그 이름을 사용하여
	Hashtable(pools 필드)로 부터 필요한 DBHandler객체를 일어온다. DBHandler 클래스의 getDbConnection(String dbName)메소드를 호출하여
	connection객체를 리턴한다.


	*/
    public Connection getConnection(String name) {
        DBConnectionPool pool = (DBConnectionPool) pools.get(name);
        if (pool != null) {
            return pool.getConnection();
        }
        return null;
    }

    /**
     * Returns an open connection. If no one is available, and the max
     * number of connections has not been reached, a new connection is
     * created. If the max number has been reached, waits until one
     * is available or the specified time has elapsed.
     *
     * @param name The pool name as defined in the properties file
     * @param time The number of milliseconds to wait
     * @return Connection The connection or null
     */
    public Connection getConnection(String name, long time) {
        DBConnectionPool pool = (DBConnectionPool) pools.get(name);
        if (pool != null) {
            return pool.getConnection(time);
        }
        return null;
    }

    /**
     * Closes all open connections and deregisters all drivers.
     */
   /*
    public synchronized void release() {
        // Wait until called by the last client
        if (--clients != 0) {
            return;
        }

        Enumeration allPools = pools.elements();
        while (allPools.hasMoreElements()) {
            DBConnectionPool pool = (DBConnectionPool) allPools.nextElement();
            pool.release();
        }
        Enumeration allDrivers = drivers.elements();
        while (allDrivers.hasMoreElements()) {
            Driver driver = (Driver) allDrivers.nextElement();
            try {
                DriverManager.deregisterDriver(driver);
                log("Deregistered JDBC driver " + driver.getClass().getName());
            }
            catch (SQLException e) {
                log(e, "Can't deregister JDBC driver: " + driver.getClass().getName());
            }
        }
    }
   */
	 /*
	 ConnectionPool객체들을 만든다.
	 loadDrivers(dbProps)메소드를 사용하여 필요한 jdbc드라이버를 등록하면 createPools()메소드를 사용하여 데이터베이스커넥션풀을 생성한다.
	 이때 DBHandler클래스를 통해서 생성한다.
	 pools는 DBHandler(커넥션풀) 객체를 저장하는 Hashtable이다. 각각의 DBHandler(커넥션풀) 객체는 설정파일의 [이름]부분에 명시된 값을 통해서 구분된다.
	  이 이름을 키값으로 사용하여 Hashtable에 DBHandler 객체를 저장한다.
	  createPools() 메소드가 실행이 완료되면 DBConnectionManager클래스를 사용하여 연결하려는 데이터베이스에 대한 Connection객체를 구할수있는데
	  Connection객체는 DBConnectionManager클래스의 getConnection()메소드를 사용하여 구한다.116라인 참조

	 */
    private void createPools(Properties props) {
        Enumeration propNames = props.propertyNames();
        while (propNames.hasMoreElements()) {
            String name = (String) propNames.nextElement();
            if (name.endsWith(".url")) {
                String poolName = name.substring(0, name.lastIndexOf("."));
                String url = props.getProperty(poolName + ".url");
                if (url == null) {
                    log("No URL specified for " + poolName);
                    continue;
                }
                String user = props.getProperty(poolName + ".user");
                String password = props.getProperty(poolName + ".password");
                String maxconn = props.getProperty(poolName + ".maxconn", "0");
                int max;
                try {
                    max = Integer.valueOf(maxconn).intValue();
                }
                catch (NumberFormatException e) {
                    log("Invalid maxconn value " + maxconn + " for " + poolName);
                    max = 0;
                }
                DBConnectionPool pool =
                        new DBConnectionPool(poolName, url, user, password, max);
                pools.put(poolName, pool);
                log("Initialized pool " + poolName);
            }
        }
    }


    /*
    load(InputStream)클래스는 입력 스트림으로부터 [속성이름]=[속성값]형태의 속성을 읽어올수 있도록 해준다.
    이 메소드를 사용하여 설정 파일로부터 속성값을 읽어온다.
    init()메소드는 설정파일로부터 속성값을 읽어온후 loadDrivers(dbProps) 메소드와
    createPools(dbProps); 메소드를 차례로 호출한다.loadDrivers(dbProps)는 loadDrivers(dbProps)설정파일의
    drivers속성으로 부터 사용할 jdbc드라이버클래스를 로딩한후 DriverManager.registerDriver(driver);메소드를 사용하여
    사용가능한 JDBC드라이버를 등록한다.  loadDrivers(dbProps)메소드는 JDBC드라이버를 찾을수없거나 또는 등록 하지 못하는
    경우 예외를 발생한다.(222 ~ 270 라인까지의 주석)
         */
    private void init() {
        InputStream is = getClass().getResourceAsStream("/db.properties");
        Properties dbProps = new Properties();
        try {
            dbProps.load(is);
        }
        catch (Exception e) {
            System.err.println("Can't read the properties file. " +
                    "Make sure db.properties is in the CLASSPATH");
            return;
        }
        String logFile = dbProps.getProperty("logfile", "DBConnectionManager.log");
        try {
            log = new PrintWriter(new FileWriter(logFile, true), true);
        }
        catch (IOException e) {
            System.err.println("Can't open the log file: " + logFile);
            log = new PrintWriter(System.err);
        }
        loadDrivers(dbProps);
        createPools(dbProps);
    }

    /**
     loadDrivers()는 StringTokenizer를 사용해서 drivers속성값을 각 드라이버 클래스 이름의 String으로 나눈다음 모든 클래스이름에 대해 루프를 돈다.
     각 클래스들을 jvm에 로드학고 인스턴스를 만들고 JDBC DriverManager의 인스턴스를 등혹한다음 이것을 drivers Vector에 추가한다.
     driver Vector는 셧다운으로 DriverManager에서 모든 드라이버 등록을 지우기 위해 햐용된다
     */

    private void loadDrivers(Properties props) {
        String driverClasses = props.getProperty("drivers");
        StringTokenizer st = new StringTokenizer(driverClasses);
        while (st.hasMoreElements()) {
            String driverClassName = st.nextToken().trim();
            try {
                Driver driver = (Driver)
                        Class.forName(driverClassName).newInstance();
                DriverManager.registerDriver(driver);
                drivers.addElement(driver);
                log("Registered JDBC driver " + driverClassName);
            }
            catch (Exception e) {
                log("Can't register JDBC driver: " +
                        driverClassName + ", Exception: " + e);
            }
        }
    }

    /**
     * Writes a message to the log file.
     */
    private void log(String msg) {
        log.println(new Date() + ": " + msg);
    }

    /**
     * Writes a message with an Exception to the log file.
     */
    private void log(Throwable e, String msg) {
        log.println(new Date() + ": " + msg);
        e.printStackTrace(log);
    }

    /**
     * This inner class represents a connection pool. It creates new
     * connections on demand, up to a max number if specified.
     * It also makes sure a connection is still open before it is
     * returned to a client.
     */
    class DBConnectionPool {
        private int checkedOut;
        private Vector freeConnections = new Vector();
        private int maxConn;
        private String name;
        private String password;
        private String URL;
        private String user;

        /**
         * Creates new connection pool.
         *
         * @param name The pool name
         * @param URL The JDBC URL for the database
         * @param user The database user, or null
         * @param password The database user password, or null
         * @param maxConn The maximal number of connections, or 0
         *   for no limit
         */
        public DBConnectionPool(String name, String URL, String user, String password,
                                int maxConn) {
            this.name = name;
            this.URL = URL;
            this.user = user;
            this.password = password;
            this.maxConn = maxConn;
        }

        /**
         * Checks in a connection to the pool. Notify other Threads that
         * may be waiting for a connection.
         *
         * @param con The connection to check in
         */
        public synchronized void freeConnection(Connection con) {
            // Put the connection at the end of the Vector
            freeConnections.addElement(con);
            checkedOut--;
            notifyAll();
        }

        /**
         * Checks out a connection from the pool. If no free connection
         * is available, a new connection is created unless the max
         * number of connections has been reached. If a free connection
         * has been closed by the database, it's removed from the pool
         * and this method is called again recursively.
         */
        public synchronized Connection getConnection() {
            Connection con = null;
            if (freeConnections.size() > 0) {
                // Pick the first Connection in the Vector
                // to get round-robin usage
                con = (Connection) freeConnections.firstElement();
                freeConnections.removeElementAt(0);
                try {
                    if (con.isClosed()) {
                        log("Removed bad connection from " + name);
                        // Try again recursively
                        con = getConnection();
                    }
                }
                catch (SQLException e) {
                    log("Removed bad connection from " + name);
                    // Try again recursively
                    con = getConnection();
                }
            }
            else if (maxConn == 0 || checkedOut < maxConn) {
                con = newConnection();
            }
            if (con != null) {
                checkedOut++;
            }
            return con;
        }

        /**
         * Checks out a connection from the pool. If no free connection
         * is available, a new connection is created unless the max
         * number of connections has been reached. If a free connection
         * has been closed by the database, it's removed from the pool
         * and this method is called again recursively.
         * <P>
         * If no connection is available and the max number has been
         * reached, this method waits the specified time for one to be
         * checked in.
         *
         * @param timeout The timeout value in milliseconds
         */
        public synchronized Connection getConnection(long timeout) {
            long startTime = new Date().getTime();
            Connection con;
            while ((con = getConnection()) == null) {
                try {
                    wait(timeout);
                }
                catch (InterruptedException e) {}
                if ((new Date().getTime() - startTime) >= timeout) {
                    // Timeout has expired
                    return null;
                }
            }
            return con;
        }

        /**
         * Closes all available connections.
         */
       /*
	    public synchronized void release() {
            Enumeration allConnections = freeConnections.elements();
            while (allConnections.hasMoreElements()) {
                Connection con = (Connection) allConnections.nextElement();
                try {
                    con.close();
                    log("Closed connection for pool " + name);
                }
                catch (SQLException e) {
                    log(e, "Can't close connection for pool " + name);
                }
            }
            freeConnections.removeAllElements();
        }
        */
        /**
         * Creates a new connection, using a userid and password
         * if specified.
         */
        private Connection newConnection() {
            Connection con = null;
            try {
                if (user == null) {
                    con = DriverManager.getConnection(URL);
                }
                else {
                    con = DriverManager.getConnection(URL, user, password);
                }
                log("Created a new connection in pool " + name);
            }
            catch (SQLException e) {
                log(e, "Can't create a new connection for " + URL);
                return null;
            }
            return con;
        }
    }
}
