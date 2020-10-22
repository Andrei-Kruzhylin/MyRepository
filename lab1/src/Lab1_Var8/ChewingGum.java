package Lab1_Var8;

public class ChewingGum extends Food {

    private String flavour;

    public ChewingGum(String flavour) {
        super("Жевательная резинка");
        this.flavour = flavour;
    }

    @Override
    public void consume() {
        System.out.println(this + " съедена");
    }

    public String getFlavour() {
        return flavour;
    }

    public void setFlavour(String flavour) {
        this.flavour = flavour;
    }

    public boolean equals(Object arg0) {
        if (super.equals(arg0)) {
            if (!(arg0 instanceof ChewingGum)) return false;
            return flavour.equals(((ChewingGum)arg0).flavour);
        } else
            return false;
    }

    public String toString() {
        return super.toString() + " вкуса '" + flavour.toUpperCase() + "'";
    }
}

