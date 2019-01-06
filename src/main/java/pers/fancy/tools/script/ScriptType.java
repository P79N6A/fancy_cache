package pers.fancy.tools.script;


public enum ScriptType {

    Groovy("Groovy"),

    JavaScript("JavaScript"),

    Python("Python");

    ScriptType(String type) {
        this.type = type;
    }

    private String type;

    public String getValue() {
        return this.type;
    }
}
