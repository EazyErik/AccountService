class Pumpkin {

    private boolean forHalloween;

    public Pumpkin(boolean forHalloween) {
        this.forHalloween = forHalloween;
    }

    // create method addCandle()
    public void addCandle() {
        if (forHalloween) {

            this.new Candle().burning();
        } else {
            System.out.println("We don't need a candle.");
        }
    }

    class Candle {

        void burning() {
            System.out.println("The candle is burning! Boooooo!");
        }
    }
}