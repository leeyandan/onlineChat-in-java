package Server;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/*
 * Frame中有布局管理器LayoutManager
 * 常见有FlowLayout BorderLayout GridLayout CardLayout GridBagLayout
 * FlowLayout 流水布局
 * BorderLayout东西南北中五个区域，是Frame的默认布局管理器
 * GridLayout 表格布局管理器，按照表格划分 new GridLayout(3,2);   3行2列 
 * 用法是frame.setLayout(new FlowLayout(FlowLayout.LEFT));
 * frame.pack() 打包，Frame大小的包着各个组件的大小
 * 在外面设置setLocation setSize setBounds 都会被布局管理器给覆盖，如果要自己设置大小，应该取消布局管理器setLayout(null)
 * 
 * 事件监听
 *  对于按钮，要实现ActionListener这个接口，好像也是一个类，实现它的actionPerformed()方法，匿名类ActionListener的设计比较好
 *  当有一堆事件处理一堆匿名类的时候，设计一个方法myEvent()是让代码变得整齐一些
 *  当接口实现了自己的方法，它就变成一个类了
 
 */

public class ServerFrame extends Frame
{
        private static final int WIDTH = 600;
        private static final int HEIGHT = 400;
        private static final int FrameX = 400;
        private static final int FrameY = 300;
        Button b = new Button("点击按钮，关闭服务器！");
        TextArea ta = new TextArea();
        Dialog confirmBox = new Dialog(this , true);            
        Button yes = new Button("是");
        Button no = new Button("否");
        Label info =new Label("确定停止运行服务器吗？");
        Dialog frameClose = new Dialog(this ,"暴躁的程序员", true);
        Label infoClose = new Label("               傻逼吗？叫你点按钮啊！这个没用！");
        
        public void launchFrame()
        {
            setBackground(Color.DARK_GRAY);
            setLocation(FrameX, FrameY);
            setSize(WIDTH , HEIGHT);
            setTitle("piChatServer");
            setLayout(null);
            setResizable(false);
            b.addActionListener(new ActionListener() 
            {
                public void actionPerformed(ActionEvent arg0)
                {
                    confirmBox.setVisible(true);
                }
                
            });
            addWindowListener(new WindowAdapter()
            {
                public void windowClosing(WindowEvent e)
                {
                   frameClose.setVisible(true);
                }
            });
            ta.setEditable(false);
            add(b);
            b.setLocation(10, 30);
            b.setSize(WIDTH - 20, 45);
            add(ta);
            ta.setText("服务器运行中...\n");
            ta.setLocation(0, 80);
            ta.setSize(WIDTH, HEIGHT - 70);
           setVisible(true);
           confirmBox.setLayout(new FlowLayout());
           confirmBox.add(info );
           confirmBox.add(yes );
           confirmBox.add(no );
           confirmBox.setLocation(FrameX+150, FrameY + 170); 
           confirmBox.setSize(300, 150);
           frameClose.setLocation(FrameX+150, FrameY + 170); 
           frameClose.setSize(300, 150);
           frameClose.setLayout(new BorderLayout());
           frameClose.add(infoClose , BorderLayout.CENTER);
           myEvent();
        }
        
        //事件处理
        private void myEvent()
        {
            frameClose.addWindowListener(new WindowAdapter()
            {
                public void windowClosing(WindowEvent e)
                {
                  frameClose.setVisible(false);
                }
                
            });
            confirmBox.addWindowListener(new WindowAdapter() 
            {
                public void windowClosing(WindowEvent e)
                {
                        confirmBox.setVisible(false);
                }
                
            });
            yes.addActionListener(new ActionListener() 
            {
                public void actionPerformed(ActionEvent arg0)

                {
                    System.exit(0);
                }
            }   );
            no.addActionListener(new ActionListener() 
            {
                public void actionPerformed(ActionEvent arg0)

                {
                    confirmBox.setVisible(false);
                }
            }   );
           
        }
        
        public TextArea getTa()
        {
            return ta;
        }

  
}
