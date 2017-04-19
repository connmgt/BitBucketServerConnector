package nl.topicus.bitbucket.events;

public enum EventType {
    
    PULL_REQUEST_CREATED("pullrequest:created", "PR_CREATED"),
    PULL_REQUEST_UPDATED("pullrequest:updated", "PR_UPDATED"),
    PULL_REQUEST_RESCOPED("pullrequest:updated", "PR_RESCOPED"),
    PULL_REQUEST_REOPENED("pullrequest:updated", "PR_REOPENED"),
    PULL_REQUEST_MERGED("pullrequest:fulfilled", "PR_MERGED"),
    PULL_REQUEST_DECLINED("pullrequest:rejected", "PR_DECLINED"),
    REPO_PUSH("repo:push", "REPO_PUSH"),
    TAG_CREATED("repo:push", "TAG_CREATED"),
    BRANCH_CREATED("repo:push", "BRANCH_CREATED"),
    BRANCH_DELETED("repo:push", "BRANCH_DELETED"),;

    private final String headerValue;
    private final String queryColumn;

    EventType(String headerValue, String queryColumn) {
        this.headerValue = headerValue;
        this.queryColumn = queryColumn;
    }

    public String getHeaderValue() {
        return headerValue;
    }

    public String getQueryColumn() {
        return queryColumn;
    }
}
