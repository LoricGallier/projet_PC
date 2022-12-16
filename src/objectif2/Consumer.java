package objectif2;

public class Consumer extends Thread {
    private IProdConsBuffer buffer;
    private long consTime;

    public Consumer(ProdConsBuffer buf, long consTime){
        this.buffer = buf;
        this.consTime = consTime;
    }
    public void run() {
        while(true){
            try {
                final Message m = buffer.get();

                if (m == Message.NULL_MESSAGE){ // si le message est null, on arrete le thread
                    break;
                }
                Thread.sleep(this.consTime);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        
    }
    @Override
	public String toString() {
		final long id = this.getId();
		return "[C" + (id < 10 ? "0" : "") + id + "]";
	}

}