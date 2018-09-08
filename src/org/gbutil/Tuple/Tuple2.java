package org.gbutil.Tuple;

public class Tuple2<T, G> {
    public T first;
    public G second;

    public Tuple2(T first, G second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tuple2)) return false;

        Tuple2<?, ?> tuple2 = (Tuple2<?, ?>) o;

        if (first != null ? !first.equals(tuple2.first) : tuple2.first != null) return false;
        return second != null ? second.equals(tuple2.second) : tuple2.second == null;
    }

    @Override
    public int hashCode() {
        int result = first != null ? first.hashCode() : 0;
        result = 31 * result + (second != null ? second.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Tuple2{" + "first=" + first +
                ", second=" + second +
                '}';
    }
}
