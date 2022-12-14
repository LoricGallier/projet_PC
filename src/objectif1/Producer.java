package objectif1;

public class Producer extends Thread {

	long prodTime;
    int nbmsg;
    ProdConsBuffer buffer;
    int sent;

    Producer(ProdConsBuffer buffer, long prodTime, int minProd, int maxProd){
        this.buffer = buffer;
        this.prodTime= prodTime;
        double f = Math.random()/Math.nextDown(1.0);
        this.nbmsg = (int) (minProd*(1.0 - f) + maxProd*f);

        this.sent = 0;
        System.out.println(this.getId());

    }


    public void run() {
        while(this.nbmsg-this.sent > 0){
            try {
                this.buffer.put(new Message("MessageP " + (this.getId() * 1000 + this.sent), this.getId()));
            } catch (InterruptedException e) {
                e.printStackTrace();
            } 
            this.sent++;
            try {
                Thread.sleep(this.prodTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
	public String toString() {
		final long id = this.getId();
		return "[P" + (id < 10 ? "0" : "") + id + "]";
	}
	
}