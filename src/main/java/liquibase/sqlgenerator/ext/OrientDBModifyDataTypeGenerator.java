/**
 * Created by Martin Sandig on 10.08.2015
 * This is an fork of the "orientdb-liquibase" project by young-druid
 * Link: https://github.com/young-druid/liquibase-orientdb
 */
package liquibase.sqlgenerator.ext;

import liquibase.database.Database;
import liquibase.database.ext.OrientDBDatabase;
import liquibase.datatype.DataTypeFactory;
import liquibase.exception.ValidationErrors;
import liquibase.sql.Sql;
import liquibase.sql.UnparsedSql;
import liquibase.sqlgenerator.SqlGeneratorChain;
import liquibase.sqlgenerator.core.AbstractSqlGenerator;
import liquibase.statement.core.ModifyDataTypeStatement;
import liquibase.structure.core.Relation;
import liquibase.structure.core.Table;

public class OrientDBModifyDataTypeGenerator extends AbstractSqlGenerator<ModifyDataTypeStatement> {

	@Override
	public int getPriority() {
		return PRIORITY_DATABASE;
	}

	/**
	 * Check if database is supported
	 */
	@Override
	public boolean supports(ModifyDataTypeStatement statement, Database database) {
		return database instanceof OrientDBDatabase;
	}

	/**
	 * Check if "tableName" and "columnName" are required in the current statement
	 */
	@Override
	public ValidationErrors validate(ModifyDataTypeStatement statement, Database database,
			SqlGeneratorChain sqlGeneratorChain) {
		ValidationErrors validationErrors = new ValidationErrors();
		validationErrors.checkRequiredField("tableName", statement.getTableName());
		validationErrors.checkRequiredField("columnName", statement.getColumnName());
		return validationErrors;
	}
    /**
     * Generate the OrientDB-SQL statements to alter the classes and properties of the database for Liquibase.
     * Liquibase want to create "tables", but OrientDB has classes, because of that we have to modify the SQL-statements.
     */
	@Override
	public Sql[] generateSql(ModifyDataTypeStatement statement, Database database,
			SqlGeneratorChain sqlGeneratorChain) {
		String alterTable = "ALTER PROPERTY " + database.escapeTableName(statement.getCatalogName(),
				statement.getSchemaName(), statement.getTableName()) + ".";

		alterTable += database.escapeColumnName(statement.getCatalogName(), statement.getSchemaName(),
				statement.getTableName(), statement.getColumnName());

		alterTable += " TYPE ";

		// add column type
		alterTable += DataTypeFactory.getInstance().fromDescription(statement.getNewDataType(), database)
				.toDatabaseDataType(database);

		return new Sql[] { new UnparsedSql(alterTable, getAffectedTable(statement)) };
	}

	protected Relation getAffectedTable(ModifyDataTypeStatement statement) {
		return new Table().setName(statement.getTableName()).setSchema(statement.getCatalogName(),
				statement.getSchemaName());
	}

}
