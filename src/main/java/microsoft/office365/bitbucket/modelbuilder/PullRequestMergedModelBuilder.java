/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package microsoft.office365.bitbucket.modelbuilder;

import com.atlassian.bitbucket.event.pull.PullRequestEvent;
import com.atlassian.bitbucket.nav.NavBuilder;
import com.atlassian.bitbucket.pull.PullRequest;
import com.atlassian.bitbucket.user.ApplicationUser;
import java.util.ArrayList;
import java.util.List;
import microsoft.office365.swiftmodel.Card;
import microsoft.office365.swiftmodel.Facts;
import microsoft.office365.swiftmodel.PotentialAction;
import microsoft.office365.swiftmodel.Sections;

/**
 *
 * @author srhebbar
 */
public class PullRequestMergedModelBuilder extends ModelBuilder {

    public PullRequestMergedModelBuilder(PullRequestEvent pullRequest) {
        super(pullRequest);
    }

    public PullRequestMergedModelBuilder(PullRequestEvent pullRequest, NavBuilder navBuilder) {
        super(pullRequest, navBuilder);
    }

    @Override
    public Card Build() {
        PullRequest pullRequest = Event.getPullRequest();
        ApplicationUser user = Event.getUser();
        String summary = "Pull request " + pullRequest.getId();
        String title = user.getDisplayName();
        List<Sections> sections = new ArrayList<>();
        String subTitle = "has merged pull request #" + pullRequest.getId() 
                + " in " + pullRequest.getFromRef().getDisplayId() + 
                " of " + pullRequest.getFromRef().getRepository().getName();
        String text = pullRequest.getId() + ": " + pullRequest.getTitle();
        List<Facts> factsList = new ArrayList<>();
        factsList.add(new Facts("Source Branch", pullRequest.getFromRef().getId()));
        factsList.add(new Facts("Destination Branch", pullRequest.getToRef().getId()));
        sections.add(new Sections(title, subTitle, text, factsList));
        String url = NavBuilder.repo(pullRequest.getToRef().getRepository()).pullRequest(pullRequest.getId()).buildAbsolute();
        List<String> urlList = new ArrayList<>();
        urlList.add(url);
        PotentialAction potentialAction = new PotentialAction(urlList);
        List<PotentialAction> potentialActions= new ArrayList<>();
        potentialActions.add(potentialAction);
        return new Card(summary, sections, potentialActions);
    }
    
}
