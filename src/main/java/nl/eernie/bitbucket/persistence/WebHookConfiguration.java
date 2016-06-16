package nl.eernie.bitbucket.persistence;

import com.atlassian.bitbucket.repository.Repository;
import com.atlassian.bitbucket.repository.RepositoryService;
import net.java.ao.Accessor;
import net.java.ao.Entity;
import net.java.ao.Implementation;
import net.java.ao.Mutator;
import net.java.ao.Preload;
import net.java.ao.schema.Default;
import net.java.ao.schema.Ignore;
import net.java.ao.schema.NotNull;
import net.java.ao.schema.Table;

@Table("WHConfig")
@Preload
@Implementation(WebHookConfigurationImpl.class)
public interface WebHookConfiguration extends Entity
{
	@NotNull
	@Accessor("TITLE")
	String getTitle();

	@Mutator("TITLE")
	void setTitle(String title);

	@NotNull
	@Accessor("URL")
	String getURL();

	@Mutator("URL")
	void setURL(String URL);

	@NotNull
	@Accessor("REPO_ID")
	Integer getRepositoryId();

	@Mutator("REPO_ID")
	void setRepositoryId(Integer repoId);

	@NotNull
	@Default("true")
	@Accessor("IS_ENABLED")
	boolean isEnabled();

	@Mutator("IS_ENABLED")
	void setEnabled(boolean isEnabled);

	@Ignore
	Repository getRepository(RepositoryService rs);

	@Ignore
	void setRepository(Repository repo);
}
