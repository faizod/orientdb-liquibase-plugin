/**
 * Created by Martin Sandig on 10.08.2015
 * This is an fork of the "orientdb-liquibase" project by young-druid
 * Link: https://github.com/young-druid/liquibase-orientdb
 */
package liquibase.datatype;

import liquibase.database.Database;
import liquibase.database.ext.OrientDBDatabase;
import liquibase.datatype.core.IntType;

@DataTypeInfo(name = "int", aliases = {"integer", "java.sql.Types.INTEGER", "java.lang.Integer", "serial"},
        minParameters = 0, maxParameters = 1, priority = LiquibaseDataType.PRIORITY_DATABASE)
public class IntegerDataType extends IntType {

    @Override
    public boolean supports(Database database) {
        return database instanceof OrientDBDatabase;
    }

    @Override
    public DatabaseDataType toDatabaseDataType(Database database) {
        return new DatabaseDataType("integer");
    }

}
