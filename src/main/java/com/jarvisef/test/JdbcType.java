package com.jarvisef.test;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by youngsunkr on 2015-10-26.
 */
public enum JdbcType {

    BIT(Types.BIT),
    TINYINT(Types.TINYINT),
    SMALLINT(Types.SMALLINT),
    INTEGER(Types.INTEGER),
    BIGINT(Types.BIGINT),
    FLOAT(Types.FLOAT),
    REAL(Types.REAL),
    DOUBLE(Types.DOUBLE),
    NUMERIC(Types.NUMERIC),
    DECIMAL(Types.DECIMAL),
    CHAR(Types.CHAR),
    VARCHAR(Types.VARCHAR),
    LONGVARCHAR(Types.LONGVARCHAR),
    DATE(Types.DATE),
    TIME(Types.TIME),
    TIMESTAMP(Types.TIMESTAMP),
    BINARY(Types.BINARY),
    VARBINARY(Types.VARBINARY),
    LONGVARBINARY(Types.LONGVARBINARY),
    NULL(Types.NULL),
    OTHER(Types.OTHER),
    BLOB(Types.BLOB),
    CLOB(Types.CLOB),
    BOOLEAN(Types.BOOLEAN),
    CURSOR(-10), // Oracle
    TB_CURSOR(-17), // Oracle
    UNDEFINED(Integer.MIN_VALUE + 1000),
    NVARCHAR(-9),
    NCHAR(-15),
    NCLOB(2011);

    public final int TYPE_CODE;
    private static Map<Integer,JdbcType> codeLookup = new HashMap<Integer,JdbcType>();

    static {
        for (JdbcType type : JdbcType.values()) {
            codeLookup.put(type.TYPE_CODE, type);
        }
    }

    JdbcType(int code) {
        this.TYPE_CODE = code;
    }

    static JdbcType forCode(int code)  {
        return codeLookup.get(code);
    }

}
