/**
 * Created by Martin Sandig on 10.08.2015
 * This is an fork of the "orientdb-liquibase" project by young-druid
 * Link: https://github.com/young-druid/liquibase-orientdb
 */
package liquibase.lockservice.ext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.orientechnologies.orient.core.exception.OQueryParsingException;

import liquibase.database.Database;
import liquibase.database.ext.OrientDBDatabase;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.exception.UnexpectedLiquibaseException;
import liquibase.executor.Executor;
import liquibase.executor.ExecutorService;
import liquibase.lockservice.StandardLockService;
import liquibase.statement.core.RawSqlStatement;

public class OrientDBLockService extends StandardLockService {

	protected Database database;
	
	private static final Logger LOG = LoggerFactory.getLogger(OrientDBLockService.class);
	
	@Override
	public int getPriority() {
		return PRIORITY_DATABASE;
	}

	/**
	 * Check if the database is supported
	 */
	@Override
	public boolean supports(Database database) {
		return database instanceof OrientDBDatabase;
	}

	@Override
	public void setDatabase(Database database) {
		super.setDatabase(database);
		this.database = database;
	}

	/**
	 * Check if the Class "DATABASECHANGELOGLOCK" is initialized
	 */
	@Override
	public boolean isDatabaseChangeLogLockTableInitialized(boolean tableJustCreated) throws DatabaseException {
		boolean initialized;
		Executor executor = ExecutorService.getInstance().getExecutor(database);
		try {
			initialized = executor.queryForLong(new RawSqlStatement(
					"select count(*) from " + database.escapeTableName(database.getLiquibaseCatalogName(),
							database.getLiquibaseSchemaName(), database.getDatabaseChangeLogLockTableName()))) > 0;
		} catch (LiquibaseException e) {
			if (executor.updatesDatabase()) {
				throw new UnexpectedLiquibaseException(e);
			} else {
				// probably didn't actually create the table yet.

				initialized = !tableJustCreated;
			}
		}
		return initialized;
	}
	
	/**
	 * Check if the database has a "DATABASECHANGELOGLOCK" Class.
	 * Problem: If the class does not exist, then an exception message is output
	 */
	@Override
	public boolean hasDatabaseChangeLogLockTable() throws DatabaseException{
		Boolean hasTable = false;
		Executor executor = ExecutorService.getInstance().getExecutor(database);
			try {
				hasTable = executor.queryForLong(new RawSqlStatement(
						"select count(*) from " + database.escapeTableName(database.getLiquibaseCatalogName(),
								database.getLiquibaseSchemaName(), database.getDatabaseChangeLogLockTableName()))) > 0;

			} catch (LiquibaseException le) {
				LOG.info("Class DATABASECHANGELOGLOCK could not found! Creating class DATABASECHANGELOGLOCK...");
			} catch (OQueryParsingException oqpe) {
				LOG.info("Class DATABASECHANGELOGLOCK could not found! Creating class DATABASECHANGELOGLOCK...");
			} 
		
		return hasTable;

	}


}
