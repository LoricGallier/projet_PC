package objectif1;

import java.util.Properties;

import javax.sql.rowset.spi.SyncFactory;


public class TestProdCons {

    public static void main(String[] args) throws Exception {

        Properties properties = new Properties();
        properties.loadFromXML(TestProdCons.class.getClassLoader().getResourceAsStream("options.xml"));
        int nProd = Integer.parseInt(properties.getProperty("nProd"));
        int nCons = Integer.parseInt(properties.getProperty("nCons"));
        int bufSz = Integer.parseInt(properties.getProperty("bufSz"));
        int prodTime = Integer.parseInt(properties.getProperty("prodTime"));
        int consTime = Integer.parseInt(properties.getProperty("consTime"));
        int minProd = Integer.parseInt(properties.getProperty("minProd"));
        int maxProd = Integer.parseInt(properties.getProperty("maxProd"));
        






        
        ProdConsBuffer buff = new ProdConsBuffer(minProd);
        Producer thsP[] = new Producer[nProd];
        Consumer thsC[] = new Consumer[nCons];
        System.out.println("nProd :"+ nProd);
        System.out.println("nCons :"+ nCons);

        for (int i = 0; i < nProd; i++) {
            thsP[i] = new Producer(buff);
            thsP[i].start();
            System.out.println("Thread Producer "+ i + " started");
        }

        for (int i = 0; i < nCons; i++) {
            thsC[i] = new Consumer(buff);
            thsC[i].start();
            System.out.println("Thread Consumer "+ i + " started");
        }

        for (int i = 0; i < nProd; i++) {
            try {
                thsP[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < nCons; i++) {
            try {
                thsC[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("prod fini");

    }

}
