/**
 * Created by Martin Sandig on 10.08.2015
 * This is an fork of the "orientdb-liquibase" project by young-druid
 * Link: https://github.com/young-druid/liquibase-orientdb
 */
package liquibase.sqlgenerator.ext;

import liquibase.database.Database;
import liquibase.database.ext.OrientDBDatabase;
import liquibase.datatype.LiquibaseDataType;
import liquibase.sql.Sql;
import liquibase.sql.UnparsedSql;
import liquibase.sqlgenerator.SqlGeneratorChain;
import liquibase.sqlgenerator.core.CreateTableGenerator;
import liquibase.statement.UniqueConstraint;
import liquibase.statement.core.CreateTableStatement;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class OrientDBCreateTableGenerator extends CreateTableGenerator {

    @Override
    public int getPriority() {
        return PRIORITY_DATABASE;
    }
	/**
	 * Check if the database is supported
	 */
    @Override
    public boolean supports(CreateTableStatement statement, Database database) {
        return database instanceof OrientDBDatabase;
    }
    /**
     * Generate the OrientDB-SQL statements to create the classes and properties of the database for Liquibase.
     * Liquibase want to create normaly "tables", but OrientDB has classes, because of that we have to modify the SQL-statements.
     */
    @Override
    public Sql[] generateSql(CreateTableStatement statement, Database database, SqlGeneratorChain sqlGeneratorChain) {
        List<Sql> sqls = new ArrayList<Sql>();
        sqls.add(new UnparsedSql("CREATE CLASS " + statement.getTableName(), getAffectedTable(statement)));
        Map<String, LiquibaseDataType> columnTypes = statement.getColumnTypes();
        statement.getColumns().stream().forEach(column -> sqls.add(new UnparsedSql("CREATE PROPERTY " + statement.getTableName() + "." + column + " " + columnTypes
                    .get(column).toDatabaseDataType(database).toSql(), getAffectedTable(statement))));
        
        for (UniqueConstraint constraint : statement.getUniqueConstraints()) {
            final StringBuilder columns = new StringBuilder("(");
            for (Iterator<String> iterator = constraint.getColumns().iterator(); iterator.hasNext(); ) {
                columns.append(iterator.next());
                if (iterator.hasNext()) {
                    columns.append(", ");
                }
            }
            columns.append(")");
            sqls.add(new UnparsedSql("CREATE INDEX " + constraint.getConstraintName() + " ON " + getAffectedTable
                    (statement) + " " + columns.toString() + " UNIQUE"));
        }
        return sqls.toArray(new Sql[sqls.size()]);
    }
}
