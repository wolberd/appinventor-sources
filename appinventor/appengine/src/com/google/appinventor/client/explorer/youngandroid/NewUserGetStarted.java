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
	
  public class Tutorial {
	ArrayList<TutorialSlide> slides = new ArrayList<TutorialSlide>();
	int currentMessageIndex = 0;
		  
	public Tutorial() {
			  
	}
		  
	public void addSlide(TutorialSlide slide) {
	  slides.add(slide);
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
		currentMessageIndex += 1;
		slides.get(currentMessageIndex).show();
	  }
	}
		
	public void exit() {
	  slides.get(currentMessageIndex).hide();
	}
		  
  }

  public class ImageAndLocation {
	private Image image;
	private int xLoc;
	private int yLoc;
  }

  public class TutorialSlide extends DialogBox {
	private ArrayList<Image> images = new ArrayList<Image>();
	private Image background;
	private Image nextButton;
	private Image backButton;
	private Image exitButton;
	private SimplePanel holder = new SimplePanel();
	Tutorial tutorial;
	  
	public TutorialSlide(Tutorial t) {
	  t.addSlide(this);
	  tutorial = t;
	}
	  
	public void setBackgroundImage(Image background, int x, int y) {
	  this.background = background;
	  this.background.setPixelSize(x, y);
	  this.holder.add(this.background);
	}
	  
	public void setNextButton(Image button, int x, int y) {
	  this.nextButton = button;
	  this.nextButton.addClickListener(new ClickListener() {
	    public void onClick(Widget sender) {
	      tutorial.nextSlide();
	    }
	  });
	}
	  
	public void setBackButton(Image button, int x, int y) {
	  this.backButton = button;
	  this.backButton.addClickListener(new ClickListener() {
	    public void onClick(Widget sender) {
	      tutorial.lastSlide();
	    }
	  });
	}  

	public void setExitButton(Image button, int x, int y) {
	  this.exitButton = button;
	  this.exitButton.addClickListener(new ClickListener() {
		public void onClick(Widget sender) {
		  tutorial.exit();
		}
	  });
	}
  }

  // Pull out and start own class here, ideally.
  public static int currentMessageIndex;

  /**
   * Creates, visually centers, and optionally displays the dialog box
   * that informs the user how to start learning about using App Inventor
   * @param showDialog Convenience variable to show the created DialogBox.
   * @return The created and optionally displayed Dialog box.
   */
  public static DialogBox createStarterDialog(boolean showDialog) {
    // Create the UI elements of the DialogBox
    final DialogBox dialogBox = new DialogBox(false, false); // DialogBox(autohide, modal)
    dialogBox.setStylePrimaryName("ode-DialogBox");
    //dialogBox.setHeight("400px");
    //dialogBox.setWidth("400px");
    //dialogBox.setGlassEnabled(true);  // was true
    dialogBox.setAnimationEnabled(true);
    
    AbsolutePanel holder = new AbsolutePanel();
    
    Image backgroundImage = new Image("images/getStarted/Screen2Popup.png");
    backgroundImage.setPixelSize(835, 470);
    holder.add(backgroundImage);
    
    Image exitButton = new Image("images/getStarted/Components/0RedCloseButton.png");
    exitButton.addClickListener(new ClickListener() {
        public void onClick(Widget sender) {
            dialogBox.hide();
        }
    });
    exitButton.setPixelSize(30, 30);
    holder.add(exitButton);
    holder.setWidgetPosition(exitButton, 805, 0);
    
    Image continueButton = new Image("images/getStarted/Components/0BlankRightArrow.png");
    continueButton.setPixelSize(190, 96);
    continueButton.addClickListener(new ClickListener() {
        public void onClick(Widget sender) {
          dialogBox.hide();
          beginDesignTutorial(true);
        }
    });
    holder.add(continueButton);
    holder.setWidgetPosition(continueButton, 625, 370);
    
    dialogBox.setWidget(holder);
    dialogBox.show();
    return dialogBox;
  }

  public static DialogBox createNikiStarterDialog(boolean showDialog) {
    // Create the UI elements of the DialogBox
    final DialogBox dialogBox = new DialogBox(false, false); // DialogBox(autohide, modal)
    dialogBox.setStylePrimaryName("ode-DialogBox");
    dialogBox.setHeight("400px");
    dialogBox.setWidth("400px");
    dialogBox.setGlassEnabled(false);  // was true
    dialogBox.setAnimationEnabled(true);
    dialogBox.center();
    final VerticalPanel DialogBoxContents = new VerticalPanel();
    currentMessageIndex = 0;
    final ArrayList<HTML> messages = new ArrayList<HTML>();
    messages.add(new HTML("<h2>This is a sample app to get you started. You can try it on your phone or tablet by downloading the AI Companion app to your device</h2>"));
    messages.add(new HTML("<h2>Either scan this QR code, or search for MIT AI2 Companion on your device.</h2>"));
    messages.add(new HTML("<h2>Show the app you're building on your device. Open the companion app on your phone, then on your computer click Connect | Companion and scan the QR code.</h2>"));
    messages.add(new HTML("<h2>Change the Screen's Title. Can you see the change on your device?</h2>"));
    messages.add(new HTML("<h2>Change the Screen's Background. Can you see the change on your device?</h2>"));
    messages.add(new HTML("<h2>Now it's time to explore and modify the app's behavior. Click on the Blocks button.</h2>"));
    messages.get(0).setStyleName("DialogBox-message");
    DialogBoxContents.add(messages.get(0));
    SimplePanel holder = new SimplePanel();
    Button ok = new Button("Continue");
    Button back = new Button("Back");
    // you can put images in appinventor-sources/appinventor/appengine/war/images
    //   and then...
    final Image downloadImg = new Image("/images/downloadCompanion.png");
    
    ok.addClickListener(new ClickListener() {
        public void onClick(Widget sender) {
          /*
          dialogBox.hide();
          getProjectService().getProjects(new AsyncCallback<long[]>() {
              @Override
              public void onSuccess(long [] projectIds) {
                if (projectIds.length == 0) {
                  createNoProjectsDialog(true);
                }
              }
          
              @Override
              public void onFailure(Throwable projectIds) {
                // OdeLog.elog("Could not get project list");
              }
            });
        */
        // below is a simple and sort of dumb thing to do when the user clicks continue
        //   we need to set it up so it goes to next step, maybe a different dialog, or
        //   maybe we have a data structure of steps and we use its data to fill up
        //   the fixed fields in this dialog box as the user steps through
          if (currentMessageIndex < messages.size() - 1) {
            DialogBoxContents.remove(messages.get(currentMessageIndex));
            currentMessageIndex += 1;
            messages.get(currentMessageIndex).setStyleName("DialogBox-message");
            DialogBoxContents.remove(downloadImg);
            DialogBoxContents.insert(messages.get(currentMessageIndex), 0);
            dialogBox.setWidget(DialogBoxContents);
            dialogBox.show();
          }
        }
      });
    holder.add(ok);
    //DialogBoxContents.add(message.get(currentMessageIndex));
    DialogBoxContents.add(downloadImg);
    DialogBoxContents.add(holder);
    dialogBox.setWidget(DialogBoxContents);
    dialogBox.show();
    return dialogBox;
  }

  
  public static DialogBox beginDesignTutorial(boolean beginDesign) {
    // Create the UI elements of the DialogBox
    final DialogBox dialogBox = new DialogBox(false, false); // DialogBox(autohide, modal)
    dialogBox.setStylePrimaryName("ode-DialogBox");
    //dialogBox.setHeight("400px");
    //dialogBox.setWidth("400px");
    //dialogBox.setGlassEnabled(true);  // was true
    dialogBox.setAnimationEnabled(true);
    
    AbsolutePanel holder = new AbsolutePanel();
    int browserHeight=Window.getClientHeight();
    int browserWidth=Window.getClientWidth();
    
    Image backgroundImage = new Image("images/getStarted/Screen3Frame.png");
    backgroundImage.setPixelSize(browserWidth, 410);
    holder.add(backgroundImage);
    
    Image exitButton = new Image("images/getStarted/Components/0RedCloseButton.png");
    exitButton.addClickListener(new ClickListener() {
        public void onClick(Widget sender) {
            dialogBox.hide();
        }
    });
    
    exitButton.setPixelSize(40,40);
    holder.add(exitButton);
    holder.setWidgetPosition(exitButton, browserWidth - 40, 240);
    
    Image continueButton = new Image("images/getStarted/Components/0BlankRightArrow.png");
    continueButton.setPixelSize(190, 96);
    continueButton.addClickListener(new ClickListener() {
        public void onClick(Widget sender) {
          dialogBox.hide();
          continueDesignTutorial(true);
        }
    });
    holder.add(continueButton);
    holder.setWidgetPosition(continueButton, browserWidth - 230, 280);

    Image backButton = new Image("images/getStarted/Components/0BlankLeftArrow.png");
    backButton.setPixelSize(190, 96);
    backButton.addClickListener(new ClickListener() {
        public void onClick(Widget sender) {
          dialogBox.hide();
          createStarterDialog(true);
        }
    });
    holder.add(backButton);
    holder.setWidgetPosition(backButton, browserWidth - 450, 280);
    
    dialogBox.setWidget(holder);

    dialogBox.setPopupPosition(0, browserHeight - 410);

    dialogBox.show();
    return dialogBox;
  }

  public static DialogBox continueDesignTutorial(boolean continueDesign) {
    // Create the UI elements of the DialogBox
    final DialogBox dialogBox = new DialogBox(false, false); // DialogBox(autohide, modal)
    dialogBox.setStylePrimaryName("ode-DialogBox");
    //dialogBox.setHeight("400px");
    //dialogBox.setWidth("400px");
    //dialogBox.setGlassEnabled(true);  // was true
    dialogBox.setAnimationEnabled(true);
    
    AbsolutePanel holder = new AbsolutePanel();
    int browserHeight=Window.getClientHeight();
    int browserWidth=Window.getClientWidth();
    
    Image backgroundImage = new Image("images/getStarted/Screen4Overlay.png");
    backgroundImage.setPixelSize(browserWidth, browserHeight);
    holder.add(backgroundImage);
    
    Image exitButton = new Image("images/getStarted/Components/0RedCloseButton.png");
    exitButton.addClickListener(new ClickListener() {
        public void onClick(Widget sender) {
            dialogBox.hide();
        }
    });
    exitButton.setPixelSize(40, 40);
    holder.add(exitButton);
    holder.setWidgetPosition(exitButton, browserWidth - 40, 0);
    
    Image continueButton = new Image("images/getStarted/Components/0BlankRightArrow.png");
    continueButton.setPixelSize(190, 96);
    continueButton.addClickListener(new ClickListener() {
        public void onClick(Widget sender) {
          dialogBox.hide();
          beginDesignPopup(true);
        }
    });
    holder.add(continueButton);
    holder.setWidgetPosition(continueButton, browserWidth - 230, browserHeight - 125);

    Image backButton = new Image("images/getStarted/Components/0BlankLeftArrow.png");
    backButton.setPixelSize(190, 96);
    backButton.addClickListener(new ClickListener() {
        public void onClick(Widget sender) {
          dialogBox.hide();
          beginDesignTutorial(true);
        }
    });
    holder.add(backButton);
    holder.setWidgetPosition(backButton, browserWidth - 450, browserHeight - 125);
    
    dialogBox.setWidget(holder);
    dialogBox.show();
    return dialogBox;
  }


  public static DialogBox beginDesignPopup(boolean beginPopup) {
    // Create the UI elements of the DialogBox
    final DialogBox dialogBox = new DialogBox(false, false); // DialogBox(autohide, modal)
    dialogBox.setStylePrimaryName("ode-DialogBox");
    //dialogBox.setHeight("400px");
    //dialogBox.setWidth("400px");
    //dialogBox.setGlassEnabled(true);  // was true
    dialogBox.setAnimationEnabled(true);
    
    AbsolutePanel holder = new AbsolutePanel();
    int browserWidth=Window.getClientWidth();
    int browserHeight=Window.getClientHeight();
    
    Image backgroundImage = new Image("images/getStarted/Components/0BlankSideMenu.png");
    backgroundImage.setPixelSize(250, 650);
    holder.add(backgroundImage);
    
    Image exitButton = new Image("images/getStarted/Components/0RedCloseButton.png");
    exitButton.addClickListener(new ClickListener() {
        public void onClick(Widget sender) {
            dialogBox.hide();
        }
    });
    exitButton.setPixelSize(30, 30);
    holder.add(exitButton);
    holder.setWidgetPosition(exitButton, 220, 0);
    
    Image continueButton = new Image("images/getStarted/Components/0BlankRightArrow.png");
    continueButton.setPixelSize(80, 40);
    continueButton.addClickListener(new ClickListener() {
        public void onClick(Widget sender) {
          dialogBox.hide();
          beginProgramTutorial(true);
        }
    });
    holder.add(continueButton);
    holder.setWidgetPosition(continueButton, 150, 590);
    
    dialogBox.setWidget(holder);
    dialogBox.show();
    return dialogBox;
  }


  public static DialogBox beginProgramTutorial(boolean beginProgram) {
    // Create the UI elements of the DialogBox
    final DialogBox dialogBox = new DialogBox(false, false); // DialogBox(autohide, modal)
    dialogBox.setStylePrimaryName("ode-DialogBox");
    //dialogBox.setHeight("400px");
    //dialogBox.setWidth("400px");
    //dialogBox.setGlassEnabled(true);  // was true
    dialogBox.setAnimationEnabled(true);
    
    AbsolutePanel holder = new AbsolutePanel();
    int browserHeight=Window.getClientHeight();
    int browserWidth=Window.getClientWidth();
    
    Image backgroundImage = new Image("images/getStarted/Screen8Frame.png");
    backgroundImage.setPixelSize(browserWidth, 410);
    holder.add(backgroundImage);
    
    Image exitButton = new Image("images/getStarted/Components/0RedCloseButton.png");
    exitButton.addClickListener(new ClickListener() {
        public void onClick(Widget sender) {
            dialogBox.hide();
        }
    });
    
    exitButton.setPixelSize(40,40);
    holder.add(exitButton);
    holder.setWidgetPosition(exitButton, browserWidth - 40, 240);
    
    Image continueButton = new Image("images/getStarted/Components/0BlankRightArrow.png");
    continueButton.setPixelSize(190, 96);
    continueButton.addClickListener(new ClickListener() {
        public void onClick(Widget sender) {
          dialogBox.hide();
          //continueProgramTutorial(true);
        }
    });
    holder.add(continueButton);
    holder.setWidgetPosition(continueButton, browserWidth - 230, 280);
    
    dialogBox.setWidget(holder);

    dialogBox.setPopupPosition(0, browserHeight - 410);

    dialogBox.show();
    return dialogBox;
  }
  

}