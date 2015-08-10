/**
 * Created by Martin Sandig on 10.08.2015
 * This is an fork of the "orientdb-liquibase" project by young-druid
 * Link: https://github.com/young-druid/liquibase-orientdb
 */
package com.orientechnologies.orient.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.orientechnologies.orient.core.db.document.ODatabaseDocument;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OProperty;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.orientechnologies.orient.core.record.impl.ODocument;

public class LiquibaseOrientJdbcDatabaseMetaData extends OrientJdbcDatabaseMetaData {
	
	protected final static List<String> TABLE_TYPES = Arrays.asList("TABLE", "SYSTEM TABLE");
	private OrientJdbcConnection connection;
	private ODatabaseDocument database;

	public LiquibaseOrientJdbcDatabaseMetaData(OrientJdbcConnection iConnection, ODatabaseDocument iDatabase) {
		super(iConnection, iDatabase);
        this.connection = iConnection;
        this.database = iDatabase;
	}
	/**
	 * Returns SQL-Keywords for OrientDB and overwrites the current OrientJdbcDatabaseMetaData Method.
	 * The current getSQLKeywords returns null
	 */
	public String getSQLKeywords() throws SQLException {
        return "@rid,@class,@version,@size,@type,@this,CONTAINS,CONTAINSALL,CONTAINSKEY,CONTAINSVALUE,CONTAINSTEXT,"
                + "MATCHES,TRAVERSE";
    }
	/**
	 * Returns a ResultSet the Tables of the current database
	 */
    @Override
    public ResultSet getTables(String catalog, String schemaPattern, String tableNamePattern, String[] types) throws
            SQLException {
        if (isValidTableTypes(types)) {
            return super.getTables(catalog, schemaPattern, tableNamePattern, types);
        } else {
            return new OrientJdbcResultSet(new OrientJdbcStatement(this.connection),
                    Collections.<ODocument>emptyList(), 1003, 1007, 1);
        }
    }
	/**
	 * Check for an appropriate table type 
	 */
    private boolean isValidTableTypes(String[] types) {
        if (types != null) {
        	return Arrays.stream(types).anyMatch(type -> TABLE_TYPES.contains(type));
        }
        return true;
    }
	/**
	 * Returns a ResultSet with the TabelTypes of the current database
	 */
    public ResultSet getTableTypes() throws SQLException {
        OrientJdbcStatement stmt = new OrientJdbcStatement(this.connection);
        List<ODocument> records = new ArrayList<ODocument>();   
        TABLE_TYPES.stream().forEach(tableType -> records.add(new ODocument().field("TABLE_TYPE", tableType)));
        return new OrientJdbcResultSet(stmt, records, ResultSet.TYPE_FORWARD_ONLY, ResultSet
                .CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);
    }
	/**
	 * Returns a ResultSet with the Columns of the current database
	 */
    public ResultSet getColumns(final String catalog, final String schemaPattern, final String tableNamePattern,
                                final String columnNamePattern) throws SQLException {
        final List<ODocument> records = new ArrayList<ODocument>();
        final OClass clazz = database.getMetadata().getSchema().getClass(tableNamePattern);
        if (clazz != null) {
            if (columnNamePattern == null){
            	clazz.properties().stream().forEach(prop -> records.add(getPropertyAsDocument(clazz, prop)));
            } else {
                final OProperty prop = clazz.getProperty(columnNamePattern);
                if (prop != null) {
                    records.add(getPropertyAsDocument(clazz, prop));
                }
            }
        }
        return new OrientJdbcResultSet(new OrientJdbcStatement(connection), records, ResultSet.TYPE_FORWARD_ONLY,
                ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);
    }
    /**
     * Convert the "tables" of Liquibase to ODocuments for OrientDB 
     */
    private ODocument getPropertyAsDocument(final OClass clazz, final OProperty prop) {
        final OType type = prop.getType();
        return new ODocument().field("TABLE_CAT", database.getName())
                .field("TABLE_NAME", clazz.getName())
                .field("COLUMN_NAME", prop.getName())
                .field("DATA_TYPE", OrientJdbcResultSetMetaData.getSqlType(type))
                .field("TYPE_NAME", type.name())
                .field("COLUMN_SIZE", 1)
                .field("NULLABLE", !prop.isNotNull() ? columnNoNulls : columnNullable)
                .field("IS_NULLABLE", prop.isNotNull() ? "NO" : "YES");
    }  

}
