package tw.org.sevenflanks.cli.common;

public interface Handler {

    String name();

    boolean handle(String... args) throws Exception;

}
