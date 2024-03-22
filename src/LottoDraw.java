import java.text.NumberFormat;
import java.util.*;

/**
 * This class simulates a lottery draw that generates a predefined amount of possible numbers to draw, storing them into
 * a 'lotto pool', it then randomly selects 6 numbers from the pool to form a winning ticket. A predefined number of lottery
 * tickets are then generated, each with 6 random numbers drawn from the same pool which are compared against the winning
 * ticket numbers. Finally, the total ticket sale revenue, total prize money paid, and total profit from the draw is calculated
 * and displayed.
 * Note: The lotto pool will not provide any duplicate numbers for a single ticket and is refilled after each ticket is generated.
 */
public class LottoDraw {
    /**
     * The amount of numbers each lotto ticket is generated with
     * (e.g., if this value is set to 6, each lotto ticket will be generated with 6 random numbers on it)
     */
    final int SIZE_LOTTO_TICKETS = 6;
    /**
     * The minimum number of matches required to be eligible for a prize (speeds up calculation process)
     */
    final int MIN_MATCHES = 2;
    /**
     * The amount of money awarded when the minimum amount of matches has been made. This reward is squared each time another match is made.
     */
    final int STARTING_PRIZE_VALUE = 10;
    /**
     * The amount of lotto numbers that can be drawn from the lotto pool
     * (e.g., if this value is set to 40, the lotto pool will be filled with numbers 1-40 inclusive)
     */
    final int SIZE_LOTTO_POOL = 40;
    /**
     * The amount of lotto tickets that are generated per draw
     */
    final int NUM_LOTTO_TICKETS = 100;
    /**
     * The cost of each lotto ticket in dollars ($)
     */
    final int TICKET_PRICE = 10;
    /**
     * The prize value pool: The index = matchCount and the value at each index is its respective prize amount
     * (e.g., The value at index 0 is the amount of money you will get if you get 0 matches, and the value at
     * index 6 is the amount of money you will get if you get 6 matches)
     */
    private StrLinkedList prizePool;
    /**
     * The random number object used to generate unique lotto tickets
     */
    private final Random RANDOM = new Random();
    /**
     * A list of the possible numbers that can be drawn at random for a lotto ticket
     */
    private final StrLinkedList LOTTO_POOL = new StrLinkedList();
    /**
     * The total revenue gained from sales
     */
    private int totalSales = 0;
    /**
     * The total prize money awarded from winning tickets
     */
    private int totalPrizeMoney = 0;
    /**
     * The total profit made in sales after prize money was awarded
     */
    private int totalProfit = 0;
    /**
     * The numbers that each ticket is compared against for validation
     * (i.e., foreach of these numbers that appear on another ticket, the prize value for that ticket will be incremented)
     */
    private LottoTicket winningTicketNumbers;

    /**
     * Class to define the properties of a lotto ticket
     */
    public class LottoTicket{
        /**
         * The lotto numbers that this ticket contains
         */
        protected StrLinkedList numbers = new StrLinkedList();
        /**
         * The number of winning numbers this ticket contains
         */
        protected int matchCount = 0;
        /**
         * The total prize money that this ticket is worth
         */
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
        @SuppressWarnings("ConstantConditions")
        public void resetLottoPool(){
            // checks ticket length against pool size in-case someone hard codes a lotto ticket value that's bigger than the lotto pool
            if (SIZE_LOTTO_POOL < SIZE_LOTTO_TICKETS)
                throw new IllegalStateException("Invalid lotto ticket/lotto pool size! Lotto ticket size cannot be greater than the pool size!");

            // resets and refills the lotto pool otherwise it would be impossible to win
            LOTTO_POOL.clear();
            for(int i = 1; i < SIZE_LOTTO_POOL + 1; i++)
                LOTTO_POOL.add(i);

        } // end void

        /**
         * Generates a lotto ticket made up of a predefined amount of random numbers
         */
        public void generateTicket(){
            resetLottoPool();

            // generates a random number and adds it to this lotto ticket
            while(numbers.getLength() != SIZE_LOTTO_TICKETS){
                int lottoNumber = RANDOM.nextInt(1, SIZE_LOTTO_POOL + 1);

                // ensures no duplicate numbers are added to this ticket
                if (LOTTO_POOL.hasValue(lottoNumber)) {
                    numbers.add(lottoNumber);
                    LOTTO_POOL.remove(lottoNumber);

                } // end if

            } // end while

        } // end void

        /**
         * Calculates the total prize value of this ticket by comparing its numbers against the winningTicketNumbers
         */
        private void calcPrizeMoney(){
            matchCount = 0;
            prizeMoney = 0;

            if(winningTicketNumbers == null){
                throw new NoSuchElementException("Unable to calculate prize value, winning numbers have not been defined yet!");

            } // end if

            // counts how many numbers on this ticket appear in the winning ticket numbers
            for (int i = 0; i < numbers.getLength(); i++){
                if (winningTicketNumbers.hasValue(numbers.getValueAt(i)))
                    matchCount++;

            } // end for

            // ensures that any number of matches will never exceed the jackpot value
            // (will only happen if ticket size is manually increased without adding extra prizes)
            if (matchCount > prizePool.getLength() - 1)
                matchCount = prizePool.getLength() - 1;

            // updates this tickets prize value based on the number of matches found
            prizeMoney = Integer.parseInt(prizePool.getValueAt(matchCount));

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
            System.out.println("Prize won: " + getPrizeMoney() + " Ticket: " + this + " (" + matchCount + " winning numbers)");

        } // end void

    } // end class

    /**
     * The entry point for this class, this handles each lotto draw
     */
    public static void main(String[] args){
        LottoDraw lottoDraw = new LottoDraw();
        //lottoDraw.startDraw(lottoDraw);
        lottoDraw.testJackpot(lottoDraw); // <- Remove comments to test jackpot (may take time for a successful jackpot)

    } // end void

    /**
     * Starts a new lotto draw, generating a random set of winning numbers, then generates a pre-defined number of tickets, comparing each ticket number
     * against the winning numbers and finally, calculates the prize money earned from each ticket in this draw and displays the results to the console.
     * @param lottoDraw The lottoDraw instance that is being started
     */
    public void startDraw(LottoDraw lottoDraw){
        // fills the prize pool
        lottoDraw.generatePrizes();
        // sets winning lotto numbers for this draw
        lottoDraw.generateWinningNumbers();
        System.out.println("Generating lotto tickets... Please wait...");
        // creates each lotto ticket being used in this draw
        lottoDraw.generateLottoTickets();
        System.out.println("Checking winning tickets...");
        // checks each ticket and calculates winnings
        lottoDraw.checkTickets();
        // prints results to the console
        lottoDraw.print();

    } // end void

    /**
     * Generates the prize pool - This is a list of numbers in which the value at each index is equal to the prize money earned
     * for that number of matches (i.e., value at index 0 = prize money for 0 matches, similarly, the value at index 6 = prize money for 6 matches)
     * The prize amounts are dependent on the amount of numbers on each ticket, and the value at each index will be equal to 0 until the index is
     * greater than the minimum number of matches required.
     */
    @SuppressWarnings("ConstantConditions")
    public void generatePrizes(){
        try {
            prizePool = new StrLinkedList();
            int prizeAmount = 0;

            // ensures the prizes are actually winnable before continuing - this will only fail is the minimum matches is manually set higher than the lotto ticket size
            if (MIN_MATCHES > SIZE_LOTTO_TICKETS)
                throw new UnsupportedOperationException(
                        "Unable to generate a valid prize pool! The number of matches required to win is greater than the amount of numbers per ticket!\n"
                                + " Matches required: " + MIN_MATCHES + ", Amount of numbers per ticket: " + SIZE_LOTTO_TICKETS);

            // generates prize pool values based on how many numbers each lotto
            // ticket has and the minimum number of matches required for a prize
            for (int i = 0; i < SIZE_LOTTO_TICKETS; i++) {
                // once the minimum amount of matches has been made, increments prize money
                if (i % MIN_MATCHES == 0)
                    prizeAmount += STARTING_PRIZE_VALUE;

                // adds the current prize amount to the prize pool
                prizePool.add("" + prizeAmount);
                // squares the current prize amount
                prizeAmount *= prizeAmount;

        } // end for

        } catch (Exception e){
            e.printStackTrace();

        } // end try

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
            LottoTicket ticket = new LottoTicket();
            lottoTickets.add(ticket);

        } // end for

    } // end void

    /**
     * Checks all tickets and calculates the total sale revenue, prize values and profit gained from this draw
     */
    public void checkTickets(){
        totalSales = lottoTickets.size() * TICKET_PRICE;

        // iterates through each ticket in the lotto ticket list to calculate the total prize money
        for(LottoTicket ticket : lottoTickets){
            ticket.calcPrizeMoney();
            if (ticket.matchCount >= MIN_MATCHES) {
                totalPrizeMoney += ticket.prizeMoney;

            } // end if

        } // end for

        totalProfit = totalSales - totalPrizeMoney;

    } // end void

    /**
     * Displays the information of each ticket (i.e., The prize amount & ticket numbers)
     */
    public void displayTickets(){
        System.out.println("Tickets bought:");
        // iterates through each lotto ticket in the lottoTickets hashmap to calculate the total prize money
        for(LottoTicket ticket : lottoTickets){
            ticket.print();

        } // end for

    } // end void

    /**
     * Displays each prize value to the console
     * @return A string value containing each possible prize value
     */
    public String getPrizeValues(){
        StringBuilder prizeValues = new StringBuilder();

        // iterates through the prize pool to display the number of matches
        // (1st element) required to get the respective prize money (2nd element)
        for(int i = 0; i < prizePool.getLength(); i++){
            prizeValues.append(prizePool.getValueAt(i));
            if (i != prizePool.getLength() - 1)
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
        // creates a currency formatter
        NumberFormat currency = NumberFormat.getCurrencyInstance();
        // uses currency formatter to convert passed value into currency format and returns the result
        return currency.format(value);

    } // end string

    /**
     * Prints the details of this draw including total tickets printed, revenue, prize money awarded, profit etc., to the console
     */
    public void print(){
        System.out.println("\nFull number list: " + LOTTO_POOL.toString().replace(" -> null", "")
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
    @SuppressWarnings("unused")
    private void testJackpot(LottoDraw testDraw){
        int i = 0;
        System.out.println("Testing jackpot odds... Please be patient as this may take a few minutes...");

        // keeps repeating lotto draws until a jackpot is hit
        while (testDraw.totalPrizeMoney <= 10000) {
            testDraw.startDraw(testDraw);
            i++;
            // intermittently tracks iteration count to show user that the application is still working
            if (i % 210 == 0)
                System.out.println("Jackpot attempt: " + i);

        } // end while

        System.out.println("Draws taken to win jackpot: " + i);

    } // end void

} // end class
