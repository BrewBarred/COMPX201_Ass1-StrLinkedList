public class Main {
    public static void main(String[] args) {
        System.out.println("Testing StrLinkedList");
        System.out.println();

        System.out.println("Creating an instance of the StrLinkedList...");
        StrLinkedList strList = new StrLinkedList();
        System.out.println("List is empty: " + strList.isEmpty() );
        System.out.println("List length is: " + strList.getLength());
        System.out.println();

        System.out.println("Adding values A,B,C to the StrLinkedList ...");
        strList.add("A");
        strList.add("B");
        strList.add("C");
        strList.print();
        System.out.println("List is empty: " + strList.isEmpty());
        System.out.println("List has value A: " + strList.hasValue("A"));
        System.out.println("List has value F: " + strList.hasValue("F"));
        System.out.println("List length is: " + strList.getLength());
        System.out.println();

        System.out.println("Removing value B...");
        strList.remove("B");
        strList.print();
        System.out.println("Removing value A...");
        strList.remove("A");
        strList.print();
        System.out.println("Removing value C...");
        strList.remove("C");
        strList.print();
        System.out.println("List length is: " + strList.getLength());
        System.out.println();

        System.out.println("Adding values A, B, C, B, D to the StrLinkedList ...");
        strList.add(new String[] {"A", "B", "C", "B", "D"});
        strList.print();
        System.out.println();

        System.out.println("Removing value B...");
        strList.remove("B");
        strList.print();
        System.out.println();

        System.out.println("Retrieving value at position 0...");
        System.out.println("The value at position 0: " + strList.getValueAt(0));
        System.out.println();

        System.out.println("Retrieving value at position 3...");
        System.out.println("The value at position 3: " + strList.getValueAt(3));

    } // end main

} // end class