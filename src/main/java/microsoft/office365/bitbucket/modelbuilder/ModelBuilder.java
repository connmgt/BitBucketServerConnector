/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package microsoft.office365.bitbucket.modelbuilder;

import com.atlassian.bitbucket.event.pull.PullRequestEvent;
import com.atlassian.bitbucket.nav.NavBuilder;
import microsoft.office365.swiftmodel.Card;

/**
 *
 * @author srhebbar
 */
public abstract class ModelBuilder {

    protected PullRequestEvent Event;
    protected NavBuilder NavBuilder;
    
    ModelBuilder(PullRequestEvent event)
    {
        Event = event;
    }
    
    ModelBuilder(PullRequestEvent event,  NavBuilder navBuilder )
    {
        Event = event;
        NavBuilder = navBuilder;
    }
    
    /**
     *
     * @return
     */
    public abstract Card Build();
}
