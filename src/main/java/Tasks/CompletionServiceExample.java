package Tasks;

import java.util.Date;
import java.util.concurrent.*;

class FoodPlate {

    private boolean isPizzaReady;
    private boolean isBurgerReady;
    private boolean isOtherJunkReady;
    private String foodPlateCreatedBy;

    public String getFoodPlateCreatedBy() {
        return foodPlateCreatedBy;
    }
    public void setFoodPlateCreatedBy(String foodPlateCreatedBy) {
        this.foodPlateCreatedBy = foodPlateCreatedBy;
    }
    public boolean isPizzaReady() {
        return isPizzaReady;
    }
    public void setPizzaReady(boolean isPizzaReady) {
        this.isPizzaReady = isPizzaReady;
    }
    public boolean isBurgerReady() {
        return isBurgerReady;
    }
    public void setBurgerReady(boolean isBurgerReady) {
        this.isBurgerReady = isBurgerReady;
    }
    public boolean isOtherJunkReady() {
        return isOtherJunkReady;
    }
    public void setOtherJunkReady(boolean isOtherJunkReady) {
        this.isOtherJunkReady = isOtherJunkReady;
    }

}
class CanteenStaffProducer implements Callable {

    private String staffName;

    public CanteenStaffProducer(String prodName) {
            this.staffName = prodName;
    }

    @Override
    public FoodPlate call() throws Exception {
            System.out.println("Current Canteen Staff at work: "+
            this.staffName + " at "+ new Date());
            Thread.sleep(2000L);

            FoodPlate foodPlate = new FoodPlate();
            foodPlate.setBurgerReady(true);
            foodPlate.setPizzaReady(true);
            foodPlate.setOtherJunkReady(true);
            foodPlate.setFoodPlateCreatedBy(this.staffName);

            return foodPlate;
    }
}


class StudentConsumer implements Runnable {

    private String studName;
    private CompletionService<FoodPlate> service;

    public String getStudName() {
        return studName;
    }

    public void setStudName(String studName) {
        this.studName = studName;
    }

    public CompletionService getService() {
        return service;
    }

    public void setService(CompletionService<FoodPlate> service) {
        this.service = service;
    }

    public StudentConsumer(String studName, CompletionService<FoodPlate> service) {
        this.studName = studName;
        this.service = service;
    }

    @Override
    public void run() {
        System.out.println("Student waiting for foodplate: " +
                this.studName + " at " + new Date());
        try {
            Future<FoodPlate> fp = service.take();
            System.out.println("student got food plate created by: " +
                    fp.get().getFoodPlateCreatedBy());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println("Exiting run()");
    }

}

class CompletionServiceProvider {

    private static final Executor exec = Executors.newCachedThreadPool();
    private static final CompletionService completionService
            = new ExecutorCompletionService(exec);
    public static Executor getExec() {
        return exec;
    }
    public static CompletionService getCompletionservice() {
        return completionService;
    }

}

public class CompletionServiceExample {

    public static void main(String[] args) {
        CanteenStaffProducer prod1 = new CanteenStaffProducer("staff1");
        CanteenStaffProducer prod2 = new CanteenStaffProducer("staff2");

        CompletionService compService = CompletionServiceProvider.getCompletionservice();

        compService.submit(prod1);
        compService.submit(prod2);



        new Thread(new StudentConsumer("student1",compService)).start();
        new Thread(new StudentConsumer("student2",compService)).start();
    }

}
