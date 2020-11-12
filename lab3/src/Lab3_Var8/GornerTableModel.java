package Lab3_Var8;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class GornerTableModel extends AbstractTableModel{
    private Double[] coefficients;
    private Double from;
    private Double to;
    private Double step;

    public GornerTableModel(Double from, Double to, Double step,
                            Double[] coefficients){
        this.from = from;
        this.to = to;
        this.step = step;
        this.coefficients = coefficients;
    }

    public Double getFrom() {
        return from;
    }

    public Double getTo() {
        return to;
    }

    public Double getStep() {
        return step;
    }

    public int getColumnCount() {
        return 3;
    }

    public int getRowCount() {
        return new Double(Math.ceil((to-from)/step)).intValue()+1;
    }

    public Object getValueAt(int row, int col) {
        double x = from + step*row;
        if (col == 0) {
            return x;
        }
        if (col == 1) {
            Double result = 0.0;
            for(int i=0;i<coefficients.length;i++){
                result=(result*x+coefficients[i]);
            }
            return result;
        }
        else{
            Double result = 0.0;
            for(int i=0;i<coefficients.length;i++){
                result=(result*x+coefficients[i]);
            }
            result = Math.round(result*10e5)/10e5;
            double dr = Double.parseDouble(result.toString().split("\\.")[1]);
            System.out.print(dr);
            if(dr%2 == 0) {
                return false;
            }
            else{
                return true;
            }
        }
    }

    public String getColumnName(int col) {
        switch (col) {
            case 0:
                return "Значение X";
            case 1:
                return "Значение многочлена";
            default:
                return "Дробная часть нечётная";
        }
    }

    public Class<?> getColumnClass(int col) {
        if (col==2) return Boolean.class;
        else
            return Double.class;
    }
}
