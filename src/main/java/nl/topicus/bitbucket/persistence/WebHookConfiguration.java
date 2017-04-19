package nl.topicus.bitbucket.persistence;

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

	@NotNull
	@Default("true")
	@Accessor("PR_CREATED")
	boolean isPrCreated();

	@Mutator("PR_CREATED")
	void setPrCreated(boolean isPrCreated);

	@NotNull
	@Default("true")
	@Accessor("PR_UPDATED")
	boolean isPrUpdated();

	@Mutator("PR_UPDATED")
	void setPrUpdated(boolean isPrUpdated);

	@NotNull
	@Default("true")
	@Accessor("PR_REOPENED")
	boolean isPrReopened();

	@Mutator("PR_REOPENED")
	void setPrReopened(boolean isPrReopened);

	@NotNull
	@Default("true")
	@Accessor("PR_MERGED")
	boolean isPrMerged();

	@Mutator("PR_MERGED")
	void setPrMerged(boolean isPrMerged);

	@NotNull
	@Default("true")
	@Accessor("PR_RESCOPED")
	boolean isPrRescoped();

	@Mutator("PR_RESCOPED")
	void setPrRescoped(boolean isPrRescoped);

	@NotNull
	@Default("true")
	@Accessor("PR_DECLINED")
	boolean isPrDeclined();

	@Mutator("PR_DECLINED")
	void setPrDeclined(boolean isPrDeclined);

	@NotNull
	@Default("true")
	@Accessor("REPO_PUSH")
	boolean isRepoPush();

	@Mutator("REPO_PUSH")
	void setRepoPush(boolean isRepoPush);

	@NotNull
	@Default("true")
	@Accessor("BRANCH_CREATED")
	boolean isBranchCreated();

	@Mutator("BRANCH_CREATED")
	void setBranchCreated(boolean isBranchCreated);

	@NotNull
	@Default("true")
	@Accessor("BRANCH_DELETED")
	boolean isBranchDeleted();

	@Mutator("BRANCH_DELETED")
	void setBranchDeleted(boolean isBranchDeleted);

	@NotNull
	@Default("false")
	@Accessor("TAG_CREATED")
	boolean isTagCreated();

	@Mutator("TAG_CREATED")
	void setTagCreated(boolean isTagCreated);
}
