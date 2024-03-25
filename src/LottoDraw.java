import java.text.NumberFormat;
import java.util.NoSuchElementException;
import java.util.Random;

/**
 * This class simulates a lottery draw that generates a predefined amount of possible numbers to draw, storing them into
 * a 'lotto pool'. It then randomly selects 6 numbers from the pool to form a winning ticket. A predefined number of lottery
 * tickets are then generated, each with 6 random numbers drawn from the same pool which are compared against the winning
 * ticket numbers. Finally, the total ticket sale revenue, total prize money paid, and total profit from the draw is calculated
 * and displayed.
 * Note: The lotto pool will not provide any duplicate numbers for a single ticket and is refilled after each ticket is generated.
 */
public class LottoDraw {
    /**
     * The amount of numbers that each lotto ticket shall contain
     * (i.e., if this value is set to 6, each lotto ticket will be generated with 6 random numbers on it)
     */
    private final int SIZE_LOTTO_TICKETS = 6;
    /**
     * The minimum number of matches required to be eligible for a prize (i.e., any number of matches less than this value will yield a ticket value of $0)
     */
    private final int MIN_MATCHES = 2;
    /**
     * The highest amount of money that can be won. This reward is divided by the prize multiplier to form
     * each of the other prize values until the minimum match count makes the remaining prize amounts $0
     */
    private final int MAX_PRIZE_VALUE = 100000;
    /**
     * The divisor that is used with the maximum prize value to dictate the other prize values
     * (i.e., if the max prize value = 100k, and the divisor is 10, the 2nd best prize value would be: 100k / 10 = 10k and so fourth...)
     */
    private final int PRIZE_DIVISOR = 10;
    /**
     * The amount of lotto numbers that can be drawn from the lotto pool
     * (e.g., if this value is set to 40, the lotto pool will be filled with numbers 1-40 inclusive)
     */
    private final int LOTTO_POOL_SIZE = 40;
    /**
     * The number of lotto tickets that are generated per draw
     */
    private final int NUM_LOTTO_TICKETS = 100;
    /**
     * The cost of each lotto ticket in dollars ($)
     */
    private final int TICKET_PRICE = 10;
    /**
     * The random number object used to generate unique lotto tickets
     */
    private final Random RANDOM = new Random();
    /**
     * The list of possible numbers that can be drawn for a lotto ticket
     */
    private StrLinkedList lottoPool = new StrLinkedList();
    /**
     * Stores the prizes that can be won based on a lotto tickets match count.
     * (i.e., The value at index 0 is the amount of money you will get if you get 0 matches, and the value at
     * index 6 is the amount of money you will get if you get 6 matches)
     */
    private StrLinkedList prizePool;
    /**
     * The numbers that each ticket is compared against for prize validation
     * (i.e., foreach of these numbers that appear on a given ticket, the match count for
     * that ticket will be incremented which, in turn, will increment its prize value)
     */
    private LottoTicket winningTicketNumbers;
    /**
     * The total number of tickets sold in this draw
     */
    private int totalSales = 0;
    /**
     * The total revenue gained from sales
     */
    private int totalSaleAmount = 0;
    /**
     * The total prize money awarded from winning tickets
     */
    private int totalPrizeAmount = 0;

    /**
     * Class to define the properties of a lotto ticket
     */
    public class LottoTicket{
        /**
         * The lotto numbers that this ticket contains
         */
        private final StrLinkedList ticketNumbers = new StrLinkedList();
        /**
         * The number of winning numbers this ticket contains
         */
        private int matchCount = 0;
        /**
         * The total amount of prize money that this ticket is worth in dollars ($)
         */
        private int value = 0;

        /**
         * Constructs a new lotto ticket using random numbers drawn from the lotto pool
         */
        public LottoTicket(){
            generateTicket();

        } // end ticket

        /**
         * Iterates through each of the numbers this ticket has, to check if it contains the passed value
         * @param value The value to search for in this list
         * @return True if the passed value is found in this list, else returns false
         */
        public boolean hasValue(String value){
            return ticketNumbers.hasValue(value);

        } // end boolean

        /**
         * Generates a lotto ticket made up of a predefined amount of random numbers
         */
        public void generateTicket(){
            resetLottoPool();

            // foreach lotto number this ticket requires, takes a random
            // number from the lotto pool and adds it to this lotto ticket
            while(ticketNumbers.getLength() != SIZE_LOTTO_TICKETS){
                int index = RANDOM.nextInt(0, lottoPool.getLength());
                String lottoNumber = lottoPool.getValueAt(index);

                ticketNumbers.add(lottoNumber);
                // removes this number from the pool to prevent duplicates
                lottoPool.remove(lottoNumber);

            } // end while

        } // end void

        /**
         * Calculates the total prize value of this ticket by comparing its numbers against the winningTicketNumbers
         */
        private void calcPrizeMoney(){
            matchCount = 0;
            value = 0;

            if(prizePool == null)
                throw new NoSuchElementException("Unable to calculate prize value, prize pool was not initiated!");

            // counts how many numbers on this ticket appear in the winning ticket numbers
            for (int i = 0; i < ticketNumbers.getLength(); i++){
                if (winningTicketNumbers.hasValue(ticketNumbers.getValueAt(i)))
                    matchCount++;

            } // end for

            // Ensures the matchCount cannot exceed the prize pool size
            // (this will only happen if ticket size is manually increased without adding extra prizes to the prize pool)
            if (matchCount > prizePool.getLength() - 1)
                matchCount = prizePool.getLength() - 1;

            // updates this tickets prize value based on the number of matches found
            value = Integer.parseInt(prizePool.getValueAt(matchCount));

        } // end void

        /**
         * Calculates and retrieves this tickets prize value
         * @return A string formatted as a currency value that represents the total prize value for this ticket
         */
        public String getValue(){
            calcPrizeMoney();
            return formatCurrency(value);

        } // end int

        /**
         * Gets this tickets numbers
         * @return A string value containing each number on this ticket
         */
        @Override
        public String toString(){
           return ticketNumbers.toString().replace(" -> null","");

        } // end string

        /**
         * Prints information about this ticket (i.e., Prize money won + ticket numbers)
         */
        public void print(){
            System.out.println("Prize won: " + getValue() + ", Ticket: " + this + " (" + matchCount + " winning numbers)");

        } // end void

    } // end class

    /**
     * The entry point for this class, this handles each lotto draw
     */
    public static void main(String[] args){
        LottoDraw lottoDraw = new LottoDraw();
        lottoDraw.startDraw(lottoDraw);
          //Remove comments to test how many attempts it takes to hit a jackpot with the current settings
          //(WARNING: This may take a while - often takes over 2 million tickets before a jackpot is reached when printing 100 tickets per game)
        //lottoDraw.testJackpot(lottoDraw);

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
            System.out.println("\nGenerating lotto tickets... Please wait...\n");
            // creates each lotto ticket being used in this draw
            lottoDraw.generateLottoTickets();
            // prints results to the console
            lottoDraw.print();

    } // end void

    /**
     * Generates the prize pool - This is a list of numbers in which the value at each index is equal to the prize money earned
     * for that number of matches (i.e., value at index 0 = prize money for 0 matches, similarly, the value at index 6 = prize money for 6 matches)
     */
    @SuppressWarnings("ConstantConditions")
    public void generatePrizes(){
        try {
            // ensures the prizes are actually winnable before continuing - this will only fail is the minimum matches is manually set higher than the lotto ticket size
            if (MIN_MATCHES > SIZE_LOTTO_TICKETS)
                throw new UnsupportedOperationException(
                        "Unable to generate a valid prize pool! The number of matches required to win is greater than the amount of numbers per ticket!"
                        + "\nMatches required: " + MIN_MATCHES + ", Amount of numbers per ticket: " + SIZE_LOTTO_TICKETS);

            prizePool = new StrLinkedList();
            int prizeAmount = MAX_PRIZE_VALUE;

            // generates prize pool values based on how many numbers each lotto
            // ticket has and the minimum number of matches required for a prize
            for (int i = SIZE_LOTTO_TICKETS; i >= 0; i--) {
                // sets the remaining values to 0 once the iteration count gets down to the minimum matches value.
                if (i == MIN_MATCHES - 1)
                    prizeAmount = 0;

                // adds the current prize amount to the prize pool
                prizePool.add(String.valueOf(prizeAmount));
                // squares the current prize amount
                prizeAmount /= PRIZE_DIVISOR;

            } // end for

        } catch (Exception e){
            e.printStackTrace();
            System.exit(1);

        } // end try

    } // end void

    /**
     * Resets the 'lotto pool' (i.e., the set of numbers that each ticket can be made up of)
     */
    @SuppressWarnings("ConstantConditions")
    public void resetLottoPool(){
        // checks ticket length against pool size in-case someone hard codes a lotto ticket value that's bigger than the lotto pool
        if (LOTTO_POOL_SIZE < SIZE_LOTTO_TICKETS)
            throw new IllegalStateException("Invalid lotto ticket/lotto pool size! Lotto ticket size cannot be greater than the pool size!");

        // resets and refills the lotto pool otherwise it would be impossible to win
        lottoPool.clear();
        for(int i = LOTTO_POOL_SIZE; i > 0; i--)
            lottoPool.add(i);

    } // end void

    /**
     * Generates random values to compare lotto tickets against, each of these numbers
     * will increment the prize value of a lotto ticket
     */
    public void generateWinningNumbers(){
        // creates a winning lotto ticket to generate the numbers that each ticket in this draw should be compared against
        winningTicketNumbers = new LottoTicket();

    } // end void

    /**
     * Generates a preset number of lotto tickets with random numbers and stores them into the lottoTicket hash map
     */
    public void generateLottoTickets(){
        // generates the set amount of lotto tickets that this draw should have
        for(int i = 0; i < NUM_LOTTO_TICKETS; i++)
            // simultaneously creates and checks a lotto ticket
            checkTicket(new LottoTicket());

    } // end void

    /**
     * Checks the passed ticket and calculates its total sale revenue and total prize value
     */
    public void checkTicket(LottoTicket ticket){
        totalSales++;
        totalSaleAmount += TICKET_PRICE;
        ticket.calcPrizeMoney();
        totalPrizeAmount += ticket.value;
        ticket.print();

    } // end void

    /**
     * Calculates and returns the total profit made from this draw (i.e., totalSaleAmount - totalPrizeAmount)
     * @return A currency formatted string denoting the total profit made in this draw
     */
    public String calcProfit(){
        return formatCurrency(totalSaleAmount - totalPrizeAmount);

    } // end void

    /**
     * Formats the passed integer value into a currency value, ready for display
     * @param value The integer value to be formatted into a currency value
     * @return A string value denoting
     */
    public String formatCurrency(int value){
        NumberFormat currency = NumberFormat.getCurrencyInstance();
        // uses currency formatter to return the passed value in currency format
        return currency.format(value);

    } // end string

    /**
     * Prints the details of this draw including total tickets printed, revenue, prize money awarded, profit etc., to the console
     */
    public void print(){
        resetLottoPool();
        calcProfit();

        System.out.println("\nLotto Pool:\n" + lottoPool.toString().replace(" -> null", "")
                + "\nWinning numbers:\n" + winningTicketNumbers.toString()
                + "\nPrize pool:\n" + prizePool.toString().replace(" -> null", "") + "\n"
                + "\nTicket price: " + formatCurrency(TICKET_PRICE)
                + "\nTotal tickets sold: " + totalSales
                + "\nTotal earnings: " + formatCurrency(totalSaleAmount)
                + "\nTotal prize money paid: " + formatCurrency(totalPrizeAmount)
                + "\nTotal profit from this draw: " + calcProfit() + "\n");
        
    } // end void

    /**
     * Keeps repeating lotto draws until a jackpot is hit (This is purely for testing purposes and may take
     * a while to hit a jackpot, so please be patient)
     */
    @SuppressWarnings("unused")
    private void testJackpot(LottoDraw jackpotTest) {
        try {
            System.out.println("\nTesting jackpot odds... Please be patient as this may take a while...\n"
                    + "\nPress \"ctrl + c\" at any time to stop the execution early.\n");

            // gives the user a chance to read the cancel shortcut before executing the test run
            Thread.sleep(3000);

            // keeps repeating lotto draws until a jackpot is hit
            while (MAX_PRIZE_VALUE > totalPrizeAmount) {
                totalPrizeAmount = 0;
                jackpotTest.startDraw(jackpotTest);

            } // end while

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);

        } // end try

    } // end void

} // end class
