
public interface IProdConsBuffer {
    /**
* Put the message m in the buffer
**/
public void put(Message m) throws InterruptedException;
/**
* Retrieve a message from the buffer,
* following a FIFO order (if M1 was put before M2, M1
* is retrieved before M2)
**/
public Message get() throws InterruptedException;
}