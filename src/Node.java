import java.util.LinkedList;
import java.util.Vector;

public class Node {
    private NodeType Type;
    private Number FieldNumber;
    private Number Begin;
    private Number End;
    private Vector <Node> Neighbours;
    private String[] Content;
    private int ContentSize;

    Node(NodeType type) {
        Type = type;
        ContentSize = 0;
        Content = new String[0];
        Neighbours = new Vector<>(0);
        FieldNumber = Begin = End = null;
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

    public Number getBegin() {
        return Begin;
    }

    public Number getEnd() {
        return End;
    }

    public void setBeginAndEnd() {
        Begin = End = null;
    }

    public void updateBeginAndEnd (Node vertex) {
        if (vertex.getNodeType() != NodeType.artykuł) {
            if (vertex.Begin != null && vertex.Begin.isNotGreaterThan(Begin))
                Begin = vertex.Begin;
            if (vertex.End != null && vertex.End.isNotLessThan(End))
                End = vertex.End;
        }
        else {
            if (vertex.FieldNumber.isNotGreaterThan(Begin))
                Begin = vertex.FieldNumber;
            if (vertex.FieldNumber.isNotLessThan(End))
                End = vertex.FieldNumber;
        }
    }

    public void addVertexToNeighbours (Node vertex) {
        //int s = Neighbours.size();
        Neighbours.addElement(vertex);
        //System.out.println(s + "   " + Neighbours.size());
        if (vertex.FieldNumber.isNotGreaterThan(Begin))
            Begin = vertex.getNumber();
        if (vertex.FieldNumber.isNotLessThan(End))
            End = vertex.FieldNumber;
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

    public void printContentWithoutEnter(String beginning) {
        if(ContentSize > 0) {
            for (int i = 0; i < ContentSize - 1; i++)
                System.out.println(beginning+Content[i]);
            System.out.print(beginning+Content[ContentSize-1]);
        }
    }
}
