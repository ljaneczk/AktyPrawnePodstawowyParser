public interface InputParser {

    void removeHyphenation();

    boolean isBadLine (String Line);

    void removeUnnecessaryLines();

    boolean isArticleToSeparate (String Line);

    void separateArticlesAndContents();
}
