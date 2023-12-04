package myPets;


import dataAccessObject.PetProfileDataAccessInterface;
import dataAccessObject.PetProfileDataAccessObject;
import myPets.createNewDog.CreateController;
import myPets.createNewDog.CreateViewModel;
import myPets.createRedirect.CreateRedirectController;
import myPets.createRedirect.CreateRedirectViewModel;
import myPets.deleteMyPet.DeleteMyPetController;
import myPets.deleteMyPet.DeleteMyPetViewModel;
import myPets.innerViews.CreatePetView;
import myPets.innerViews.MyPetsDisplayView;
import myPets.innerViews.MyRedirectView;
import myPets.myPetDisplayRedirect.MyPetRedirectController;
import myPets.myPetDisplayRedirect.MyPetRedirectViewModel;
import myPets.myPetsRedirect.MyRedirectController;
import myPets.updateMyPet.UpdateMyPetsViewModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;


public class MyPetsView extends JPanel implements ActionListener, PropertyChangeListener {
    private final MyPetsViewModel vm;
    public final String viewName = "MyPetsView";

    private final JPanel petDisplaySection; //only changing views that are directly related to this JPanel
    private final CardLayout yesNOPetLayout;
    private final InnerViewModelManager innerViewModelManager;
    PetProfileDataAccessObject daop = new PetProfileDataAccessObject();

    private final CreateRedirectViewModel createRedirectViewModel;
    private final CreateViewModel createVM;
    private final CreateController createController;
    private final MyRedirectController myRedirectController;
    private final MyPetRedirectViewModel myPetRedirectViewModel;
    private final MyPetRedirectController myPetRedirectController;
    private JDialog dialog;

    public MyPetsView(InnerViewModelManager innerViewModelManager, MyPetsViewModel myPetsViewModel,
                      DeleteMyPetController deletePetController,
                      MyRedirectController myRedirectController, PetProfileDataAccessInterface petProfileDataAccessObject,
                      CreateRedirectViewModel createRedirectViewModel, CreateController createController, CreateRedirectController createRedirectController,
                      MyPetRedirectViewModel myPetRedirectViewModel, MyPetRedirectController myPetRedirectController
                          ) {


        this.myPetRedirectController = myPetRedirectController;
        this.myPetRedirectViewModel = myPetRedirectViewModel;
        myPetRedirectViewModel.addPropertyChangeListener(this);
        this.myRedirectController = myRedirectController;
        this.innerViewModelManager = innerViewModelManager;
        vm = myPetsViewModel;
        createVM = vm.getCreateViewModel();
        UpdateMyPetsViewModel updateVM = vm.getUpdateMyPetsViewModel();
        DeleteMyPetViewModel deleteMPVM = vm.getDeleteMyPetViewModel();
 //       NoMyPetsDisplayViewModel noMyPetsDisplayViewModel = vm.getNoPetsDisplayViewModel();
        this.createRedirectViewModel = createRedirectViewModel;
        createRedirectViewModel.addPropertyChangeListener(this);
        this.createController = createController;


        yesNOPetLayout = new CardLayout();
        petDisplaySection = new JPanel(yesNOPetLayout);

        //AddPetViewModel addVM = vm.getAddViewModel();


        //all the subview components
        MyPetsDisplayView petDisplayView = new MyPetsDisplayView(
                createVM,
                updateVM,
                deleteMPVM,
                deletePetController,
                myPetRedirectViewModel, createRedirectViewModel, createRedirectController);

        setLayout(new BorderLayout());
        setOpaque(true);
        JScrollPane scrollPane = new JScrollPane(petDisplayView);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        //CreatePetView createPetView = new CreatePetView(vm.getCreateViewModel());
        MyRedirectView redirectView = new MyRedirectView(vm.getRedirectViewModel(), myRedirectController);
//        NoPetsDisplayView noPetDisplayView = new NoPetsDisplayView(innerViewModelManager, noMyPetsDisplayViewModel, daop, myRedirectController, createRedirectController);
        //DisplayUserView displayUserView = new DisplayUserView(vm.getDisplayUserModel());


        //Display
        petDisplaySection.add(scrollPane, vm.getMyPetsDisplayViewModel().getViewName());
        //MyPetsDisplayView
        petDisplaySection.add(redirectView, vm.getRedirectViewModel().getViewName());
        //NoPetsDisplayView
//        petDisplaySection.add(noPetDisplayView, vm.getNoPetsDisplayViewModel().getViewName());



        //new ViewManager(petDisplaySection, yesNOFavPetLayout, viewModelManager);

        //Top section displays user, app info
        //add(displayUserView, BorderLayout.NORTH);
        //Middle section displays pet info

        //customize cardboard/scrollPane

//        petDisplaySection.add(scrollPane, createVM.getViewName());
//        petDisplaySection.add(noPetDisplayView,noMyPetsDisplayViewModel.getViewName());
        yesNOPetLayout.show(petDisplaySection, vm.getMyPetsDisplayViewModel().getViewName());//default view
        add(petDisplaySection, BorderLayout.CENTER);
        //TODO Debugging
        JPanel sidePanel1 = new JPanel();
        sidePanel1.setBackground(Color.darkGray);
        sidePanel1.setPreferredSize(new Dimension(100,100));
        JPanel sidePanel2 = new JPanel();
        sidePanel2.setBackground(Color.darkGray);
        sidePanel2.setPreferredSize(new Dimension(100,100));
        add(sidePanel1, BorderLayout.WEST);
        add(sidePanel2, BorderLayout.EAST);
        //Bottom section redirects
        add(redirectView, BorderLayout.SOUTH);


    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        System.out.println("propertyChange");
//        if (evt.getSource() == vm.getNoPetsDisplayViewModel()){
//            yesNOPetLayout.show(petDisplaySection,vm.getNoPetsDisplayViewModel().getViewName());
//            petDisplaySection.revalidate();
//            petDisplaySection.repaint();
//        }
        if(evt.getSource() == createRedirectViewModel){
            System.out.println("createRedirect");
            CreatePetView createView = new CreatePetView(innerViewModelManager, createVM, createController, myPetRedirectController);
            dialog = new JDialog((JFrame)null);
            dialog.setContentPane(createView);
            dialog.setSize(1000, 1000);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        }
        else if(evt.getSource() == myPetRedirectViewModel){
            System.out.println("myPetRedirectViewModel");
            yesNOPetLayout.show(petDisplaySection, vm.getMyPetsDisplayViewModel().getViewName());
            dialog.dispose();
            System.out.println("created");
            petDisplaySection.revalidate();
            petDisplaySection.repaint();
        }

    }
}
