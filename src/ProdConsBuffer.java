public class ProdConsBuffer implements IProdConsBuffer {

    Message[] buff;
    int nempty, nfull = 0;
    int bufSz;
    int nmsg;
    int totmsg;

    ProdConsBuffer (int n){
        this.buff = new Message[n];
        this.bufSz = n;
    }
    
    

    @Override
    public void put(Message m) throws InterruptedException {
        while(nmsg >= bufSz){
            wait();
        }
        buff[nempty] = m;
        nempty = (nempty+1) % bufSz;
        nmsg++; totmsg++;
        notify();
        
    }

    @Override
    public Message get() throws InterruptedException {
        while (nmsg <= 0){
            wait();
        }
        Message rep = buff[nfull];
        nfull = (nfull+1) % bufSz;
        nmsg--;
        notify();
        return rep;
    }

    @Override
    public int nmsg() {
        return nmsg;
    }

    @Override
    public int totmsg() {
        return totmsg;
    }

    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!");
    }
}
