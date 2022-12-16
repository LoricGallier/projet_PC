package objectif2;

public class ProdConsBuffer implements IProdConsBuffer {

    Message[] buffer;
    int head, tail, bufSz, totmsgProduced, totmsgConsumed, msgtoproduce;

    ProdConsBuffer (int bufSz){
        this.buffer = new Message[bufSz];


        this.bufSz = bufSz;
        this.totmsgProduced = 0;//
        this.totmsgConsumed = 0;//
        this.msgtoproduce = 0;


        this.head = -1;
		this.tail = 0;

    }
    
    

    @Override
    public synchronized void put(Message m) throws InterruptedException {
        while(this.head == this.tail){
            this.wait();
        }
        this.buffer[this.tail] = m;

        if (this.head == -1)
			this.head = this.tail;

        this.tail = (this.tail+1) % this.bufSz;

        totmsgProduced++; 

        System.out.println(Thread.currentThread().toString() + " Message produced:      " + m);
        this.notifyAll();
        
    }

    @Override
    public synchronized Message get() throws InterruptedException {

        while(this.head==-1) {
        // Tant que le buffer est vide, les threads consommateurs tentant d'y retirer
		// renvoie un message sont mis en attente.
            if (this.totmsgProduced - this.totmsgConsumed ==0)
            {
                return Message.NULL_MESSAGE;
            }
    		this.wait();
    	}
        Message message = buffer[this.head];
        if (this.nmsg() == 1)
			this.head = -1;
		else
			this.head = (this.head + 1) % this.bufSz;

        totmsgConsumed++;//
        System.out.println(Thread.currentThread().toString() + " Consumed message:      " + message);

        this.notifyAll();
        return message;


    }

    @Override
    public int nmsg() {

        if(this.head ==-1) 
            return 0;
        if (this.head == this.tail) 
            return this.bufSz;
        if(this.tail> this.head)
            return this.tail - this.head;
        else
            return this.bufSz + this.tail - this.head;
    }

    @Override
    public int totmsg() {
        return this.totmsgProduced;
    }

    //Spécifiee un nbr de messages qui seront déposés dans le buffer par un producteur
    public void addUpcomingMessages(int count) {
		this.msgtoproduce += count;
	}
}
