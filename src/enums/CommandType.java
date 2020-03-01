package enums;

public enum CommandType {
    GENERAL("search?generalSearchInput=%s&requireAllWords=true&api_key="),
    DESCRIPTION("%s?api_key="),
    BARCODE(""),
    IMAGE("");

    private String query;

    public String getValue() {
        return this.query;
    }

    CommandType(String query) {
        this.query = query;
    }
}
