package objectif6;

public class Message {

    public static Message NULL_MESSAGE = new Message(null, -1, -1);
    private String msg;
    long prodID, timestamp;
    int count;




    public Message(String msg, long prodID) {
        this.msg = msg;
        this.prodID = prodID;
        this.timestamp = System.currentTimeMillis();
    }


    public Message(String msg, long prodID, long timestamp) {
        this.msg = msg;
        this.prodID = prodID;
        this.timestamp = timestamp;
    }

    public Message(String msg, long prodID, long timestamp, int count) {
        this.msg = msg;
        this.prodID = prodID;
        this.timestamp = timestamp;
        this.count = count;
    }

    @Override
	public String toString() {
		return "[P" + (this.prodID < 10 ? "0" : "") + this.prodID + "] -> " + this.msg;
	}
    
}