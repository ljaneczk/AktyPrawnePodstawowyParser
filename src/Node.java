import java.util.Vector;

public class Node {
    private NodeType Type;
    private Number FieldNumber;
    private Number LeftRange;
    private Number RightRange;
    private Vector <Node> Neighbours;
    private String[] Content;
    private int ContentSize;

    Node(NodeType type) {
        Type = type;
        ContentSize = 0;
        Content = new String[0];
        Neighbours = new Vector<>(0);
        FieldNumber = LeftRange = RightRange = null;
        if (type == NodeType.plik)
            FieldNumber = new Number("",NodeType.plik);
    }

    public NodeType getNodeType() {
        return Type;
    }

    public Number getNumber() {
        return FieldNumber;
    }

    private void setNumber (String line) {
        FieldNumber = new Number(line,Type);
    }

    public Vector<Node> getNeighbours() {
        return Neighbours;
    }

    public Number getLeftRange() {
        return LeftRange;
    }

    public Number getRightRange() {
        return RightRange;
    }

    public void setLeftRangeAndRightRange() {
        LeftRange = RightRange = null;
    }

    public void updateLeftRangeAndRightRange (Node vertex) {
        if (vertex.getNodeType() != NodeType.artykuł) {
            if (vertex.LeftRange != null && (vertex.LeftRange).isNotGreaterThan(LeftRange))
                LeftRange = vertex.LeftRange;
            if (vertex.RightRange != null && (vertex.RightRange).isNotLessThan(RightRange))
                RightRange = vertex.RightRange;
        }
        else {
            if ((vertex.FieldNumber).isNotGreaterThan(LeftRange))
                LeftRange = vertex.FieldNumber;
            if ((vertex.FieldNumber).isNotLessThan(RightRange))
                RightRange = vertex.FieldNumber;
        }
    }

    public void addVertexToNeighbours (Node vertex) {
        Neighbours.addElement(vertex);
        if (vertex.FieldNumber.isNotGreaterThan(LeftRange))
            LeftRange = vertex.getNumber();
        if (vertex.FieldNumber.isNotLessThan(RightRange))
            RightRange = vertex.FieldNumber;
    }

    public void addContentLine(String line) {
        int NewContentSize = ContentSize + 1;
        String[] NewContent = new String[NewContentSize];
        for (int i = 0; i < NewContentSize - 1; i++)
            NewContent[i] = Content[i];
        NewContent[NewContentSize-1] = line;
        Content = NewContent;
        ContentSize = NewContentSize;
        if (NewContentSize == 1)
            setNumber(line);
    }

    public void removeContentLine(int index) {
        assert (index < ContentSize);
        int NewContentSize = ContentSize - 1;
        String[] NewContent = new String[NewContentSize];
        for (int i = 0; i < index; i++)
            NewContent[i] = Content[i];
        for (int i = index; i < NewContentSize; i++)
            NewContent[i] = Content[i+1];
        Content = NewContent;
        ContentSize = NewContentSize;
    }

    public String getContentLine(int index) {
        assert (index < ContentSize);
        return Content[index];
    }

    public void changeContentLine(int index, String line) {
        assert (index < ContentSize);
        Content[index] = line;
    }

    public void printContent() {
        if(ContentSize > 0) {
            for (String line : Content)
                System.out.println(line);
        }
    }

    public void printContentWithAddingArticlesRange(String beginning, String articlesRange) {
        if(ContentSize > 0) {
            System.out.println(beginning+Content[0]+articlesRange);
            for (int i = 1; i < ContentSize; i++)
                System.out.println(beginning+Content[i]);
        }
    }
}
