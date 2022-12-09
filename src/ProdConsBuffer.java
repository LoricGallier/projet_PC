public class ProdConsBuffer implements IProdConsBuffer {

    Message[] buff;
    int nempty;
    int nfull;
    int bufSz;
    int nmsg;
    int totmsg;

    ProdConsBuffer (int bufSz){
        this.nempty = 0;
        this.nfull = 0;
        this.buff = new Message[bufSz];
        this.bufSz = bufSz;
        this.nmsg = 0;
        this.totmsg = 0;

    }
    
    

    @Override
    public void put(Message m) throws InterruptedException {
        while(nmsg == bufSz){
            wait();
        }
        buff[nempty] = m;
        nempty = (nempty+1) % bufSz;
        nmsg++; totmsg++;
        
    }

    @Override
    public Message get() throws InterruptedException {
        while (nmsg == 0){
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
}
