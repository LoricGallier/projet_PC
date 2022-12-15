package objectif6;

import java.util.concurrent.Semaphore;

public class ProdConsBuffer implements IProdConsBuffer {

    long WAITING_TIME = 1L;

    Message[] buffer;
    private final Semaphore semaphore, consSemaphore;
    
    int head, tail, bufSz, totmsgProduced, totmsgConsumed, msgtoproduce;

    ProdConsBuffer (int bufSz){
        this.buffer = new Message[bufSz];
        this.semaphore = new Semaphore(1);
        this.consSemaphore = new Semaphore(1);


        this.bufSz = bufSz;
        this.totmsgProduced = 0;
        this.totmsgConsumed = 0;
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
    public void put(Message m, int n) throws InterruptedException {
        this.semaphore.acquire();
        m.count = n;
        
        while(this.head == this.tail){
            this.semaphore.release();
            Thread.sleep(WAITING_TIME);
            this.semaphore.acquire();
        }
        this.buffer[this.tail] = m;

        if (this.head == -1)
			this.head = this.tail;

        this.tail = (this.tail+1) % this.bufSz;

        totmsgProduced+= n; 

        for (int i = 0; i < n; i++)
			System.out.println(Thread.currentThread().toString() + " Message produced:      " + m);

        this.semaphore.release();


        while (m.count > 0) {
			//System.out.println("P" + Thread.currentThread().getId() + " est bloqué, il reste " + m.getCount() + " exemplaires.");
			Thread.sleep(WAITING_TIME);
		}
    }



    @Override
    public synchronized Message get() throws InterruptedException {

        while(this.head==-1) {
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

        totmsgConsumed++;
        System.out.println(Thread.currentThread().toString() + " Consumed message:      " + message);

        this.notifyAll();
        return message;


    }

    @Override
    public Message[] get(int n) throws InterruptedException {
        this.consSemaphore.acquire();
		this.semaphore.acquire();

        final Message[] messages = new Message[n];

        for (int i = 0; i < n; i++) {
            while(this.head==-1) {
                if (this.totmsgProduced - this.totmsgConsumed ==0){
                    for (int j = i; j < n; j++)
					    messages[j] = Message.NULL_MESSAGE;
				    break;
                }
            this.semaphore.release();
			Thread.sleep(WAITING_TIME);
			this.semaphore.acquire();   
            }

            if (messages[i] == Message.NULL_MESSAGE)
				break;

                messages[i] = this.buffer[this.head];
			
                if (messages[i].count > 0)                   
                    messages[i].count--;
                
                if (messages[i].count == 0) {
                    if (this.nmsg() == 1)
                        this.head = -1;
                    else
                        this.head = (this.head + 1) % this.bufSz;
                }
                
                this.totmsgConsumed++;
                
            }
            for (final Message message : messages) {
                if (message == Message.NULL_MESSAGE)
                    break;
                
                System.out.println(Thread.currentThread().toString() + " Consumed message:      " + message);
            }
            
            this.semaphore.release();
            this.consSemaphore.release();
            
            while (messages[n-1].count > 0) {
                Thread.sleep(WAITING_TIME);
            }
            return messages;

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
