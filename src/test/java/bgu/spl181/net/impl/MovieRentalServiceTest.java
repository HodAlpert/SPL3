package bgu.spl181.net.impl;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class MovieRentalServiceTest {
    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addClass(MovieRentalService.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }
    MovieRentalService service;
    String defaultuserFile;
    String getDefaultMovieFile;

    @BeforeClass
    public void beforeClass(){
        service = new MovieRentalService();
    }

    /**
     * 1) positive test- add user that's not exist- should be created
     * 2) negative test- add user that's already registered- should not be created
     * 3) ThreadSafeTest- registering same user with 10 threads- should only be added once- the rest should return false
     */
    @Test
    public void registerUser() {
    }

    /**
     * 1) positiveTest- check match userName and password
     * 2) negativeTest- check unMatch userName and password
     * 3) threadSafeTest- send request with five threads, check rightness
     */
    @Test
    public void loginValidation() {
    }

    /**
     * 1) positive Test- login a user and then check for true
     * 2) negatice Test- check unloggedIn user
     * 3) threadSafeTest- send request with 10 threads and check result
     */
    @Test
    public void isLoggedIn() {
    }

    /**
     * 1) positive Test- check value is correct
     * 2) threadSafeTest- send request with 10 threads and check result
     */
    @Test
    public void userBalanceInfo() {
    }

    /**
     * 1) positive Test- check value is added
     * 2) threadSafe Test and Fairness Test- send requests with 10 threads check fairness
     */
    @Test
    public void userAddBalance() {
    }

    /**
     * 1) positiveTest- login user and then signout him- should return true
     * 2) negative Test- signout user before logging him out- should return false
     */
    @Test
    public void signOut() {
    }

    /**
     * 1) positive test- with a movieName
     * 2) positive Test- without movieName
     * 3) negative Test- with a movie that's not exist
     */

    @Test
    public void movieInfo() {
    }

    /**
     * TODO think more about this
     */
    @Test
    public void rentMovie() {
    }

    @Test
    public void returnMovie() {
    }

    @Test
    public void addMovie() {
    }

    @Test
    public void removeMovie() {
    }

    @Test
    public void changeMoviePrice() {
    }
}
