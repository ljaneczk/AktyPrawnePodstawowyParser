import java.util.Vector;
import java.util.regex.Pattern;

public class TreeOperations {

    private static boolean isNodeTypeAboveArticle (NodeType type) {
        return (type == NodeType.plik || type == NodeType.dział || type == NodeType.rozdział || type == NodeType.tytuł);
    }

    public static void printVertexWithArticleRanges (Node root, String beginning) {
        String articlesRange = "";
        if(root.getLeftRange() != null && root.getRightRange() != null) {
            if(root.getLeftRange() != root.getRightRange())
                articlesRange = "  (Art. " + root.getLeftRange() + ". - " + root.getRightRange()+".)";
            else
                articlesRange = "  (Art. " + root.getLeftRange() + ".)";
        }
        root.printContentWithAddingArticlesRange(beginning,articlesRange);

    }

    public static void printAllVerticesRootedIn (Node root) {
        root.printContent();
        Vector <Node> Neighbours = root.getNeighbours();
        for (Node vertex : Neighbours)
            printAllVerticesRootedIn(vertex);
    }

    public static void updateTableOfContents (Node root) {
        Vector <Node> Neighbours = root.getNeighbours();
        if (Neighbours.isEmpty())
            return;
        NodeType rootType = root.getNodeType();
        for (Node vertex : Neighbours)
            updateTableOfContents(vertex);
        if(isNodeTypeAboveArticle(rootType)) {
            root.setLeftRangeAndRightRange();
            for (Node vertex : Neighbours)
                root.updateLeftRangeAndRightRange(vertex);
        }
    }

    public static void showTableOfContentsOfFile (Node root, String beginning) {
        NodeType rootType = root.getNodeType();
        if ( ! isNodeTypeAboveArticle(rootType))
            return;
        if (rootType != NodeType.plik) {
            printVertexWithArticleRanges(root,beginning);
            beginning = beginning + "  ";
        }
        Vector<Node> Neighbours = root.getNeighbours();
        for (Node vertex : Neighbours)
            showTableOfContentsOfFile(vertex,beginning);
    }

    public static boolean findTableOfContentsOfBranch (Node root, Number indexOfBranch) {;
        NodeType rootType = root.getNodeType();
        if ( ! isNodeTypeAboveArticle(rootType))
            return false;
        if (rootType == NodeType.dział && root.getNumber().isEqualTo(indexOfBranch)) {
            showTableOfContentsOfFile(root,"");
            return true;
        }
        Vector<Node> Neighbours = root.getNeighbours();
        for (Node vertex : Neighbours)
            if(findTableOfContentsOfBranch(vertex,indexOfBranch))
                return true;
        return false;
    }

    public static void showTableOfContestsOfBranch (Node root, Number indexOfBranch) throws Exception {
        try {
            if ( ! findTableOfContentsOfBranch(root,indexOfBranch))
                throw new Exception("Nie istnieje dział o danym numerze.");
        }
        catch (Exception exception) {
            throw exception;
        }
    }

    public static boolean findSelectedPath (Node root, String path) throws Exception {
        NodeType rootType = root.getNodeType();
        if ( Pattern.matches(findInputRegex(rootType),path)) {
            path = path.substring(5);
            Number number = new Number(path,root);
            if (! root.getNumber().isEqualTo(number))
                return false;
            int nextPartIndex = path.indexOf(',');
            if (nextPartIndex == -1) {
                nextPartIndex = path.length() - 2;
            }
            path = path.substring(nextPartIndex + 2);
            if (path.isEmpty()) {
                printAllVerticesRootedIn(root);
                return true;
            }
        }
        Vector <Node> Neighbours = root.getNeighbours();
        for (Node vertex : Neighbours)
            if(findSelectedPath(vertex,path))
                return true;
        return false;
    }

    public static void showContentOfSelectedPath (Node root, String path) throws Exception {
        try {
            if ( ! findSelectedPath(root,path))
                throw new Exception("Niepoprawna ścieżka tekstu do wypisania");
        }
        catch (Exception exception) {
            throw exception;
        }
    }

    public static void printSelectedArticles (Node root, Number firstArticle, Number lastArticle) {
        NodeType rootType = root.getNodeType();
        Number nodeNumber = root.getNumber();
        if ( rootType == NodeType.artykuł) {
            if ( nodeNumber.isNotLessThan(firstArticle) && nodeNumber.isNotGreaterThan(lastArticle)) {
                printAllVerticesRootedIn(root);
            }
        }
        Vector <Node> Neighbours = root.getNeighbours();
        for (Node vertex : Neighbours)
            printSelectedArticles(vertex,firstArticle,lastArticle);
    }

    public static void showContentOfRangeOfArticles (Node root, String path) throws Exception {
        if (!Pattern.matches(findInputRegex(NodeType.artykuł), path))
            throw new Exception("Podany napis nie jest zakresem artykułów");
        path = path.substring(5);
        Node nodeWithNodeTypeArticle = new Node(NodeType.artykuł);
        Number firstArticleNumber = new Number(path, nodeWithNodeTypeArticle);
        int beginIndex = path.indexOf('-');
        if (beginIndex == -1)
            throw new Exception("Podany napis nie jest zakresem artykułów");
        beginIndex = beginIndex + 2;
        path = path.substring(beginIndex);
        Number lastArticleNumber = new Number(path, nodeWithNodeTypeArticle);
        printSelectedArticles(root, firstArticleNumber, lastArticleNumber);
    }

    public static String findInputRegex (NodeType type) {
        switch (type) {
            case plik:
                return "^pli\\.(\\s)[\\d]+.*";
            case dział:
                return "^DZI\\.(\\s)['I'|'V'|'X'|'L'|'C'|'D'|'M']+[\\w]*.*";
            case rozdział:
                return "^Roz\\.(\\s)(([\\d]+)|(['I'|'V'|'X'|'L'|'C'|'D'|'M']+))[\\w]*\\..*";
            case tytuł:
                return "^[\\p{Lu}|\\p{Punct}|\\p{Space}]*";
            case artykuł:
                return "^Art\\.(\\s)[\\d]+[\\w]*\\..*";
            case ustęp:
                return "^ust\\.(\\s)[\\d]+[\\w]*\\..*";
            case punkt:
                return "^pun\\.(\\s)[\\d]+[\\w]*\\).*";
            case litera:
                return "^lit\\.(\\s)[\\w]+\\).*";
        }
        return type.toString();
    }
}
