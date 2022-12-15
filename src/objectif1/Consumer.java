package objectif1;

public class Consumer extends Thread {
    
    ProdConsBuffer buf;
    long consTime;

    Consumer(ProdConsBuffer buf, long consTime){
        this.buf = buf;
        this.consTime = consTime;

    }
    public void run() {
        
        try {
            buf.get();
            //traitement msg
            Thread.sleep(this.consTime);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}