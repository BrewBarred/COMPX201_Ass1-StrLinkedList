import java.util.NoSuchElementException;
import java.util.logging.Logger;

public class StrLinkedList {

    // Logger object for proper error logging
    private final Logger logger = Logger.getLogger(StrLinkedList.class.getName());
    private boolean debugging = false;
    // The first node in the list
    Node head;

    public static class Node{

        // The string being stored in this node
        private final String value;
        // The node that this node is pointing to
        private Node next;

        /**
         * Constructs a new node that stores a string and points to another null
         */
        public Node(String str){
            value = str;
            next = null;

        } // end constructor

        /**
         * Overrides the default toString() method to return the details of this node
         * @return An informative string containing the details of this node
         */
        @Override
        public String toString(){
            return "Node value: " + value;

        } // end string

    } // end class

    /**
     * Constructs a new linked list to store string values
     */
    public StrLinkedList(){
        head = null;

    } // end constructor

    /**
     * Checks if this list is currently empty or not
     * @return A boolean value that is true if the list is empty, else returns false
     */
    public boolean isEmpty(){
        // if the head is null, this list must be empty
        return head == null;

    } // end boolean

    /**
     * Gets the length of this list
     * @return An integer value denoting the length of this list (i.e., the number of nodes/elements)
     */
    public int getLength(){
        Node current = head;
        int length = 0;

        // iterates through the list and counts each node/element to determine the length of this list
        while (current != null){
            length++;
            current = current.next;

        } // end while

        return length;

    } // end int

    /**
     * Checks if this list contains the passed string value
     * @param string The string to search this list for
     * @return A boolean value that is true if the linked list contains
     * a node whose value is the same the passed string value
     */
    public boolean hasValue(String string){
        Node current = head;

        // iterates through each list value returning true if a match is found
        while (current != null){
            if (current.value.equals(string))
                return true;

            current = current.next;

        } // end while

        return false;

    } // end boolean

    /**
     * Takes an integer value, converts it to a string and checks if this list contains the passed value
     * @param value The value to search this list for
     * @return A boolean value that is true if this linked list contains
     * a node whose value is the same the passed value
     */
    public boolean hasValue(int value){
        return hasValue(String.valueOf(value));

    } // end boolean

    /**
     * Gets the value at the passed index of this list
     * @param index The index of the string value to return
     * @return The string value at the passed index of this list
     */
    public String getValueAt(int index){
        try {
            // check for invalid values
            if (getLength() == 0)
                throw new IndexOutOfBoundsException("Unable to return ");
            if (index < 0 || index > getLength() - 1)
                throw new IndexOutOfBoundsException("Failed to return value at index " + index + ", index was out of bounds!\n" +
                        " Please ensure passed index value is between 0 and " + (getLength() - 1));

            int i = 0;
            String string = "";
            Node current = head;

            // iterates through each node until the desired index is reached, returning that nodes value
            while (current != null){
                if (i == index)
                    return current.value;

                current = current.next;
                i++;

            } // end while

            return string;

        } catch (Exception error){
            logger.severe(error.getMessage());
            return null;

        } // end try

    } // end string

    /**
     * Creates a new node with the passed string value and adds it to the head of this list
     * @param string The value to add to the head of this list
     */
    public void add(String string){
        Node newNode = new Node(string);

        // if the head is not null, the new node should be pointing to it
        if (head != null)
            newNode.next = head;
        head = newNode;

        debug("Successfully added \"" + string + "\" to this list");

    } // end void

    /**
     * Creates a new node for each element in the passed string array and adds them to the head of this list
     * @Note: The string values will be added in the order that they appear in the array, starting from index 0
     * @param stringArray An array containing the values to add to the head of this list
     */
    public void add(String[] stringArray){
        for (String string : stringArray) {
            add(string);

        } // end for

    } // end void

    /**
     * Takes an integer value, converts it to a string value and adds it to the head of this list
     * @param value The integer value to be converted and added to this list
     */
    public void add(int value){
        add(String.valueOf(value));

    } // end void

    /**
     * Removes the node containing the first found occurrence of the passed string value from this list
     * @param string The string value to remove the list
     */
    public void remove(String string){
        try {
            if (isEmpty())
                throw new IllegalArgumentException("Failed to remove value \"" + string + "\", list is empty!");
            if (!hasValue(string))
                throw new NoSuchElementException("Failed to remove value \"" + string + "\", value not found!");

            debug("Removing value \"" + string + "\" from the StrLinkedList ...");

            // cuts head off if value is contained in the head
            if (head.value.equals(string)) {
                head = head.next;
                debug(this.toString());
                return;

            } // end if

            Node previous = head;
            Node current = head.next;

            // iterates through the list until the value is found, then removes it
            while(current != null){
                if(current.value.equals(string)) {
                    previous.next = current.next;
                    debug(this.toString());
                    return;

                } // end if

                // moves both pointers to the next nodes
                previous = current;
                current = current.next;

            } // end while

        }  catch (Exception error){
            logger.severe(error.getMessage());

        } // end try

    } // end void

    /**
     * Converts the passed integer value into a string, then removes the first found occurrence of it, if any exists
     * @param value The integer value to convert to a string and remove from this list
     */
    public void remove(int value){
        remove(String.valueOf(value));

    } // end void

    /**
     * Clears all elements currently contained within this list
     */
    public void clear(){
        head = null;

    } // end void

    /**
     * Constructs a detailed string of each node in this list
     * @return A string value containing the nodes value and the node that it is pointing to
     */
    @Override
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();

        if (!isEmpty()){
            Node current = head;

            // for each node in the list, print its value, followed by the node it is pointing to
            while (current != null){
                stringBuilder.append(current.value).append(" -> ");
                current = current.next;

            } // end while

        } // end if

        stringBuilder.append("null");
        return stringBuilder.toString();

    } // end string

    /**
     * Prints the string value of each node, followed by a pointer to
     * it's next node (->) in the order that they appear in the list
     */
    public void print(){
        System.out.println(this);

    } // end void

    public void debug(String msg){
        if (debugging)
            System.out.println(msg);

    } // end void

} // end class
