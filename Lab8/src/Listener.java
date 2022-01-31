import mpi.MPI;
import message.CloseMessage;
import message.Message;
import message.SubscribeMessage;
import message.UpdateMessage;

public class Listener implements Runnable {

    private final DSM dsm;

    public Listener(DSM dsm) {
        this.dsm = dsm;
    }

    @Override
    public void run() {
        while (true) {
            System.out.println("Rank " + MPI.COMM_WORLD.Rank() + " waiting..");
            Object[] messages = new Object[1];

            MPI.COMM_WORLD.Recv(messages, 0, 1, MPI.OBJECT, MPI.ANY_SOURCE, MPI.ANY_TAG);
            Message message = (Message) messages[0];

            if (message instanceof CloseMessage) {
                System.out.println("Rank " + MPI.COMM_WORLD.Rank() + " stopped listening...");
                return;
            } else if (message instanceof SubscribeMessage) {
                SubscribeMessage subscribeMessage = (SubscribeMessage) message;
                System.out.println("Rank " + MPI.COMM_WORLD.Rank() + " received: rank " + subscribeMessage.rank + " subscribes to " + subscribeMessage.variable);
                dsm.syncSubscription(subscribeMessage.variable, subscribeMessage.rank);
            } else if (message instanceof UpdateMessage) {
                UpdateMessage updateMessage = (UpdateMessage) message;
                System.out.println("Rank " + MPI.COMM_WORLD.Rank() + " received:" + updateMessage.variable + "->" + updateMessage.value);
                dsm.setVariable(updateMessage.variable, updateMessage.value);
            }

            DSM.printDSMInfo(dsm);
        }
    }
}
