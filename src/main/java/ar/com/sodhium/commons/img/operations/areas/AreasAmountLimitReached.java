package ar.com.sodhium.commons.img.operations.areas;

public class AreasAmountLimitReached extends Exception {
    private static final long serialVersionUID = 1752528641931628137L;
    private int limit;

    public AreasAmountLimitReached(int limit) {
        this.limit = limit;
    }
    
    @Override
    public String getMessage() {
        return "Areas amount limit reached (" + limit + ")";
    }
}
