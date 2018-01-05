import java.util.Vector;
import java.util.regex.Pattern;

public class TreeOperations {
    private static void printVertex (Node root) {
        if (root == null)
            return;
        System.out.println("Visit:  "+root.getNodeType()+"  "+root.getNumber()+"   "+root.getBegin()+"   "+root.getEnd());
        Vector <Node> Neighbours = root.getNeighbours();
        System.out.println("Neigh.size() = "+Neighbours.size());
        root.printContent();
        System.out.println("\n");
    }

    public static void deepFirstSearch (Node root) {
        //printVertex(root);
        Vector <Node> Neighbours = root.getNeighbours();
        //System.out.println("Neigh.size() = "+Neighbours.size());
        for (Node vertex : Neighbours)
            deepFirstSearch(vertex);
    }

    public static void updateTableOfContent (Node root) {
        Vector <Node> Neighbours = root.getNeighbours();
        if (Neighbours.isEmpty())
            return;
        NodeType rootType = root.getNodeType();
        for (Node vertex : Neighbours)
            updateTableOfContent(vertex);
        if(isNodeTypeAboveArticle(rootType)) {
            root.setBeginAndEnd();
            //System.out.println(root.getBegin()+"  "+root.getEnd());
            for (Node vertex : Neighbours)
                root.updateBeginAndEnd(vertex);
        }
        printVertex(root);
    }

    public static void showContentOfSelectedPath (Node root, String path) throws Exception {
        try {
            NodeType rootType = root.getNodeType();
            //System.out.println(root+"   "+rootType+"   "+path);
            boolean pathWasEmpty = path.isEmpty();
            if (path.isEmpty()) {
                root.printContent();
            }
            if (isNodeTypeAboveArticle(rootType) || path.isEmpty()) {
                Vector <Node> Neighbours = root.getNeighbours();
                for (Node vertex : Neighbours)
                    showContentOfSelectedPath(vertex,path);
                return;
            }
            if ( ! Pattern.matches(findInputRegex(rootType),path))
                throw new Exception("Niepoprawna ścieżka tekstu do wypisania\n");
            path = path.substring(5);
            Number number = new Number(path,root);
            if (root.getNumber().isEqualTo(number)) {
                int begin = path.indexOf(',');
                if (begin == -1)
                    begin = path.length()-2;
                path = path.substring(begin+2);
                System.out.println(root+ " "+rootType+" "+path+" "+number);
                if ( !pathWasEmpty && path.isEmpty())
                    root.printContent();
                Vector <Node> Neighbours = root.getNeighbours();
                for (Node vertex : Neighbours)
                    showContentOfSelectedPath(vertex,path);
            }
        }
        catch (Exception e) {
            throw e;
        }
    }

    public static void showWholeContentOfFile (Node root, String beginning) {
        NodeType rootType = root.getNodeType();
        if (isNodeTypeAboveArticle(rootType)) {
            if (rootType != NodeType.plik) {
                root.printContentWithoutEnter(beginning);
                if(root.getBegin() != null && root.getEnd() != null)
                    System.out.println("  Art. " + root.getBegin() + '-' + root.getEnd());
                else
                    System.out.println("");
            }
            beginning = new String(beginning + "  ");
            Vector<Node> Neighbours = root.getNeighbours();
            for (Node vertex : Neighbours)
                showWholeContentOfFile(vertex,beginning);
        }
    }

    private static boolean isNodeTypeAboveArticle (NodeType type) {
        return (type == NodeType.plik || type == NodeType.dział || type == NodeType.rozdział || type == NodeType.podrozdział);
    }

    public static String findInputRegex (NodeType type) {
        switch (type) {
            case plik:
                return "^pli\\.(\\s)[\\d]+.*";
            case dział:
                return "^DZI\\.(\\s)['I'|'V'|'X'|'L'|'C'|'D'|'M']+[\\w]*.*";
            case rozdział:
                return "^Roz\\.(\\s)[\\d]+[\\w]*\\..*";
            case podrozdział:
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
