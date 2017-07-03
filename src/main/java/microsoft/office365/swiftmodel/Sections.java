/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package microsoft.office365.swiftmodel;
import java.util.List;

/**
 *
 * @author srhebbar
 */
public class Sections
{
    private boolean markdown;

    private List<Facts> facts;

    private String activityTitle;

    private String activityText;

    private String activitySubtitle;

    public Sections(String activityTitle, String activitySubtitle, String activityText, List<Facts> factsList) {
        this.activityTitle = activityTitle;
        this.activitySubtitle = activitySubtitle;
        this.activityText = activityText;
        this.markdown = true;
        this.facts = factsList;
    }

    public boolean getMarkdown ()
    {
        return markdown;
    }

    public void setMarkdown (boolean markdown)
    {
        this.markdown = markdown;
    }

    public String getActivityTitle ()
    {
        return activityTitle;
    }

    public void setActivityTitle (String activityTitle)
    {
        this.activityTitle = activityTitle;
    }

    public List<Facts> getFacts ()
    {
        return facts;
    }
    
    public void setFacts(List<Facts> facts)
    {
        this.facts = facts;
    }

    public String getActivityText ()
    {
        return activityText;
    }

    public void setActivityText (String activityText)
    {
        this.activityText = activityText;
    }

    public String getActivitySubtitle ()
    {
        return activitySubtitle;
    }

    public void setActivitySubtitle (String activitySubtitle)
    {
        this.activitySubtitle = activitySubtitle;
    }
}

