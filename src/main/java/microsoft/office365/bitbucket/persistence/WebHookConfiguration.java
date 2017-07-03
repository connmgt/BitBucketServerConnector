package microsoft.office365.bitbucket.persistence;

import net.java.ao.Accessor;
import net.java.ao.Entity;
import net.java.ao.Mutator;
import net.java.ao.Preload;
import net.java.ao.schema.Default;
import net.java.ao.schema.NotNull;
import net.java.ao.schema.StringLength;
import net.java.ao.schema.Table;

@Table("WHConfig")
@Preload
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
	@StringLength(StringLength.UNLIMITED)
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
}
