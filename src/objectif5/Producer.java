package objectif5;

public class Producer extends Thread {

	long prodTime;
    int nbmsg;
    ProdConsBuffer buffer;
    int sent;

    Producer(ProdConsBuffer buffer, long prodTime, int minProd, int maxProd){
        this.buffer = buffer;
        this.prodTime= prodTime;
        this.nbmsg = (int) (Math.random() * (double) (maxProd - minProd + 1)) + minProd;

        if (buffer instanceof ProdConsBuffer)
			((ProdConsBuffer) buffer).addUpcomingMessages(this.nbmsg);
        
        //voir pour ajouter conteur msg envoyer
        this.sent = 0;

    }


    public void run() {
        while(this.nbmsg-this.sent > 0){ //tant qu'il reste des messages a envoyer
            try {
                this.buffer.put(new Message("Message " + (this.getId() * 1000 + this.sent), this.getId()));
                this.sent++;
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