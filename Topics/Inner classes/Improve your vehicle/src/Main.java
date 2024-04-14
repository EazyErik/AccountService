class Vehicle {

    private String name;

    // create constructor
    public Vehicle(String name) {
        this.name = name;
    }

    class Engine {


        private int horsePower;

        public Engine(int horsePower) {
            this.horsePower = horsePower;
        }

        void start() {
            System.out.println("RRRrrrrrrr....");
        }

        public void printHorsePower() {
            System.out.println("Vehicle " + Vehicle.this.name + " has " + horsePower + " horsepower.");


        }
    }
}

// this code should work
class EnjoyVehicle {

    public static void main(String[] args) {

        Vehicle vehicle = new Vehicle("Dixi");
        Vehicle.Engine engine = vehicle.new Engine(20);
        engine.printHorsePower();
    }
}