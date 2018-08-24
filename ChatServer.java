package Server;
import java.io.IOException;
import java.util.*;
import java.io.*;
import java.net.*;

public class ChatServer
{
    private static final int PORT = 5500;
    boolean stared = false;
    ServerSocket ss = null;
    Socket s = null;
    int userNum = 1;
    List<Client> clients = new ArrayList<Client>();
    ServerFrame sf = new ServerFrame();
    
    public static void main(String[] args)
    {
        new ChatServer().start();
    }
    
    //执行主方法：监听在端口上。来用户就起线程
    private void start()
    {
        sf.launchFrame();
        try
        { 
         ss = new ServerSocket(PORT);
        } catch (BindException e)
            {
                sf.getTa().append("端口号已经被占用！ "+"\n");
            } 
        catch (IOException e)
            {
                e.printStackTrace();
            }
        if (ss != null)
            {
                stared = true;
            }
        else
            sf.getTa().append("服务器启动失败！"+"\n");
        while (stared)
        {

            //System.out.println("Waiting for connecting...");
            try{
            s = ss.accept();
            Client c = new Client(s , userNum++);
            sf.getTa().append(c.getName()+" 连接！ \n");
            clients.add(c);
            new Thread(c).start();
                } catch (IOException e)
                    {
                        e.printStackTrace();
                    }
        }
    }
    
    //内部线程类：接受数据和发送数据
    class Client implements Runnable
    {
        Socket s = null;
        private DataInputStream dis = null;
        private DataOutputStream dos = null;
        private boolean bConnected = false;
        String name;
        private boolean live;
        
        //构造方法
        public Client(Socket s , int user)
        {
            this.s = s;
            name =new String("用户"+user);
            live = true;
            try{
            dis = new DataInputStream(s.getInputStream());
            dos = new DataOutputStream(s.getOutputStream());
            bConnected = true;
            } catch (IOException e)
                {
                    e.printStackTrace();
                }
        }
        
        public String getName()
        {
            return name;
        }
        
        //当它抛异常时候也就是说连接已经断开了，它已经下线了。那么就要移除它
        //这个方法是服务器给调用对象发消息
        public void send(String message)
        {
            try{
            dos.writeUTF(message);
            }catch (SocketException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        
        //线程执行倾听写入操作和发送
        public void run()
        {
            while (bConnected)
             {
                String message = null;
                try{
                 String str = dis.readUTF();
                 message = new String(name+":\n"+str);
                 sf.getTa().append (message+"\n");
                     } catch (EOFException e)
                //出现这个EOFException后，就是说这个线程对应的用户下线了，那么这个线程就要结束了，
                //所以break，他也不用群发消息了。
                        {
                             clients.remove(this);
                            sf.getTa().append(name+" 下线！\n");
                            try{
                                    if(dos != null) dos.close();
                                    if(dis != null) dis.close();
                                    if(s != null) s.close();
                                } catch (IOException e1)
                                {
                                    e1.printStackTrace();
                                }
                            break;
                        } 
                      catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                 for(int i=0 ; i<clients.size() ; i++)
                 {
                     Client c = clients.get(i);
                     if(this.name != c.name)
                         c.send(message);
                     
                 }
             }

        }
    }
    
}