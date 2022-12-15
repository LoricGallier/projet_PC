package objectif5;

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

        while(this.head == this.tail){//tant que le buffer est plein
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
    public synchronized Message get() throws InterruptedException {
        this.semaphore.acquire();

        while(this.head==-1) {
            if (this.totmsgProduced - this.totmsgConsumed ==0)// si tous les messages ont été consumé
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

    public Message[] get(int k) throws InterruptedException {

        this.semaphore.acquire();

        Message[] messages = new Message[k];

        for(int i = 0; i<k; i++){
            while(this.head==-1) {
                if (this.totmsgProduced - this.totmsgConsumed ==0)// si tous les messages ont été consumé
                {
                    for(int j =i;j<k;j++){
                        messages[j]=Message.NULL_MESSAGE;
                    }
                    break;
                }

                this.semaphore.release();
				Thread.sleep(WAITING_TIME);
				this.semaphore.acquire();

            }
            if(messages[i]== Message.NULL_MESSAGE){
                break;
            }

            messages[i] = this.buffer[this.head];

            if(this.nmsg()==1){
                this.head = -1;
            }
            else{
                this.head = (this.head +1) % this.bufSz;
            }

            this.totmsgConsumed++;
        }
        


        for (final Message message : messages) {
			if (message == Message.NULL_MESSAGE)
				break;
			
			System.out.println(Thread.currentThread().toString() + " Consumed message:      " + message);

        }


        this.semaphore.release();
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
