package com.google.appinventor.client.explorer.youngandroid;

/*import static com.google.appinventor.client.Ode.MESSAGES;

import com.google.appinventor.client.Ode;
import com.google.appinventor.client.OdeAsyncCallback;
import com.google.appinventor.client.explorer.project.Project;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;

import com.google.appinventor.shared.rpc.project.UserProject;
import com.google.appinventor.shared.rpc.project.youngandroid.NewYoungAndroidProjectParameters;
import com.google.appinventor.shared.rpc.project.youngandroid.YoungAndroidProjectNode;
*/

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.CustomButton;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.List;
import java.util.ArrayList;


public class NewUserGetStarted{
	
  public static class Tutorial {
	ArrayList<TutorialSlide> slides = new ArrayList<TutorialSlide>();
	int currentMessageIndex = 0;
		  
	public Tutorial() {
			  
	}
		  
	public void addSlide(TutorialSlide slide) {
	  slides.add(slide);
	  slide.setTutorial(this);
	}
		  
	public void nextSlide() {
	  if (currentMessageIndex < this.slides.size() - 1) {
		slides.get(currentMessageIndex).hide();
		currentMessageIndex += 1;
		slides.get(currentMessageIndex).show();
	  }
	}
		  
	public void lastSlide() {
	  if (currentMessageIndex > 0) {
		slides.get(currentMessageIndex).hide();
		currentMessageIndex -= 1;
		slides.get(currentMessageIndex).show();
	  }
	}
		
	public void exit() {
	  slides.get(currentMessageIndex).hide();
	}
		  
  }

  public static class TutorialSlide extends DialogBox {
	private ArrayList<Image> images = new ArrayList<Image>();
	private Image background;
	private Image continueButton;
	private Image backButton;
	private Image exitButton;
	private AbsolutePanel holder = new AbsolutePanel();
	Tutorial tutorial;
	
	public void setTutorial(Tutorial t) {
	  tutorial = t;
	}
	  
	public void setBackgroundImage(Image background) {
	  this.background = background;
	  this.holder.add(this.background);
	}
	  
	public void setContinueButton(Image button, int x, int y) {
	  this.continueButton = button;
	  holder.add(this.continueButton);
	  this.continueButton.addClickListener(new ClickListener() {
	    public void onClick(Widget sender) {
	      tutorial.nextSlide();
	    }
	  });
	  //TODO: do i need this?
	  holder.setWidgetPosition(this.continueButton, x, y);
	}
	  
	public void setBackButton(Image button, int x, int y) {
	  this.backButton = button;
	  holder.add(this.backButton);
	  this.backButton.addClickListener(new ClickListener() {
	    public void onClick(Widget sender) {
	      tutorial.lastSlide();
	    }
	  });
	  holder.setWidgetPosition(this.backButton, x, y);
	}  

	public void setExitButton(Image button, int x, int y) {
	  this.exitButton = button;
	  holder.add(this.exitButton);
	  this.exitButton.addClickListener(new ClickListener() {
		public void onClick(Widget sender) {
		  tutorial.exit();
		}
	  });
	  holder.setWidgetPosition(this.exitButton, x, y);
	}
	
	public void addImage(Image newImage, int x, int y) {
	  this.images.add(newImage);
	  this.holder.add(newImage);
	  holder.setWidgetPosition(newImage, x, y);
	}
	
	public void ready() {
	  this.setWidget(holder);
    }
  }

  // Pull out and start own class here, ideally.
  // TODO: is this still used?
  public static int currentMessageIndex;

  /**
   * Creates, visually centers, and optionally displays the dialog box
   * that informs the user how to start learning about using App Inventor
   * @param showDialog Convenience variable to show the created DialogBox.
   * @return The created and optionally displayed Dialog box.
   */
  public static Tutorial createStarterDialog(boolean showDialog) {
    // Create the UI elements of the DialogBox
    final Tutorial tutorial = new Tutorial();
    final TutorialSlide firstSlide= new TutorialSlide(); // DialogBox(autohide, modal)
    firstSlide.setStylePrimaryName("ode-DialogBox-getStarted");
    //dialogBox.setHeight("400px");
    //dialogBox.setWidth("400px");
    //dialogBox.setGlassEnabled(true);  // was true
    firstSlide.setAnimationEnabled(true);

    Image backgroundImage = new Image("images/getStarted/Screen2Popup.png");
    backgroundImage.setPixelSize(835, 470);
    firstSlide.setBackgroundImage(backgroundImage);
    
    Image exitButton = new Image("images/getStarted/Components/0RedCloseButton.png");
    exitButton.setPixelSize(30, 30);
    firstSlide.setExitButton(exitButton, 805, 0);
    
    Image continueButton = new Image("images/getStarted/Components/NextButton.png");
    continueButton.setPixelSize(190, 96);
    firstSlide.setContinueButton(continueButton, 625, 370);
    
    firstSlide.ready();
    tutorial.addSlide(firstSlide);
    
    addDesignTutorialSlides(tutorial);
    
    
    firstSlide.show();
    return tutorial;
  }
  
  public static void addDesignTutorialSlides(Tutorial tutorial) {
    tutorial.addSlide(beginDesignTutorial(true));
    tutorial.addSlide(continueDesignTutorial(true));
    tutorial.addSlide(beginDesignPopup(true));
    tutorial.addSlide(beginProgramTutorial(true));
  }
  
  public static TutorialSlide beginDesignTutorial(boolean showDialog) {
    // Create the UI elements of the DialogBox
    final TutorialSlide designSlide = new TutorialSlide(); // DialogBox(autohide, modal)
    designSlide.setStylePrimaryName("ode-DialogBox-getStarted");
    //dialogBox.setHeight("400px");
    //dialogBox.setWidth("400px");
    //dialogBox.setGlassEnabled(true);  // was true
    designSlide.setAnimationEnabled(true);
    
    int browserHeight = Window.getClientHeight();
    int browserWidth = Window.getClientWidth();
    
    Image backgroundImage = new Image("images/getStarted/Screen3Frame.png");
    backgroundImage.setPixelSize(browserWidth, 410);
    designSlide.setBackgroundImage(backgroundImage);
    
    Image exitButton = new Image("images/getStarted/Components/0RedCloseButton.png");
    exitButton.setPixelSize(40, 40);
    designSlide.setExitButton(exitButton, browserWidth - 40, 240);
    
    Image continueButton = new Image("images/getStarted/Components/NextButton.png");
    continueButton.setPixelSize(190, 96);
    designSlide.setContinueButton(continueButton, browserWidth - 230, 280);

    Image backButton = new Image("images/getStarted/Components/BackButton.png");
    backButton.setPixelSize(190, 96);
    designSlide.setBackButton(backButton, browserWidth - 450, 280);
    
    designSlide.ready();
    //TODO: ask about this; what does it do and should we have it in all slides?
    designSlide.setPopupPosition(0, browserHeight - 410);

    return designSlide;
  }

  public static TutorialSlide continueDesignTutorial(boolean showDialog) {
    // Create the UI elements of the DialogBox
    final TutorialSlide designSlide = new TutorialSlide(); // DialogBox(autohide, modal)
    designSlide.setStylePrimaryName("ode-DialogBox-getStarted");
    //dialogBox.setHeight("400px");
    //dialogBox.setWidth("400px");
    //dialogBox.setGlassEnabled(true);  // was true
    designSlide.setAnimationEnabled(true);
    
    int browserHeight=Window.getClientHeight();
    int browserWidth=Window.getClientWidth();
    
    Image backgroundImage = new Image("images/getStarted/Screen4Overlay.png");
    backgroundImage.setPixelSize(browserWidth, browserHeight);
    designSlide.setBackgroundImage(backgroundImage);
    
    Image exitButton = new Image("images/getStarted/Components/0RedCloseButton.png");
    exitButton.setPixelSize(40, 40);
    designSlide.setExitButton(exitButton, browserWidth - 40, 0);
    
    Image continueButton = new Image("images/getStarted/Components/NextButton.png");
    continueButton.setPixelSize(190, 96);
    designSlide.setContinueButton(continueButton, browserWidth - 230, browserHeight - 125);

    Image backButton = new Image("images/getStarted/Components/BackButton.png");
    backButton.setPixelSize(190, 96);
    designSlide.setBackButton(backButton, browserWidth - 450, browserHeight - 125);
    
    designSlide.ready();
    
    return designSlide;
  }


  //TODO: I actually should probably define a TutorialSlide subclass for popups.
  public static TutorialSlide beginDesignPopup(boolean showDialog) {
    // Create the UI elements of the DialogBox
    final TutorialSlide designPopup = new TutorialSlide(); // DialogBox(autohide, modal)
    designPopup.setStylePrimaryName("ode-DialogBox-getStarted");
    //dialogBox.setHeight("400px");
    //dialogBox.setWidth("400px");
    //dialogBox.setGlassEnabled(true);  // was true
    designPopup.setAnimationEnabled(true);
    
    int browserWidth=Window.getClientWidth();
    int browserHeight=Window.getClientHeight();
    
    Image backgroundImage = new Image("images/getStarted/Components/0BlankSideMenu.png");
    backgroundImage.setPixelSize(250, 650);
    designPopup.setBackgroundImage(backgroundImage);
    
    Image exitButton = new Image("images/getStarted/Components/0RedCloseButton.png");
    exitButton.setPixelSize(30, 30);
    designPopup.setExitButton(exitButton, 220, 0);

    Image designHeader = new Image("images/getStarted/Components/1DesignerSideMenuHeader.png");
    designHeader.setPixelSize(200, 55);
    designPopup.addImage(designHeader, 0, 0);

    Image designText = new Image("images/getStarted/Components/1DesignerSideMenuText.png");
    designText.setPixelSize(250, 540);
    designPopup.addImage(designText, 0, 65);
    
    Image continueButton = new Image("images/getStarted/Components/NextButton.png");
    continueButton.setPixelSize(80, 40);
    designPopup.setContinueButton(continueButton, 150, 590);
    
    designPopup.ready();

    designPopup.setPopupPosition(browserWidth - 250, 0);
    
    return designPopup;
  }


  public static TutorialSlide beginProgramTutorial(boolean showDialog) {
    // Create the UI elements of the DialogBox
    final TutorialSlide programSlide = new TutorialSlide(); // DialogBox(autohide, modal)
    programSlide.setStylePrimaryName("ode-DialogBox-getStarted");
    //dialogBox.setHeight("400px");
    //dialogBox.setWidth("400px");
    //dialogBox.setGlassEnabled(true);  // was true
    programSlide.setAnimationEnabled(true);
    
    int browserHeight=Window.getClientHeight();
    int browserWidth=Window.getClientWidth();
    
    Image backgroundImage = new Image("images/getStarted/Screen8Frame.png");
    backgroundImage.setPixelSize(browserWidth, 410);
    programSlide.setBackgroundImage(backgroundImage);
    
    Image exitButton = new Image("images/getStarted/Components/0RedCloseButton.png");
    exitButton.setPixelSize(40,40);
    programSlide.setExitButton(exitButton, browserWidth - 40, 240);
    
    Image continueButton = new Image("images/getStarted/Components/NextButton.png");
    continueButton.setPixelSize(190, 96);
    programSlide.setContinueButton(continueButton, browserWidth - 230, 280);

    Image backButton = new Image("images/getStarted/Components/BackButton.png");
    backButton.setPixelSize(190, 96);
    programSlide.setBackButton(backButton, browserWidth - 450, 280);
    
    programSlide.ready();
    //TODO
    programSlide.setPopupPosition(0, browserHeight - 410);

    return programSlide;
  }
  

}