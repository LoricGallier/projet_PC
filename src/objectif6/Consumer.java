package objectif6;

public class Consumer extends Thread {
    IProdConsBuffer buffer;
    private long consTime;
    private static final int MIN_CONS = 1, MAX_CONS = 10;

    public Consumer(ProdConsBuffer buf, long consTime){
        this.buffer = buf;
        this.consTime = consTime;
    }
    public void run() {
        boolean running = true;
		
		while (running) {
			try {
				final Message[] messages = buffer.get((int) (Math.random() * (double) (MAX_CONS - MIN_CONS + 1)) + MIN_CONS);
				
				for (final Message m : messages) {
					if (m == Message.NULL_MESSAGE) {
						running = false;
						break;
					}
					
					// Traitement du message...
					
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