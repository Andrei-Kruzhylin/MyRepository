package Lab3_Var8;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class AboutProgramFrame extends JFrame {
    static final int WIDTH=350;
    static final int HEIGHT=200;

    public AboutProgramFrame(){
        super("О программе");
        setSize(WIDTH,HEIGHT);
        this.setResizable(false);
        Toolkit kit=Toolkit.getDefaultToolkit();
        setLocation((kit.getScreenSize().width - WIDTH)/2,(kit.getScreenSize().height - HEIGHT)/2);


        Box hBoxData=Box.createVerticalBox();
        hBoxData.add(Box.createVerticalGlue());

        JLabel Name= new JLabel("Автор: Кружилин Андрей Игоревич");
        JLabel Group= new JLabel("Группа: 7");
        hBoxData.add(Name);
        hBoxData.add(Group);
        hBoxData.add(Box.createVerticalGlue());

        Box hBoxContent=Box.createHorizontalBox();
        hBoxContent.add(Box.createHorizontalGlue());
        hBoxContent.add(hBoxData);
        hBoxContent.add(Box.createHorizontalGlue());
        getContentPane().add(hBoxContent, BorderLayout.CENTER);
    }
}


