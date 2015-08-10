package liquibase.database.ext.rule;

import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.metadata.security.ORole;
import com.orientechnologies.orient.core.metadata.security.OSecurity;
import com.orientechnologies.orient.core.metadata.security.OUser;
import org.junit.rules.ExternalResource;

import java.nio.file.Paths;

public class OrientDBRule extends ExternalResource {

    protected final String dbUrl;
    protected final String dbUsername;
    protected final String dbPassword;
    protected final boolean drop;

    public OrientDBRule(final String dbName, final String dbUsername, final String dbPassword) {
        this.dbUrl = String.format("plocal:%s", Paths.get(System.getProperty("db.path", "db"), dbName).
                toAbsolutePath().toString());
        this.dbUsername = System.getProperty("db.username", dbUsername);
        this.dbPassword = System.getProperty("db.username", dbPassword);
        this.drop = Boolean.valueOf(System.getProperty("db.drop", "true"));
    }

    @Override
    protected void before() throws Throwable {
        super.before();
        final ODatabaseDocumentTx database = new ODatabaseDocumentTx(dbUrl);
        if (!database.exists()) {
            database.create();
            final OSecurity sm = database.getMetadata().getSecurity();
            final OUser user = sm.createUser(this.dbUsername, this.dbPassword, ORole.ADMIN);
            database.setUser(user);
        }
    }

    @Override
    protected void after() {
        super.after();
        final ODatabaseDocumentTx database = new ODatabaseDocumentTx(dbUrl);
        if (database.exists()) {
            database.open(this.dbUsername, this.dbPassword);
            if (drop) {
            	database.close();
             //   database.drop();
            }
        }
    }

    public String getDbUrl() {
        return dbUrl;
    }

    public String getDbUsername() {
        return dbUsername;
    }

    public String getDbPassword() {
        return dbPassword;
    }
}
