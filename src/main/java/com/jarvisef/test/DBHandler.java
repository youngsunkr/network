/*
 DBHandler클래스의 주요역활은 데이터베이스에 연결된 사용가능한
 Connection객체를 저장해두었다가 필요에 따라서 Connection객체를 빌려주거나 반환받는것이다.
 또한 사용가능한 Connection객체가 없을 경우 동적으로 Connection객체를 생성하는 기능도 구현한다.

 DB pool로부터 접속을 얻고 돌려주는 과정을 encapsulation하여 계승할 수
 있도록 하는 상위 객체

 */

package com.jarvisef.test;

/**
 * Created by youngsunkr on 2015-06-10.
 */
import java.sql.Connection;
public class DBHandler {
    protected DBConnectionManager connMgr;
    protected Connection tibero;
    /**
     * DB Pool로부터 접속을 하나 얻어주는 메쏘드.
     *
     */
    public Connection getDbConnection(String dbName) {
        connMgr = DBConnectionManager.getInstance();
        tibero = connMgr.getConnection(dbName);
        return tibero;
    }
    /**
     * DB Pool에 접속을 돌려주는 메쏘드.
     *
     */
    public void freeDbConnection(String dbName) {
        connMgr.freeConnection(dbName, tibero);
    }
}
