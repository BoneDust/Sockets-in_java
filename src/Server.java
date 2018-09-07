import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

//todo have the serever accept multiple clients at once. need to reconfirm tbis Thread.currentThread().join() method

public class Server
{
    static int clients = 0;
    static ExecutorService threadPool;
    public static void main(String[] args)
    {
        new Server().runServer();
    }

    private void runServer()
    {
        Thread currentThread;
        AsynchronousServerSocketChannel server = null;
        final int port = 5007;
        try
        {
            threadPool  = Executors.newCachedThreadPool(Executors.defaultThreadFactory());
            server = AsynchronousServerSocketChannel.open();
            InetSocketAddress hostAddress = new InetSocketAddress("localhost", port);
            System.out.println("we're about to start");
            server.bind(hostAddress);
            System.out.println("Server is listening on port: " + port);
            currentThread = Thread.currentThread();
            server.accept("", new ServerCompletionHandler(server, threadPool, currentThread));
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

    public static void handleConnection(AsynchronousSocketChannel socket)
    {
        MessageTask msgTask = new MessageTask(socket);
        threadPool.execute(msgTask);
    }
}