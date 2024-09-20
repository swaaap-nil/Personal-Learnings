public class Node {
    int index;
    int power;
    int bonus;
    Node(int index){
        this.index = index;
    }

    Node(int index,int power){
        this.index = index;
        this.power = power;
    }

    Node(int index,int power, int bonus){
        this.index = index;
        this.power = power;
        this.bonus = bonus;
    }

    @Override
    public String toString() {
        return "Node[index=" + index + ", power=" + power + ", bonus=" + bonus + "]";
    }

    @Override
    public boolean equals(Object other) {
    if (this == other) return true;
    if (other == null || getClass() != other.getClass()) return false;
    Node node = (Node) other;
    return index == node.index;
    }

}
