package liquibase.database.ext.rule;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.exception.DatabaseException;
import liquibase.integration.commandline.CommandLineUtils;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.resource.ResourceAccessor;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.net.URISyntaxException;
import java.nio.file.Paths;

public class LiquibaseOrientDBRule implements TestRule {

    protected final ResourceAccessor resourceAccessor;
    protected final String dbUsername;
    protected final String dbUrl;
    protected final String dbPassword;

    public LiquibaseOrientDBRule(String dbUrl, String dbUsername, String dbPassword) {
        this.resourceAccessor = new ClassLoaderResourceAccessor(this.getClass().getClassLoader());
        this.dbUrl = dbUrl;
        this.dbUsername = dbUsername;
        this.dbPassword = dbPassword;
    }

    @Override
    public Statement apply(final Statement base, final Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                final WithChangelog withChangelog = description.getAnnotation(WithChangelog.class);
                if (withChangelog != null) {
                    final Liquibase liquibase = new Liquibase(withChangelog.value(), resourceAccessor, createDatabase(dbUrl,
                            dbUsername, dbPassword));
                    liquibase.update((String) null);
                } 
                base.evaluate();
            }
        };
    }

    @SuppressWarnings("deprecation")
	private Database createDatabase(final String dbUrl, final String dbUsername, final String dbPassword) throws
            DatabaseException {
        return CommandLineUtils.createDatabaseObject(getClass().getClassLoader(), dbUrl, dbUsername, dbPassword, "com" +
                        ".orientechnologies.orient.jdbc.LiquibaseOrientJdbcDriver", null, "liquibase", false, false,
                "liquibase.database.ext.OrientDBDatabase", null, null, null, null, null, null);
    }

    

}
