

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
        while (true)
        {
            ByteBuffer buffer = ByteBuffer.allocate(4096);
            result = socket.read(buffer);
            while (!result.isDone())//waits until we're done reading the message from the client
                ;
            buffer.flip();
            String msg = new String(buffer.array()).trim();
            System.out.println("Message from client: " + msg);
            if (msg.equals("bye"))
                break;
            else if (msg.equals("a"))
            {
                try{Thread.sleep(4000);} catch (Exception po){}
                System.out.println("four seconds");
            }
            else
                System.out.println("right now");

        }
    }

}
