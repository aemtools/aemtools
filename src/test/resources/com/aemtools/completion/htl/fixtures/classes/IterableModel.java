package com.aemtools.completion.htl.fixtures.classes;

import org.apache.sling.models.annotations.Model;
import com.aemtools.completion.htl.fixtures.classes.MyModel;
import com.aemtools.completion.htl.fixtures.classes.MapModel;

import java.util.List;
import java.util.Map;

/**
 * Documentation of IterableModel.
 */
@Model(adaptables = Resource.class)
public class IterableModel {

    public MyModel[] fieldModelArray;
    public List<MyModel> fieldModelList;
    public Map<MyModel, MapModel> fieldModelMap;

    public MyModel[] getMethodModelArray() {
        return null;
    }

    public List<MyModel> getMethodModelList() {
        return null;
    }

    public Map<MyModel, MapModel> getMethodModelMap() {
        return null;
    }

}