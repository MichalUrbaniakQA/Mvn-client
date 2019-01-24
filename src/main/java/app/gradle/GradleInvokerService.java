package app.gradle;

public interface GradleInvokerService {

    void gradleBuild(final StringBuilder resultAppendString, final String detailsPath, final String commandFinal);
}
