/**
 * Created by Martin Sandig on 10.08.2015
 * This is an fork of the "orientdb-liquibase" project by young-druid
 * Link: https://github.com/young-druid/liquibase-orientdb
 */
package com.orientechnologies.orient.jdbc;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Properties;

import com.orientechnologies.orient.jdbc.OrientJdbcConnection;

public class LiquibaseOrientJdbcConnection extends OrientJdbcConnection {
	
	
	public LiquibaseOrientJdbcConnection(String iUrl, Properties iInfo) {
		super(iUrl, iInfo);
	}
	
	/**
	 * Returns the current database meta data
	 */
	public DatabaseMetaData getMetaData() throws SQLException {
		return new LiquibaseOrientJdbcDatabaseMetaData(this, getDatabase());
	}
}
