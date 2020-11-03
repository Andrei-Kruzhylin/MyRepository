package Lab2_Var8;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static java.lang.Math.*;


public class Main{

    public class MainFrame extends JFrame {

        private static final int HEIGHT = 320;
        private static final int WIDTH = 400;

        private JTextField textFieldX;
        private JTextField textFieldY;
        private JTextField textFieldZ;
        private JTextField textFieldResult;

        private ButtonGroup radioButtons = new ButtonGroup();
        private Box hboxFormulaType = Box.createHorizontalBox();
        private int formulaId = 1;

        public Double calculate1 (Double x, Double y, Double z){
            return pow(cos(PI*pow(x,3)) + log(pow(1+y,2)), 4) * (exp(pow(z, 2)) + sqrt(1/x) + cos(exp(y)));
        }

        public Double calculate2 (Double x, Double y, Double z){
            return pow(x,x)/(sqrt(pow(y,3) + 1) + log(z));
        }

        private void addRadioButton(String buttonName, final int formulaId){
            JRadioButton button = new JRadioButton(buttonName);
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ev) {
                    MainFrame.this.formulaId = formulaId;
                   /* ImagePane.updateUI();*/
                }
            });
            radioButtons.add(button);
            hboxFormulaType.add(button);
        }

    }




    public static void Main(String[] args){

    }


}
