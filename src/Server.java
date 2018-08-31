import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class Server
{
    static int clients = 0;
    public static void main(String[] args)
    {
        new Server().runServer();
    }

    private void runServer()
    {
        Thread currentThread;
        AsynchronousServerSocketChannel server = null;
        AsynchronousChannelGroup groupChannel;
        final int port = 5002;
        try
        {
            groupChannel = AsynchronousChannelGroup.withFixedThreadPool(3, Executors.defaultThreadFactory());
            server = AsynchronousServerSocketChannel.open(groupChannel);
            InetSocketAddress hostAddress = new InetSocketAddress("localhost", port);
            System.out.println("we're about to start");
            server.bind(hostAddress);
            System.out.println("Server is listening on port: " + port);
            currentThread = Thread.currentThread();
            server.accept("", new ServerCompletionHandler(server, groupChannel, currentThread));
            try
            {
                currentThread.join();
            }
            catch (InterruptedException ex)
            {
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            if (server != null)
            {
                try {server.close();} catch (IOException ex) {ex.printStackTrace();}
            }
        }
    }

    public static boolean handleConnection(AsynchronousSocketChannel ch, AsynchronousChannelGroup gr, Thread thread)
    {
        clients++;
        ByteBuffer buffer = ByteBuffer.allocate(4096);
        Future result = ch.read(buffer);
        while (! result.isDone())//waits until we're done reading the message from the client
            ;
        buffer.flip();
        String msg = new String(buffer.array()).trim();
        System.out.println("Message from client"+ clients +": " + msg);
        buffer.clear();
        if (msg.equals("Bye"))
        {
            System.out.println("Terminating the group...");
            try
            {
                gr.shutdownNow();
                gr.awaitTermination(3, TimeUnit.SECONDS);
                ch.close();
            }
            catch (IOException | InterruptedException e)
            {
                System.out.println("Exception during group termination");
                e.printStackTrace();
            }
            thread.interrupt();
            return (false);
        }
        return (true);
   }
}