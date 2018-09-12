import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.Future;

public class SendTask implements Runnable {

    AsynchronousSocketChannel socket;
    Future result;

    public SendTask(AsynchronousSocketChannel socket) {
        this.socket = socket;
    }

    public void run() {
        while (true)
        {
            try
            {
                Thread.sleep(3000);
            }
            catch (Exception ex) {}
            String msg = "Booze";
            byte[] bytes = msg.getBytes();
            ByteBuffer buffer = ByteBuffer.wrap(bytes);
            Future result = socket.write(buffer);
            buffer.clear();
        }
    }
}
