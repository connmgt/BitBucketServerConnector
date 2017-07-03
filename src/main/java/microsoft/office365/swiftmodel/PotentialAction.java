/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package microsoft.office365.swiftmodel;
import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 *
 * @author srhebbar
 */
public class PotentialAction
{
    @SerializedName("@context") private String context;
    
    @SerializedName("@type") private String type;
    
    private String name;

    private List<String> target;

    public PotentialAction(List<String> url) {
        this.context = "http://schema.org";
        this.type = "ViewAction";
        this.name = "View Build";
        this.target = url;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public List<String> getTarget ()
    {
        return target;
    }

    public void setTarget (List<String> target)
    {
        this.target = target;
    }
    
    public String getContext()
    {
        return this.context;
    }
    
    public void setContext(String context)
    {
        this.context = context;
    }
    
    public String getType()
    {
        return this.type;
    }
    
    public void setType(String type)
    {
        this.type = type;
    }
}

