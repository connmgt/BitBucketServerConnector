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
public class Card {
    private String summary;
    
    private String theme;
   
    private List<Sections> sections;
    
    private List<PotentialAction> potentialAction;

    public Card(String summary, List<Sections> sectionList) {
        this.summary = summary;
        this.theme = "#3479BF";
        this.sections = sectionList;
    }

    public Card(String summary, List<Sections> sections, List<PotentialAction> potentialActions) {
        this.summary = summary;
        this.theme = "#3479BF";
        this.sections = sections;
        this.potentialAction = potentialActions;
    }
    
    public String getSummary()
    {
        return summary;
    }
    
    public String getTheme()
    {
        return theme;
    }
    
    public List<PotentialAction> getPotentialAction()
    {
        return this.potentialAction;
    }
    
    public List<Sections> getSections()
    {
        return this.sections;
    }
    
    public void setSummary(String summary)
    {
        this.summary = summary;
    }
    
    public void setTheme(String theme)
    {
        this.theme = theme;
    }
    
    public void setPotentialAction(List<PotentialAction> pa)
    {
        this.potentialAction = pa;
    }
    
    public void setSections(List<Sections> sections)
    {
        this.sections = sections;
    }
}

