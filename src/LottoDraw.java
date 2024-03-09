import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * This class simulates a lottery draw that generates a predefined amount of possible numbers,
 * and randomly selects 6 of those numbers to form a winning ticket, then generates 100 lottery
 * tickets each with 6 random numbers, then compares these tickets to the winning ticket to
 * calculate the total sales, prize money and profit from the draw.
 */
public class LottoDraw {
    // The amount of numbers each lotto ticket can have
    // (e.g., if this value is set to 6, each lotto ticket will have 6 random numbers on it)
    private final int SIZE_LOTTO_TICKETS = 6;
    // The amount of lotto numbers that can be drawn from the lotto pool
    // (e.g., if this value is set to 40, the lotto pool will be filled with numbers 1-40 inclusive)
    private final int SIZE_LOTTO_POOL = 40;
    // The amount of lotto tickets to be generated per draw
    private final int NUM_LOTTO_TICKETS = 100;
    // The cost of a lotto ticket in dollars ($)
    private final int TICKET_PRICE = 10;
    // The prize values where the 1st element = match count, and the 2nd element = prize value
    // (i.e., PRIZE_VALUES[0][0] = 0 matches, $0, and PRIZE_VALUES[6][0] = 6 matches, $10,000)
    private final int[][] PRIZE_VALUES = {{0, 0}, {1, 0}, {2, 0}, {3, 10}, {4, 100}, {5, 1000}, {6, 10000}};
    // The total revenue gained from sales
    private int totalSales = 0;
    // The total prize money awarded from winning tickets
    private int totalPrizeMoney = 0;
    // The total profit made in sales after prize money was awarded
    private int totalProfit = 0;
    // The random number object used to generate unique lotto tickets
    private Random random = new Random();
    // A list of the possible numbers that can be drawn at random for a lotto ticket
    private StrLinkedList lottoPool = new StrLinkedList();
    // The numbers that each ticket is compared against for validation
    // (i.e., foreach of these numbers that appear on another ticket, the prize value for that ticket will be incremented)
    private LottoTicket winningTicketNumbers;
    // A hash map containing the nth lotto ticket sold as a key, a string list of its numbers as a key
    private Map<Integer, LottoTicket> lottoTickets = new HashMap<>();

    /**
     * Class to define the properties of a lotto ticket
     */
    public class LottoTicket{
        // The lotto numbers that this ticket contains
        protected StrLinkedList numbers = new StrLinkedList();
        // The total prize money that this ticket is worth
        private int prizeValue = 0;

        /**
         * Constructs a new lotto ticket with random numbers drawn from the lotto pool
         */
        public LottoTicket(){
            generateTicket();

        } // end ticket

        /**
         * Iterates through each of the numbers this ticket has to check if it contains a passed value
         * @param value The value to search for in this list
         * @return True if the passed value is found in this list, else returns false
         */
        public boolean hasValue(String value){
            return numbers.hasValue(value);

        } // end boolean

        /**
         * Fills the lotto pool (i.e., the set of numbers that each ticket can be made up of)
         */
        public void fillLottoPool(){
            lottoPool.clear();

            for(int i = 1; i < SIZE_LOTTO_POOL + 1; i++)
                lottoPool.add(i);

        } // end void

        /**
         * Generates a lotto ticket made up of random numbers
         */
        public void generateTicket(){
            fillLottoPool();

            // generates a random number and adds it to this lotto ticket
            while(numbers.getLength() != SIZE_LOTTO_TICKETS){
                int lottoNumber = random.nextInt(1, SIZE_LOTTO_POOL + 1);

                // ensures no duplicate numbers are added to this ticket
                if (lottoPool.hasValue(lottoNumber)) {
                    numbers.add(lottoNumber);
                    lottoPool.remove(lottoNumber);

                } // end if

            } // end while
            
            calcValue();

        } // end void

        /**
         * Calculates the total prize value of this ticket by comparing its numbers against the winningTicketNumbers
         */
        private void calcValue(){
            if(winningTicketNumbers == null){
                System.out.println("Unable to calculate prize value, no winning numbers have been defined!");
                return;

            } // end if

            int matchCount = 0;

            // Counts how many numbers on this ticket appears in the winning ticket numbers
            for (int i = 0; i < numbers.getLength(); i++){
                if (winningTicketNumbers.hasValue(numbers.getValueAt(i)))
                    matchCount++;

            } // end for

            // updates the prize value based on the number of matches found
            prizeValue = PRIZE_VALUES[matchCount][1];

        } // end void

        /**
         * Calculates and returns this tickets prize value
         * @return A string formatted as a currency value that represents the total prize value for this ticket
         */
        public String getPrizeValue(){
            // adds appropriate separators for prize money integer value for a currency value appearance
            NumberFormat prizeMoney = NumberFormat.getInstance();
            return String.format("$" + prizeMoney.format(prizeValue) + ".00");

        } // end int

        /**
         * Gets this tickets numbers
         * @return A string value containing each number on this ticket
         */
        @Override
        public String toString(){
           return numbers.toString().replace("-> null","");

        } // end string

        /**
         * Prints information about this ticket (i.e., Prize money won + ticket numbers)
         */
        public void print(){
            System.out.println("Prize won: " + getPrizeValue() + " Ticket: " + this);

        } // end void

    } // end class

    /**
     * The entry point for this class, this handles the lotto draw
     */
    public static void main(String[] args){
        LottoDraw lottoDraw = new LottoDraw();
        lottoDraw.testJackpot(lottoDraw);
//        lottoDraw = new LottoDraw();
//        lottoDraw.generateWinningNumbers();
//        lottoDraw.generateLottoTickets();
//        lottoDraw.print();

    } // end void

    /**
     * Generates random values to compare lotto tickets against, each of these numbers
     * will increment the prize value of a lotto ticket
     */
    public void generateWinningNumbers(){
        winningTicketNumbers = new LottoTicket();

    } // end void

    /**
     * Generates a preset number of lotto tickets with random numbers and stores them into the lottoTicket hash map
     */
    public void generateLottoTickets(){
        for(int i = 0; i < NUM_LOTTO_TICKETS; i++){
            lottoTickets.put(i, new LottoTicket());

        } // end for

    } // end void

    /**
     * Checks all tickets and calculates the total sale revenue, prize values and profit gained from this draw
     */
    public void checkTickets(){
        totalSales = lottoTickets.size() * TICKET_PRICE;

        System.out.println("Tickets bought:");
        // iterates through each lotto ticket in the lottoTickets hashmap to calculate the total prize money
        for(LottoTicket ticket : lottoTickets.values()){
            totalPrizeMoney += ticket.prizeValue;
            ticket.print();

        } // end for

        totalProfit = totalSales - totalPrizeMoney;

    } // end void

    /**
     * Displays each prize value to the console
     * @return A string value containing each possible prize value
     */
    public String getPrizeValues(){
        StringBuilder prizeValues = new StringBuilder();

        for(int i = 0; i < PRIZE_VALUES.length; i++){
            prizeValues.append(PRIZE_VALUES[i][1]);
            if (i != PRIZE_VALUES.length - 1)
                prizeValues.append(" -> ");

        } // end for

        return prizeValues.toString();

    } // end void

    public String formatCurrency(int value){
        NumberFormat currency = NumberFormat.getCurrencyInstance();
        return currency.format(value);

    } // end string

    /**
     * Keeps repeating lotto draws until a jackpot is hit for testing purposes
     */
    private void testJackpot(LottoDraw lottoDraw){
        int i = 0;
        lottoDraw.generateWinningNumbers();

        // keeps repeating lotto draws until jackpot is reached then returns the number of attemps it took
        while (lottoDraw.totalPrizeMoney <= 1000) {
            lottoDraw = new LottoDraw();
            lottoDraw.generateWinningNumbers();
            lottoDraw.generateLottoTickets();
            lottoDraw.checkTickets();
            i++;

        } // end while

        lottoDraw.print();
        System.out.println("Draws taken to win jackpot: " + i);

    } // end void

    /**
     * Prints the details of this draw including total tickets printed, revenue, prize money awarded, profit etc., to the console
     */
    public void print(){
        // adds appropriate separators for prize money integer value for a currency value appearance
        NumberFormat currency = NumberFormat.getInstance();

        System.out.println("\nFull number list: " + lottoPool.toString().replace(" -> null", "")
                + "\nWinning numbers: " + winningTicketNumbers.toString()
                + "\nPrize money: " + getPrizeValues()
                + "\n");
        checkTickets();
        System.out.println("\nTotal tickets sold: " + lottoTickets.size()
                + "\nTotal earnings: " + formatCurrency(totalSales)
                + "\nTotal prize money won: " + formatCurrency(totalPrizeMoney)
                + "\nTotal profit: " + formatCurrency(totalProfit));
        
    } // end void

} // end class
