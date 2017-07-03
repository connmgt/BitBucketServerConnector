/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package microsoft.office365.bitbucket.modelbuilder;

import com.atlassian.bitbucket.event.pull.PullRequestEvent;
import microsoft.office365.bitbucket.modelbuilder.ModelBuilder;
import microsoft.office365.swiftmodel.Card;

/**
 *
 * @author srhebbar
 */
public class RepoPushModelBuilder extends ModelBuilder {

    public RepoPushModelBuilder(PullRequestEvent pullRequest) {
        super(pullRequest);
    }

    @Override
    public Card Build() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
