/**
 * Created by Martin Sandig on 10.08.2015
 * This is an fork of the "orientdb-liquibase" project by young-druid
 * Link: https://github.com/young-druid/liquibase-orientdb
 */
package liquibase.database.ext;

import liquibase.database.AbstractJdbcDatabase;
import liquibase.database.DatabaseConnection;
import liquibase.exception.DatabaseException;

public class OrientDBDatabase extends AbstractJdbcDatabase {

    public static final String DB_NAME = "OrientDB";

    public static String DEFAULT_DRIVER = "com.orientechnologies.orient.jdbc.OrientJdbcDriver";

    public OrientDBDatabase() {
        super.setCurrentDateTimeFunction("sysdate()");
    }

    @Override
    protected String getDefaultDatabaseProductName() {
        return DB_NAME;
    }

    @Override
    public boolean isCorrectDatabaseImplementation(DatabaseConnection databaseConnection) throws DatabaseException {
        return DB_NAME.equals(databaseConnection.getDatabaseProductName());
    }

    @Override
    public String getDefaultDriver(String s) {
        return DEFAULT_DRIVER;
    }

    @Override
    public String getShortName() {
        return DB_NAME;
    }

    @Override
    public Integer getDefaultPort() {
        return 2480;
    }

    @Override
    public boolean supportsInitiallyDeferrableColumns() {
        return false;
    }

    @Override
    public boolean supportsTablespaces() {
        return false;
    }

    @Override
    public int getPriority() {
        return PRIORITY_DEFAULT;
    }

    @Override
    public boolean supportsSchemas() {
        return false;
    }

    @Override
    public boolean supportsCatalogs() {
        return false;
    }

    @Override
    public String getCurrentDateTimeFunction() {
        return currentDateTimeFunction;
    }

}
