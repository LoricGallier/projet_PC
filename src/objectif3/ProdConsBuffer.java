package objectif3;

import java.util.concurrent.Semaphore;

public class ProdConsBuffer implements IProdConsBuffer {

    long WAITING_TIME = 1L;

    Message[] buffer;
    Semaphore semaphore;
    int head, tail, bufSz, totmsgProduced, totmsgConsumed, msgtoproduce;

    ProdConsBuffer (int bufSz){
        this.buffer = new Message[bufSz];
        this.semaphore = new Semaphore(1/*, true*/);


        this.bufSz = bufSz;
        this.totmsgProduced = 0;
        this.totmsgConsumed = 0;
        this.msgtoproduce = 0;


        this.head = -1;
		this.tail = 0;

    }
    
    

    @Override
    public void put(Message m) throws InterruptedException {
        this.semaphore.acquire();

        while(this.head == this.tail){
            this.semaphore.release();
            Thread.sleep(WAITING_TIME);
            this.semaphore.acquire();
        }
        this.buffer[this.tail] = m;

        if (this.head == -1)
			this.head = this.tail;

        this.tail = (this.tail+1) % this.bufSz;

        totmsgProduced++; 

        System.out.println(Thread.currentThread().toString() + " Message produced:      " + m);
        this.semaphore.release();
        
    }

    @Override
    public Message get() throws InterruptedException {
        this.semaphore.acquire();

        while(this.head==-1) {
            if (this.totmsgProduced - this.totmsgConsumed ==0)
            {
                this.semaphore.release();
                return Message.NULL_MESSAGE;
            }
    		this.semaphore.release();
            Thread.sleep(WAITING_TIME);
            this.semaphore.acquire();
    	}
        Message message = buffer[this.head];
        if (this.nmsg() == 1)
			this.head = -1;
		else
			this.head = (this.head + 1) % this.bufSz;

        totmsgConsumed++;
        System.out.println(Thread.currentThread().toString() + " Consumed message:      " + message);

        this.semaphore.release();
        
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

    public void addUpcomingMessages(int count) {
		this.msgtoproduce += count;
	}
}
