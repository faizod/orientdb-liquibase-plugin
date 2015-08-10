/**
 * Created by Martin Sandig on 10.08.2015
 * This is an fork of the "orientdb-liquibase" project by young-druid
 * Link: https://github.com/young-druid/liquibase-orientdb
 */
package liquibase.changelog;

import liquibase.database.Database;
import liquibase.database.ext.OrientDBDatabase;
import liquibase.exception.LiquibaseException;
import liquibase.executor.ExecutorService;
import liquibase.statement.core.GetNextChangeSetSequenceValueStatement;

import java.util.List;

public class OrientDBChangeLogHistoryService extends StandardChangeLogHistoryService {

	protected Integer lastChangeSetSequenceValue;

	@Override
	public int getPriority() {
		return PRIORITY_DATABASE;
	}

	/**
	 * Check if the current database is instance of OrientDBDatabase
	 */
	@Override
	public boolean supports(Database database) {
		return database instanceof OrientDBDatabase;
	}

	/**
	 * Returns the value of the next changeset in the changelog.xml
	 */
    @Override
    public int getNextSequenceValue() throws LiquibaseException {
        if (lastChangeSetSequenceValue == null) {
            if (getDatabase().getConnection() == null) {
                lastChangeSetSequenceValue = 0;
            } else {
                List result = ExecutorService.getInstance().getExecutor(getDatabase()).queryForList(new
                        GetNextChangeSetSequenceValueStatement(), Integer.class);
                lastChangeSetSequenceValue = result.isEmpty() ? 0 : (Integer) result.iterator().next();
            }
        }
        return ++lastChangeSetSequenceValue;
    }
}
