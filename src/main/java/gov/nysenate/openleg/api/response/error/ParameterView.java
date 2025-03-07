package gov.nysenate.openleg.api.response.error;

import gov.nysenate.openleg.api.ViewObject;

public class ParameterView implements ViewObject {

    protected String name;
    protected String type;

    public ParameterView(String name, String type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public String getViewType() {
        return "parameter";
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
}
