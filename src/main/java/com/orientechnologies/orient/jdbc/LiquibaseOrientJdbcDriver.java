/**
 * Created by Martin Sandig on 10.08.2015
 * This is an fork of the "orientdb-liquibase" project by young-druid
 * Link: https://github.com/young-druid/liquibase-orientdb
 */
package com.orientechnologies.orient.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import com.orientechnologies.orient.jdbc.OrientJdbcDriver;

public class LiquibaseOrientJdbcDriver extends OrientJdbcDriver {
	/**
	 * Connect to a new OrientDB
	 */
	public Connection connect(String url, Properties info) throws SQLException {
		return new LiquibaseOrientJdbcConnection(url, info);
	}

}
