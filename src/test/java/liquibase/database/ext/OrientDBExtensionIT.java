package liquibase.database.ext;

import liquibase.database.ext.rule.LiquibaseOrientDBRule;
import liquibase.database.ext.rule.OrientDBRule;
import liquibase.database.ext.rule.WithChangelog;

import org.junit.After;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

public class OrientDBExtensionIT {

	@ClassRule
	public static OrientDBRule oRule = new OrientDBRule("simple", "test", "test");
	@Rule
	public LiquibaseOrientDBRule lRule = new LiquibaseOrientDBRule(oRule.getDbUrl(), oRule.getDbUsername(),
			oRule.getDbPassword());

	@Test
	@WithChangelog("changelog.xml")
	public void testSimpleChangelog() {

	}

	@Test
	@WithChangelog("changelog1.xml")
	public void testChangelogWithTableCreation() {
	}

}
