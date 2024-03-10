import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Random;

/**
 * This class simulates a lottery draw that generates a predefined amount of possible numbers to draw, storing them into
 * a 'lotto pool', it then randomly selects 6 numbers from the pool to form a winning ticket. A predefined number of lottery
 * tickets are then generated, each with 6 random numbers drawn from the same pool which are compared against the winning
 * ticket numbers. Finally, the total ticket sale revenue, total prize money paid, and total profit from the draw is calculated
 * and displayed.
 * Note: The lotto pool will not provide any duplicate numbers for a single ticket and is refilled after each ticket is generated.
 */
public class LottoDraw {
    // The amount of numbers each lotto ticket has
    // (e.g., if this value is set to 6, each lotto ticket will be generated with 6 random numbers on it)
    final int SIZE_LOTTO_TICKETS = 6;
    // The amount of lotto numbers that can be drawn from the lotto pool
    // (e.g., if this value is set to 40, the lotto pool will be filled with numbers 1-40 inclusive)
    final int SIZE_LOTTO_POOL = 40;
    // The amount of lotto tickets to be generated per draw
    final int NUM_LOTTO_TICKETS = 100;
    // The cost of each lotto ticket in dollars ($)
    final int TICKET_PRICE = 10;
    // The prize values where the 1st element = match count, and the 2nd element = prize value
    // (i.e., PRIZE_VALUES[0][0] = 0 matches, $0, and PRIZE_VALUES[6][0] = 6 matches, $10,000)
    private final int[][] PRIZE_POOL = {{0, 0}, {1, 0}, {2, 0}, {3, 10}, {4, 100}, {5, 1000}, {6, 10000}};
    // The total revenue gained from sales
    private int totalSales = 0;
    // The total prize money awarded from winning tickets
    private int totalPrizeMoney = 0;
    // The total profit made in sales after prize money was awarded
    private int totalProfit = 0;
    // The random number object used to generate unique lotto tickets
    private final Random random = new Random();
    // A list of the possible numbers that can be drawn at random for a lotto ticket
    private final StrLinkedList lottoPool = new StrLinkedList();
    // The numbers that each ticket is compared against for validation
    // (i.e., foreach of these numbers that appear on another ticket, the prize value for that ticket will be incremented)
    private LottoTicket winningTicketNumbers;
    // A hash map containing the nth lotto ticket sold as a key, a string list of its numbers as a key
    private final Map<Integer, LottoTicket> lottoTickets = new HashMap<>();

    /**
     * Class to define the properties of a lotto ticket
     */
    public class LottoTicket{
        // The lotto numbers that this ticket contains
        protected StrLinkedList numbers = new StrLinkedList();
        // The number of winning numbers this ticket contains
        protected int matches = 0;
        // The total prize money that this ticket is worth
        private int prizeMoney = 0;

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
         * Resets the 'lotto pool' (i.e., the set of numbers that each ticket can be made up of)
         */
        public void resetLottoPool(){
            if (SIZE_LOTTO_POOL < SIZE_LOTTO_TICKETS)
                throw new IllegalStateException("Invalid lotto ticket/lotto pool size! Lotto ticket size cannot be greater than the pool size!");

            // resets the lotto pool
            lottoPool.clear();
            for(int i = 1; i < SIZE_LOTTO_POOL + 1; i++)
                lottoPool.add(i);

        } // end void

        /**
         * Generates a lotto ticket made up of random numbers
         */
        public void generateTicket(){
            resetLottoPool();

            // generates a random number and adds it to this lotto ticket
            while(numbers.getLength() != SIZE_LOTTO_TICKETS){
                int lottoNumber = random.nextInt(1, SIZE_LOTTO_POOL + 1);

                // ensures no duplicate numbers are added to this ticket
                if (lottoPool.hasValue(lottoNumber)) {
                    numbers.add(lottoNumber);
                    lottoPool.remove(lottoNumber);

                } // end if

            } // end while

        } // end void

        /**
         * Calculates the total prize value of this ticket by comparing its numbers against the winningTicketNumbers
         */
        private void calcPrizeMoney(){
            matches = 0;
            prizeMoney = 0;

            if(winningTicketNumbers == null){
                throw new NoSuchElementException("Unable to calculate prize value, no winning numbers have been defined!");

            } // end if

            // counts how many numbers on this ticket appear in the winning ticket numbers
            for (int i = 0; i < numbers.getLength(); i++){
                if (winningTicketNumbers.hasValue(numbers.getValueAt(i)))
                    matches++;

            } // end for

            // updates the prize value based on the number of matches found
            prizeMoney = PRIZE_POOL[matches][1];

        } // end void

        /**
         * Calculates and retrieves this tickets prize value
         * @return A string formatted as a currency value that represents the total prize value for this ticket
         */
        public String getPrizeMoney(){
            calcPrizeMoney();
            return formatCurrency(prizeMoney);

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
            System.out.println("Prize won: " + getPrizeMoney() + " Ticket: " + this + " Winning numbers: " + matches);

        } // end void

    } // end class

    /**
     * The entry point for this class, this handles the lotto draw
     */
    public static void main(String[] args){
        LottoDraw lottoDraw = new LottoDraw();
        lottoDraw.generateWinningNumbers();
        //lottoDraw.testJackpot(lottoDraw); // <- Remove comments to test jackpot (may take time for a successful jackpot)
        System.out.println("Generating lotto tickets... Please wait...");
        lottoDraw.generateLottoTickets();
        System.out.println("Checking winning tickets...");
        lottoDraw.checkTickets();
        lottoDraw.print();

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

        // iterates through each lotto ticket in the lottoTickets hashmap to calculate the total prize money
        for(LottoTicket ticket : lottoTickets.values()){
            ticket.calcPrizeMoney();
            totalPrizeMoney += ticket.prizeMoney;

        } // end for

        totalProfit = totalSales - totalPrizeMoney;

    } // end void

    /**
     * Displays the information of each ticket (i.e., The prize amount & ticket numbers)
     */
    public void displayTickets(){
        System.out.println("Tickets bought:");
        // iterates through each lotto ticket in the lottoTickets hashmap to calculate the total prize money
        for(LottoTicket ticket : lottoTickets.values()){
            ticket.print();

        } // end for

    } // end void

    /**
     * Displays each prize value to the console
     * @return A string value containing each possible prize value
     */
    public String getPrizeValues(){
        StringBuilder prizeValues = new StringBuilder();

        for(int i = 0; i < PRIZE_POOL.length; i++){
            prizeValues.append(PRIZE_POOL[i][1]);
            if (i != PRIZE_POOL.length - 1)
                prizeValues.append(" -> ");

        } // end for

        return prizeValues.toString();

    } // end void

    /**
     * Formats the passed integer value into a currency value, ready for display
     * @param value The integer value to be formatted into a currency value
     * @return A string value denoting
     */
    public String formatCurrency(int value){
        NumberFormat currency = NumberFormat.getCurrencyInstance();
        return currency.format(value);

    } // end string

    /**
     * Prints the details of this draw including total tickets printed, revenue, prize money awarded, profit etc., to the console
     */
    public void print(){
        System.out.println("\nFull number list: " + lottoPool.toString().replace(" -> null", "")
                + "\nWinning numbers: " + winningTicketNumbers.toString()
                + "\nPrize money: " + getPrizeValues()
                + "\n");

        displayTickets();

        System.out.println("\nTotal tickets sold: " + lottoTickets.size()
                + "\nTotal earnings: " + formatCurrency(totalSales)
                + "\nTotal prize money won: " + formatCurrency(totalPrizeMoney)
                + "\nTotal profit: " + formatCurrency(totalProfit));
        
    } // end void

    /**
     * Keeps repeating lotto draws until a jackpot is hit (This is purely for testing purposes and may take
     * a while to hit a jackpot, so please be patient)
     */
    private void testJackpot(LottoDraw testDraw){
        int i = 0;
        LottoDraw lottoDraw = testDraw;
        System.out.println("Testing jackpot odds... Please be patient as this may take a few minutes...");

        // keeps repeating lotto draws until a jackpot is hit
        while (lottoDraw.totalPrizeMoney <= 10000) {
            lottoDraw = new LottoDraw();
            lottoDraw.generateWinningNumbers();
            lottoDraw.generateLottoTickets();
            lottoDraw.checkTickets();
            i++;
            // intermittently tracks iteration count to show user that the application is still working
            if (i % 210 == 0)
                System.out.println("Jackpot attempt: " + i);

        } // end while

        lottoDraw.print();
        System.out.println("Draws taken to win jackpot: " + i);

    } // end void

} // end class
