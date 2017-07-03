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
public class Facts
{
    private String name;

    private String value;

    public Facts(String name, String value) {
        this.name = name;
        this.value = value;
    }
    
    public Facts(String name, int value) {
        this.name = name;
        this.value = Integer.toString(value);
    }

    public Facts(String name) {
        this.name = name;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getValue ()
    {
        return value;
    }

    public void setValue (String value)
    {
        this.value = value;
    }

    public void setValue(int number) {
        this.value = Integer.toString(number);
    }

    public void setValue(List<String> causesStrList) {
        for (String cause : causesStrList) {
            this.value += cause + ". ";
        }
    }
}

