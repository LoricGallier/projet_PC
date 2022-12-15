package objectif5;

public class Consumer extends Thread {

    int MIN_CONS = 1, MAX_CONS = 10;

    private IProdConsBuffer buffer;
    private long consTime;

    public Consumer(ProdConsBuffer buf, long consTime){
        this.buffer = buf;
        this.consTime = consTime;
    }

    
    public void run() {
        boolean running = true;
        
        while(running){
            try {
                final Message[] messages = buffer.get((int) (Math.random() * (double) (MAX_CONS - MIN_CONS + 1)) + MIN_CONS);

                for (final Message m : messages) {
					if (m == Message.NULL_MESSAGE) {
						running = false;
						break;
					}
					
                Thread.sleep(this.consTime);
                }

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