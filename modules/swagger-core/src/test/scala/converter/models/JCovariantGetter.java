package converter.models;

public abstract class JCovariantGetter {

    public Object getMyProperty() {
        return "42";
    }

    public Object getMyOtherProperty() {
        return "42";
    }

    public static class Sub extends JCovariantGetter {

        @Override
        public Integer getMyProperty() {
            return 42;
        }

        @Override
        public Integer getMyOtherProperty() {
            return 42;
        }

    }

}
