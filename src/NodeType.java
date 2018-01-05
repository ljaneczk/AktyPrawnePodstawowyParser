public enum NodeType {
    plik,
    dział,
    rozdział,
    podrozdział,
    artykuł,
    ustęp,
    punkt,
    litera;

    @Override
    public String toString() {
        return super.toString();
    }

    public NodeType findNextNodeType (NodeType type) {
        switch (type) {
            case plik:
                return dział;
            case dział:
                return rozdział;
            case rozdział:
                return podrozdział;
            case podrozdział:
                return artykuł;
            case artykuł:
                return ustęp;
            case ustęp:
                return punkt;
            case punkt:
                return litera;
            case litera:
                return plik;
        }
        return plik;
    }

    public NodeType findNextNodeType() {
        return findNextNodeType(this);
    }

    public static String findRegex (NodeType type) {
        switch (type) {
            case plik:
                return "^plik.*";
            case dział:
                return "^DZIAŁ.*";
            case rozdział:
                return "^Rozdział.*";
            case podrozdział:
                return "^[\\p{Lu}|\\p{Punct}|\\p{Space}]*";
            case artykuł:
                return "^Art\\..*";
            case ustęp:
                return "^[\\d]+[\\w]*\\.(\\s).*";
            case punkt:
                return "^[\\d]+[\\w]*\\)(\\s).*";
            case litera:
                return "^[a-z]+\\)(\\s).*";
        }
        return type.toString();
    }
}
