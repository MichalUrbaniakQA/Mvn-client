package sample.maven;

public enum MvnOrderEnum {

    CLEAN("clean"),
    VALIDATE("validate"),
    COMPILE("compile"),
    TEST("TEST"),
    SKIP_TEST("-DskipTests"),
    PACKAGE("package"),
    VERIFY("verify"),
    INSTALL("install"),
    SITE("site"),
    DEPLOY("deploy"),
    VERSION("-version"),
    HELP("-help");

    private String order;

    MvnOrderEnum(String order) {
        this.order = order;
    }
}
