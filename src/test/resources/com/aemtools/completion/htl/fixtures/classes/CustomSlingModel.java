package com.aemtools.completion.htl.fixtures.classes;

import com.aemtools.completion.htl.fixtures.classes.MyModel;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import java.util.List;
import java.util.Map;

/**
 * Documentation of CustomSlingModel.
 */
@Model(adaptables = Resource.class)
public class CustomSlingModel {

    /**
     * Documentation for publicString.
     */
    public String publicString;
    /**
     * Documentation for publicStringArray.
     */
    public String[] publicStringArray;
    /**
     * Documentation for publicStringList.
     */
    public List<String> publicStringList;
    /**
     * Documentation for publicStringMap.
     */
    public Map<String, String> publicStringMap;
    /**
     * Documentation for publicBoolean.
     */
    public boolean publicBoolean;

    private String stringField;
    private String[] stringArray;
    private List<String> stringList;
    private Map<String, String> stringMap;
    private boolean booleanField;

    private List<MyModel> modelList;

    public List<MyModel> getModelList() {
        return modelList;
    }

    /**
     * Documentation for getStringField.
     *
     * @return the stringField
     */
    public String getStringField() {
        return stringField;
    }

    /**
     * Documentation for getStringArray.
     * @return the stringArray
     */
    public String[] getStringArray() {
        return stringArray;
    }

    /**
     * Documentation for getStringList.
     * @return the stringList
     */
    public List<String> getStringList() {
        return stringList;
    }

    /**
     * Documentation for getStringMap.
     * @return the stringMap
     */
    public Map<String, String> getStringMap() {
        return stringMap;
    }

    /**
     * Documentation for getBooleanField.
     * @return the booleanField
     */
    public boolean getBooleanField() {
        return booleanField;
    }

    /**
     * Documentation for isBooleanField.
     * @return the boolean field
     */
    public boolean isBooleanField() {
        return booleanField;
    }

}