import java.util.regex.Pattern;

public class StructureTree extends TreeOperations {
    private final Node Root;
    private String[] LinesOfFile;
    private int NumberOfLines;
    private boolean ThereWasEnterBetweenArticleAndContent;
    private Node[] VerticesOfGraph;

    StructureTree (InputText Text) {
        LinesOfFile = Text.getLinesOfFile();
        NumberOfLines = LinesOfFile.length;
        ThereWasEnterBetweenArticleAndContent = Text.getThereWasEnterBetweenArticleAndContent();
        Root = new Node(NodeType.plik);
        createVertices();
        buildTree(0,VerticesOfGraph.length-1,Root);
        updateTableOfContents(Root);
    }

    public Node getRoot() {
        return Root;
    }

    private void createVertices() {
        NodeType[] BeginningsOfNodeType = findBeginningsOfNodeType();
        VerticesOfGraph = new Node[NumberOfLines];
        for (int i = 0; i < NumberOfLines; i++)
            VerticesOfGraph[i] = null;

        for (int ind = 0; ind < NumberOfLines; ind++)
            if ( BeginningsOfNodeType[ind] != null) {
                VerticesOfGraph[ind] = new Node(BeginningsOfNodeType[ind]);
                int end = ind;
                while (end+1 < NumberOfLines && BeginningsOfNodeType[end+1] == null)
                    end++;
                addLinesToVertex(ind,end,VerticesOfGraph[ind]);
            }
        if(ThereWasEnterBetweenArticleAndContent)
            matchArticlesAndContent();
    }

    private NodeType[] findBeginningsOfNodeType () {
        NodeType[] BeginningOfNodeType = new NodeType[NumberOfLines];
        for (int i=0; i<NumberOfLines; i++)
            BeginningOfNodeType[i] = null;

        NodeType type = NodeType.tytuł;
        do {
            for (int i = 0; i < NumberOfLines; i++)
                if (Pattern.matches(NodeType.findRegex(type),LinesOfFile[i]))
                    BeginningOfNodeType[i] = type;
            type = type.findNextNodeType();
        }
        while (type != NodeType.tytuł);
        return BeginningOfNodeType;
    }

    private void addLinesToVertex (int begin, int end, Node vertex) {
        for (int i = begin; i <= end ; i++)
            vertex.addContentLine(LinesOfFile[i]);
    }

    private void matchArticlesAndContent() {
        assert (ThereWasEnterBetweenArticleAndContent);
        for (int ind = 1; ind < NumberOfLines; ind++) {
            Node PrevVertex = VerticesOfGraph[ind-1];
            Node NextVertex = VerticesOfGraph[ind];
            if ( PrevVertex != null && PrevVertex.getNodeType() == NodeType.artykuł) {
                if( NextVertex!=null)
                    makeCorrectionOfFirstArticleLine(PrevVertex,NextVertex,0);
                else
                    makeCorrectionOfFirstArticleLine(PrevVertex,PrevVertex,1);
            }
        }
    }

    private void makeCorrectionOfFirstArticleLine (Node PrevVertex, Node NextVertex, int indexInNextVertex) {
        String articleLine = PrevVertex.getContentLine(0);
        String firstContentLine = NextVertex.getContentLine(indexInNextVertex);
        articleLine = articleLine.substring(0, articleLine.lastIndexOf('.')+1);
        PrevVertex.removeContentLine(0);
        NextVertex.changeContentLine(0,articleLine+" "+firstContentLine);
    }


    private void buildTree(int begin, int end, Node root) {
        NodeType childType = root.getNodeType().findNextNodeType();
        String regex = NodeType.findRegex(childType);
        boolean thereExistsLineMatchingRegex = checkIfExistsLineMatchingRegex(begin,end,regex);
        while ( ! thereExistsLineMatchingRegex && childType != NodeType.plik) {
            childType = childType.findNextNodeType();
            regex = NodeType.findRegex(childType);
            thereExistsLineMatchingRegex = checkIfExistsLineMatchingRegex(begin,end,regex);
        }

        if (thereExistsLineMatchingRegex) {
            int prevIndex;
            int nextIndex = findNextLineMatchingRegex(begin,end,regex);
            Node vertex;
            while (nextIndex <= end) {
                prevIndex = nextIndex;
                nextIndex = findNextLineMatchingRegex(prevIndex+1,end,regex);
                vertex = VerticesOfGraph[prevIndex];
                root.addVertexToNeighbours(vertex);
                buildTree(prevIndex+1,nextIndex-1,vertex);
            }
        }
    }

    private boolean checkIfExistsLineMatchingRegex (int beginIndex, int endIndex, String regex) {
        for (int i = beginIndex; i <= endIndex; i++)
            if (Pattern.matches(regex,LinesOfFile[i]))
                return true;
        return false;
    }

    private int findNextLineMatchingRegex (int begin, int end, String regex) {
        for (int i = begin; i <= end; i++)
            if (Pattern.matches(regex,LinesOfFile[i]))
                return i;
        return end+1;
    }
}
