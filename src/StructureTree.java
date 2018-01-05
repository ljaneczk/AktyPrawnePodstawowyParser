import java.util.regex.Pattern;

public class StructureTree extends TreeOperations{
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
        System.out.println(VerticesOfGraph.length-1);
        buildTree(0,VerticesOfGraph.length-1,Root);
        updateTableOfContent(Root);
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
                while (end+1 < NumberOfLines && BeginningsOfNodeType[end+1]==null)
                    end++;
                addLinesToVertex(ind,end,VerticesOfGraph[ind]);
            }
        if(ThereWasEnterBetweenArticleAndContent)
            matchArticlesAndContent();

        /*for (int ind = 0; ind < NumberOfLines; ind++)
            if(VerticesOfGraph[ind] != null)
                VerticesOfGraph[ind].printContent();*/
    }

    private NodeType[] findBeginningsOfNodeType () {
        NodeType[] BeginningOfNodeType = new NodeType[NumberOfLines];
        for (int i=0; i<NumberOfLines; i++)
            BeginningOfNodeType[i] = null;

        NodeType type = NodeType.podrozdział;
        do {
            for (int i = 0; i < NumberOfLines; i++)
                if (Pattern.matches(NodeType.findRegex(type),LinesOfFile[i]))
                    BeginningOfNodeType[i] = type;
            type = type.findNextNodeType();
        }
        while (type != NodeType.podrozdział);

        for (int i = 0; i < NumberOfLines; i++)
            if ( BeginningOfNodeType[i] != null)
                System.out.println(Integer.toString(i)+"   "+BeginningOfNodeType[i]+"   "+LinesOfFile[i]);
        System.out.println("\n\n\n\n");

        return BeginningOfNodeType;
    }

    private void addLinesToVertex (int begin, int end, Node vertex) {
        //System.out.println(vertex.getNodeType()+"  "+Integer.toString(begin)+"  "+Integer.toString(end));
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
        String ArticleLine = PrevVertex.getContentLine(0);
        String FirstContentLine = NextVertex.getContentLine(indexInNextVertex);
        ArticleLine = ArticleLine.substring(0, ArticleLine.lastIndexOf('.')+1);
        PrevVertex.removeContentLine(0);
        NextVertex.changeContentLine(0,ArticleLine+' '+FirstContentLine);
    }


    private void buildTree(int begin, int end, Node root) {
        System.out.println("Enter: "+Integer.toString(begin)+" "+Integer.toString(end)+"  "+root.getNodeType()+"  "+root.getNumber());
        NodeType childType = root.getNodeType().findNextNodeType();
        String regex = NodeType.findRegex(childType);
        boolean ThereExistsLineMatchingRegex = checkIfExistsLineMatchingRegex(begin,end,regex);
        while (!ThereExistsLineMatchingRegex && childType != NodeType.plik) {
            childType = childType.findNextNodeType();
            regex = NodeType.findRegex(childType);
            ThereExistsLineMatchingRegex = checkIfExistsLineMatchingRegex(begin,end,regex);
        }
        System.out.println(childType+"  "+ThereExistsLineMatchingRegex);
        root.printContent();
        if (ThereExistsLineMatchingRegex) {
            int prevIndex = -1;
            int nextIndex = findNextLineMatchingRegex(begin,end,regex);
            Node vertex;
            while (nextIndex <= end) {
                prevIndex = nextIndex;
                nextIndex = findNextLineMatchingRegex(prevIndex+1,end,regex);
                vertex = VerticesOfGraph[prevIndex];
                //System.out.println(prevIndex + "  " + nextIndex + "  " + VerticesOfGraph[prevIndex] + "  " + VerticesOfGraph[prevIndex].getNodeType() + "  " + VerticesOfGraph[prevIndex].getFieldNumber());
                root.addVertexToNeighbours(vertex);
                //System.out.println(prevIndex + "  " + nextIndex + "  " + end + "  " + vertex + "  " + vertex.getNodeType() + "  " + vertex.getNumber()+'\n');
                buildTree(prevIndex+1,nextIndex-1,vertex);
            }
            //System.out.println("Leaving  " + prevIndex + "  " + nextIndex + "  "+end+"   "+root.getNodeType()+"  "+root.getNumber()+'\n');
        }
        //else
        //System.out.println("Leaving without visit  " + begin + "  " +end+"   "+root.getNodeType()+"  "+root.getNumber()+'\n');
    }

    private boolean checkIfExistsLineMatchingRegex (int begin, int end, String regex) {
        for (int i = begin; i <= end; i++)
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
