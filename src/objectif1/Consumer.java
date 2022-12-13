package objectif1;

public class Consumer extends Thread {
    
    ProdConsBuffer buf;
    Consumer(ProdConsBuffer buf){
        this.buf = buf;
    }
    public void run() {
        
        try {
            buf.get();
            //voir ce qu'on fait du msg
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}