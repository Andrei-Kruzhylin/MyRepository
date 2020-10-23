package Lab1_Var8;

public class Main {


    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws Exception{
        Food[] breakfast = new Food[20];
        int itemsSoFar = 0;
        for (String arg: args) {
            String[] parts = arg.split("/");
            if (parts[0].equals("Cheese")) {
                breakfast[itemsSoFar] = new Cheese();
            } else
            if (parts[0].equals("Apple")) {
                breakfast[itemsSoFar] = new Apple(parts[1]);
            }else
            if (parts[0].equals("Chewing Gum")) {
                breakfast[itemsSoFar] = new ChewingGum(parts[1]);
            }
            itemsSoFar++;
        }

        int NumbOfApple = 0;
        int NumbOfCheese = 0;
        int NumbOfChewingGum = 0;

        Apple checkApple = new Apple("Большое");
        Cheese checkCheese = new Cheese();
        ChewingGum checkChewingGum = new ChewingGum("Мята");

        for (Food count: breakfast )
        {
            if (count!=null)
            {
                if (count.equals(checkApple))
                {
                    NumbOfApple++;
                }
                if (count.equals(checkCheese))
                {
                    NumbOfCheese++;
                }
                if (count.equals(checkChewingGum))
                {
                    NumbOfChewingGum++;
                }
            }
        }

        System.out.print("Яблок съедено: ");
        System.out.println(NumbOfApple);
        System.out.print("Сыра съедено: ");
        System.out.println(NumbOfCheese);
        System.out.print("Жевательных резинок съедено: ");
        System.out.println(NumbOfChewingGum);

        System.out.print("Продуктов съедено: ");
        System.out.println(itemsSoFar);
        for (Food item: breakfast)
            if (item!=null)
                item.consume();
            else
                break;
        System.out.println("Всего хорошего!");
    }
}
