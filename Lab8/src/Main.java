import mpi.MPI;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        MPI.Init(args);
        DSM dsm = new DSM();
        int me = MPI.COMM_WORLD.Rank();
        if (me == 0) {
            Thread thread = new Thread(new Listener(dsm));

            thread.start();

            dsm.subscribeTo("a");
            dsm.subscribeTo("b");
            dsm.subscribeTo("c");
            dsm.compareAndExchange("a", 0, 5);
            dsm.compareAndExchange("c", 2, 7);
            dsm.compareAndExchange("b", 6, 11);
            dsm.close();

            thread.join();

        } else if (me == 1) {
            Thread thread = new Thread(new Listener(dsm));

            thread.start();

            dsm.subscribeTo("a");
            dsm.subscribeTo("c");


            thread.join();
        } else if (me == 2) {
            Thread thread = new Thread(new Listener(dsm));

            thread.start();

            dsm.subscribeTo("b");
            dsm.subscribeTo("a");
            dsm.subscribeTo("c");
            dsm.compareAndExchange("b", 1, 6);

            thread.join();
        } else if (me == 3){
            Thread thread = new Thread(new Listener(dsm));

            thread.start();

            dsm.subscribeTo("b");
            dsm.subscribeTo("a");
            dsm.subscribeTo("c");
            dsm.compareAndExchange("b", 1, 16);

            thread.join();
        } else if (me == 4){
            Thread thread = new Thread(new Listener(dsm));

            thread.start();

            dsm.subscribeTo("c");
            dsm.compareAndExchange("c", 2, 26);

            thread.join();
        }
        MPI.Finalize();
    }
}
