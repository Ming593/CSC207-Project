package SwipePage;
import entity.petProfile.PetProfile;

public class SwipePageController {
    private ProfileSwipingInteractor interactor;
    private PetProfilePresenter presenter;
    private NewSwipePage view;

    public SwipePageController(ProfileSwipingInteractor interactor, PetProfilePresenter presenter, NewSwipePage view) {
        this.interactor = interactor;
        this.presenter = presenter;
        this.view = view;
    }

    public void loadNextProfile() {
        PetProfile profile = interactor.getNextProfile();
        SwipePageViewModel viewModel = presenter.createViewModel(profile);
        view.updateProfile(viewModel);
    }

    public void onLike() {
        // Logic for liking a profile
        System.out.println("clicked like");
        loadNextProfile();
    }

    public void onDislike() {
        // Logic for disliking a profile
        System.out.println("clicked dislike");
        loadNextProfile();
    }
}