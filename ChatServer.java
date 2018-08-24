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
    
    //ִ���������������ڶ˿��ϡ����û������߳�
    private void start()
    {
        sf.launchFrame();
        try
        { 
         ss = new ServerSocket(PORT);
        } catch (BindException e)
            {
                sf.getTa().append("�˿ں��Ѿ���ռ�ã� "+"\n");
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
            sf.getTa().append("����������ʧ�ܣ�"+"\n");
        while (stared)
        {

            //System.out.println("Waiting for connecting...");
            try{
            s = ss.accept();
            Client c = new Client(s , userNum++);
            sf.getTa().append(c.getName()+" ���ӣ� \n");
            clients.add(c);
            new Thread(c).start();
                } catch (IOException e)
                    {
                        e.printStackTrace();
                    }
        }
    }
    
    //�ڲ��߳��ࣺ�������ݺͷ�������
    class Client implements Runnable
    {
        Socket s = null;
        private DataInputStream dis = null;
        private DataOutputStream dos = null;
        private boolean bConnected = false;
        String name;
        private boolean live;
        
        //���췽��
        public Client(Socket s , int user)
        {
            this.s = s;
            name =new String("�û�"+user);
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
        
        //�������쳣ʱ��Ҳ����˵�����Ѿ��Ͽ��ˣ����Ѿ������ˡ���ô��Ҫ�Ƴ���
        //��������Ƿ����������ö�����Ϣ
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
        
        //�߳�ִ������д������ͷ���
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
                //�������EOFException�󣬾���˵����̶߳�Ӧ���û������ˣ���ô����߳̾�Ҫ�����ˣ�
                //����break����Ҳ����Ⱥ����Ϣ�ˡ�
                        {
                             clients.remove(this);
                            sf.getTa().append(name+" ���ߣ�\n");
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