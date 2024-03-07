import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class ChallengeControl {

    private Controller controller;
    private final int INITIAL_NUMBER = 10000;
    private int retAndRecPeople = INITIAL_NUMBER;
    private int grocAndPhaPeople = INITIAL_NUMBER;
    private int parksPeople = INITIAL_NUMBER;
    private int transitPeople = INITIAL_NUMBER;
    private int workplacePeople = INITIAL_NUMBER;
    private int residentialPeople = INITIAL_NUMBER;
    private ArrayList<Integer> people;

    public ChallengeControl(Controller controller){
        people = new ArrayList<>(Arrays.asList(retAndRecPeople, grocAndPhaPeople, parksPeople, transitPeople, workplacePeople, residentialPeople));
        this.controller = controller;
    }

}
