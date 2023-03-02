package com.strategy.api.base.model.http;

import java.util.List;

/**
 * Created on 6/12/18.
 */
public class UiEntityField {

    private String name;
    private boolean required;
    private boolean showInGrid;
    private UiFieldType type;
    private double min;
    private double max;
    private String regex;
    private String path;
    private String displayName;
    private String searchAttribute;
    private List<Object> values;

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public UiFieldType getType() {
        return type;
    }

    public void setType(UiFieldType type) {
        this.type = type;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isShowInGrid() {
        return showInGrid;
    }

    public void setShowInGrid(boolean showInGrid) {
        this.showInGrid = showInGrid;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getSearchAttribute() {
        return searchAttribute;
    }

    public void setSearchAttribute(String searchAttribute) {
        this.searchAttribute = searchAttribute;
    }

    public List<Object> getValues() {
        return values;
    }

    public void setValues(List<Object> values) {
        this.values = values;
    }
}
