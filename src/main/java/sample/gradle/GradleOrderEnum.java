package sample.gradle;

public enum GradleOrderEnum {

    ASSEMBLE("assemble"),
    TEST("test"),
    CLEAN("clean"),
    HELP("-help"),
    INSTALL("install"),
    VERSION("-version");

    private String order;

    GradleOrderEnum(String order) {
        this.order = order;
    }
}
