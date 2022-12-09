public class Producer extends Thread {
    ProdConsBuffer buf;
    Producer(ProdConsBuffer buf){
        this.buf = buf;
    }
    public void run(Message Msg) throws InterruptedException {
        buf.put(Msg); 
        
    }

}