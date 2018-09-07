import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.Future;

public class MessageTask implements Runnable
{


    AsynchronousSocketChannel socket;
    Future result;

    public MessageTask(AsynchronousSocketChannel socket)
    {
        this.socket = socket;

    }

    public  void run()
    {
        while (socket.isOpen())
        {
            ByteBuffer buffer = ByteBuffer.allocate(4096);
            result = socket.read(buffer);
            while (!result.isDone())//waits until we're done reading the message from the client
                ;
            buffer.flip();
            String msg = new String(buffer.array()).trim();
            System.out.println("Message from client: " + msg);
        }
    }

}
