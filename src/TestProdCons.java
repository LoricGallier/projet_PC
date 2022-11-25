import java.util.Properties;

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
        






        

        Thread thsP[] = new Producer[nProd];
        Thread thsC[] = new Consumer[nCons];

        for (int i = 0; i < nProd; i++) {
            thsP[i] = new Thread();
            thsP[i].start();
        }

        for (int i = 0; i < nCons; i++) {
            thsC[i] = new Thread();
            thsC[i].start();
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

    }

}
