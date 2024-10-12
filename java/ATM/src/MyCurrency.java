import java.util.ArrayList;
import java.util.Objects;

public class MyCurrency {
//    public enum Type {USD, CNY}
    public enum Type {
        CNY("人民币"), USD("美元");
        private final String name;
        Type(String name) {
            this.name = name;
        }
        public String getName() {
            return name;
        }
    }

    public ArrayList<Bean> beanArrayList = new ArrayList<>();

    public void add(Bean... beanObjects) {
        for (Bean bean : beanObjects) {
            if (!beanArrayList.contains(bean)) {
                beanArrayList.add(bean);
            }
        }
    }

    interface ExchangeRate {
        double Converter(double amount, Type sourceType, Type targetType);
    }

    public Bean getBeanByType(Type type){
        for (Bean bal: beanArrayList) {
            if (bal.getName() == type){
                return bal;
            }
        }
        Bean bal = new Bean();
        bal.setName(type);
        bal.setValue(0.0);
        return bal;
    }

    public static class Bean {
        private MyCurrency.Type name;
        private double value;
        public Type getName() {
        return name;
    }
        public void setName(Type name) {
        this.name = name;
    }
        public double getValue() {
        return value;
    }
        public void setValue(double value) {
        this.value = value;
    }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Bean bean = (Bean) o;
            return Double.compare(bean.value, value) == 0 && name == bean.name;
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, value);
        }
    }
}