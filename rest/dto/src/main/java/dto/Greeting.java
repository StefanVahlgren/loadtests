package dto;

public class Greeting {

    private final long id;
    private final Customer content;
    
    public Greeting(long id, Customer content) {
        this.id = id;
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public Customer getContent() {
        return content;
    }
    
}
