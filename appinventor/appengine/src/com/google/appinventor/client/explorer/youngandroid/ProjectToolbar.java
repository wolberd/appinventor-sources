// -*- mode: java; c-basic-offset: 2; -*-
// Copyright 2009-2011 Google, All Rights reserved
// Copyright 2011-2012 MIT, All rights reserved
// Released under the MIT License https://raw.github.com/mit-cml/app-inventor/master/mitlicense.txt

package com.google.appinventor.client.explorer.youngandroid;

import static com.google.appinventor.client.Ode.MESSAGES;

import com.google.appinventor.client.ErrorReporter;
import com.google.appinventor.client.Ode;
import com.google.appinventor.client.OdeAsyncCallback;
import com.google.appinventor.client.boxes.ProjectListBox;
import com.google.appinventor.client.boxes.ViewerBox;
import com.google.appinventor.client.explorer.project.Project;
import com.google.appinventor.client.tracking.Tracking;
import com.google.appinventor.client.utils.Downloader;
import com.google.appinventor.client.widgets.Toolbar;
import com.google.appinventor.client.wizards.DownloadUserSourceWizard;
import com.google.appinventor.client.wizards.KeystoreUploadWizard;
import com.google.appinventor.client.wizards.ProjectUploadWizard;
import com.google.appinventor.client.wizards.youngandroid.NewYoungAndroidProjectWizard;
import com.google.appinventor.shared.rpc.ServerLayout;
import com.google.appinventor.shared.storage.StorageUtil;
import com.google.common.collect.Lists;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

import com.google.appinventor.shared.rpc.project.UserProject;
import com.google.appinventor.shared.rpc.project.youngandroid.NewYoungAndroidProjectParameters;
import com.google.appinventor.shared.rpc.project.youngandroid.YoungAndroidProjectNode;

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

/**
 * The project toolbar houses command buttons in the Young Android Project tab.
 *
 */
public class ProjectToolbar extends Toolbar {
  private static final String WIDGET_NAME_NEW = "New";
  private static final String WIDGET_NAME_DELETE = "Delete";
  private static final String WIDGET_NAME_MORE_ACTIONS = "MoreActions";

  private static final String WIDGET_NAME_GET_STARTED = "GetStarted";
 
  private static final String WIDGET_NAME_DOWNLOAD_ALL = "DownloadAll";
  private static final String WIDGET_NAME_DOWNLOAD_SOURCE = "DownloadSource";
  private static final String WIDGET_NAME_UPLOAD_SOURCE = "UploadSource";
  private static final String WIDGET_NAME_ADMIN = "Admin";
  private static final String WIDGET_NAME_DOWNLOAD_USER_SOURCE = "DownloadUserSource";
  private static final String WIDGET_NAME_SWITCH_TO_DEBUG = "SwitchToDebugPane";
  private static final String WIDGET_NAME_DOWNLOAD_KEYSTORE = "DownloadKeystore";
  private static final String WIDGET_NAME_UPLOAD_KEYSTORE = "UploadKeystore";
  private static final String WIDGET_NAME_DELETE_KEYSTORE = "DeleteKeystore";

  private static final String PROJECT_ARCHIVE_EXTENSION = ".aia";
  public static final String TEMPLATES_ROOT_DIRECTORY =  "templates/";

  /**
   * Initializes and assembles all commands into buttons in the toolbar.
   */
  public ProjectToolbar() {
    super();

    addButton(new ToolbarItem(WIDGET_NAME_NEW, MESSAGES.newButton(),
        new NewAction()));

    addButton(new ToolbarItem(WIDGET_NAME_DELETE, MESSAGES.deleteButton(),
        new DeleteAction()));
    List<ToolbarItem> otherItems = Lists.newArrayList();
    otherItems.add(new ToolbarItem(WIDGET_NAME_DOWNLOAD_SOURCE,
        MESSAGES.downloadSourceButton(), new DownloadSourceAction()));
    otherItems.add(new ToolbarItem(WIDGET_NAME_UPLOAD_SOURCE,
        MESSAGES.uploadSourceButton(), new UploadSourceAction()));
    otherItems.add(null);
    otherItems.add(new ToolbarItem(WIDGET_NAME_DOWNLOAD_KEYSTORE,
        MESSAGES.downloadKeystoreButton(), new DownloadKeystoreAction()));
    otherItems.add(new ToolbarItem(WIDGET_NAME_UPLOAD_KEYSTORE,
        MESSAGES.uploadKeystoreButton(), new UploadKeystoreAction()));
    otherItems.add(new ToolbarItem(WIDGET_NAME_DELETE_KEYSTORE,
        MESSAGES.deleteKeystoreButton(), new DeleteKeystoreAction()));

    otherItems.add(null);
    otherItems.add(new ToolbarItem(WIDGET_NAME_DOWNLOAD_ALL, MESSAGES.downloadAllButton(),
        new DownloadAllAction()));

    addDropDownButton(WIDGET_NAME_MORE_ACTIONS, MESSAGES.moreActionsButton(), otherItems);
    
    addButton(new ToolbarItem(WIDGET_NAME_GET_STARTED, MESSAGES.getStartedButton(),
        new GetStartedAction()));

    if (Ode.getInstance().getUser().getIsAdmin()) {
      List<ToolbarItem> adminItems = Lists.newArrayList();
      adminItems.add(new ToolbarItem(WIDGET_NAME_DOWNLOAD_USER_SOURCE,
          MESSAGES.downloadUserSourceButton(), new DownloadUserSourceAction()));
      adminItems.add(new ToolbarItem(WIDGET_NAME_SWITCH_TO_DEBUG,
          MESSAGES.switchToDebugButton(), new SwitchToDebugAction()));
      addDropDownButton(WIDGET_NAME_ADMIN, MESSAGES.adminButton(), adminItems);
    }
    updateKeystoreButtons();
    updateButtons();
  }

  private static class NewAction implements Command {
    @Override
    public void execute() {
      new NewYoungAndroidProjectWizard().center();
      // The wizard will switch to the design view when the new
      // project is created.
    }
  }

  public void getStarted()
  {
     GetStartedAction action = new GetStartedAction();
     action.execute();
  }

  private static class GetStartedAction implements Command {
    @Override
    public void execute() {
      
      final String projectName="GetStarted";
      // Callback for updating the project explorer after the project is created on the back-end
      final Ode ode = Ode.getInstance();
      final OdeAsyncCallback<UserProject> callback = new OdeAsyncCallback<UserProject>(
        // failure message
        MESSAGES.createProjectError()) {
        @Override
        public void onSuccess(UserProject projectInfo) {
          // Update project explorer -- i.e., display in project view
          if (projectInfo == null) {

            Window.alert("Unable to open get started project:" + projectName);
            ode.getProjectService().newProject(
              YoungAndroidProjectNode.YOUNG_ANDROID_PROJECT_TYPE,
              projectName,
              new NewYoungAndroidProjectParameters(projectName),
              this);
            return;
          }
          Project project = ode.getProjectManager().addProject(projectInfo);
          Ode.getInstance().openYoungAndroidProjectInDesigner(project);
          //if (onSuccessCommand != null) {
            // onSuccessCommand.execute(project);
          // }  
        }
      };
      String pathToZip = TEMPLATES_ROOT_DIRECTORY + projectName + "/" + projectName +
        PROJECT_ARCHIVE_EXTENSION;
      ode.getProjectService().newProjectFromTemplate(projectName, pathToZip, callback);
      createStarterDialog(true);
    
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
  public static DialogBox createEricaStarterDialog(boolean showDialog) {
    // Create the UI elements of the DialogBox
    final DialogBox dialogBox = new DialogBox(false, false); // DialogBox(autohide, modal)
    dialogBox.setStylePrimaryName("ode-DialogBox");
    dialogBox.setHeight("400px");
    dialogBox.setWidth("400px");
    dialogBox.setGlassEnabled(false);  // was true
    dialogBox.setAnimationEnabled(true);
    
    AbsolutePanel holder = new AbsolutePanel();
    
    Image backgroundImage = new Image("images/getStarted/Background2.gif");
    backgroundImage.setPixelSize(835, 470);
    holder.add(backgroundImage);
    
    Image exitButton = new Image("images/getStarted/RedExitButton1.gif");
    exitButton.addClickListener(new ClickListener() {
        public void onClick(Widget sender) {
            dialogBox.hide();
        }
    });
    
    holder.add(exitButton);
    holder.setWidgetPosition(exitButton, 805, 0);
    
    Image continueButton = new Image("images/getStarted/NextScreenButton2.gif");
    continueButton.setPixelSize(190, 96);
    continueButton.addClickListener(new ClickListener() {
        public void onClick(Widget sender) {
            dialogBox.hide();
        }
    });
    holder.add(continueButton);
    holder.setWidgetPosition(continueButton, 625, 370);
    
    dialogBox.setWidget(holder);
    dialogBox.show();
    return dialogBox;
  }
  
  
  public static DialogBox createStarterDialog(boolean showDialog) {
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
    
  

  private static class DownloadAllAction implements Command {
    @Override
    public void execute() {
      Tracking.trackEvent(Tracking.PROJECT_EVENT,
          Tracking.PROJECT_ACTION_DOWNLOAD_ALL_PROJECTS_SOURCE_YA);

      // Is there a way to disable the Download All button until this completes?
      if (Window.confirm(MESSAGES.downloadAllAlert())) {

        Downloader.getInstance().download(ServerLayout.DOWNLOAD_SERVLET_BASE +
            ServerLayout.DOWNLOAD_ALL_PROJECTS_SOURCE);
      }
    }
  }

  private static class UploadSourceAction implements Command {
    @Override
    public void execute() {
      new ProjectUploadWizard().center();
    }
  }

  private static class DeleteAction implements Command {
    @Override
    public void execute() {
      List<Project> selectedProjects =
        ProjectListBox.getProjectListBox().getProjectList().getSelectedProjects();
      if (selectedProjects.size() > 0) {
        // Show one confirmation window for selected projects.
        if (deleteConfirmation(selectedProjects)) {
          for (Project project : selectedProjects) {
            deleteProject(project);
          }
        }

      } else {
        // The user can select a project to resolve the
        // error.
        ErrorReporter.reportInfo(MESSAGES.noProjectSelectedForDelete());
      }
    }

    private boolean deleteConfirmation(List<Project> projects) {
      String message;
      if (projects.size() == 1) {
        message = MESSAGES.confirmDeleteSingleProject(projects.get(0).getProjectName());
      } else {
        StringBuilder sb = new StringBuilder();
        String separator = "";
        for (Project project : projects) {
          sb.append(separator).append(project.getProjectName());
          separator = ", ";
        }
        String projectNames = sb.toString();
        message = MESSAGES.confirmDeleteManyProjects(projectNames);
      }
      return Window.confirm(message);
    }

    private void deleteProject(Project project) {
      Tracking.trackEvent(Tracking.PROJECT_EVENT,
          Tracking.PROJECT_ACTION_DELETE_PROJECT_YA, project.getProjectName());

      final long projectId = project.getProjectId();

      Ode ode = Ode.getInstance();
      boolean isCurrentProject = (projectId == ode.getCurrentYoungAndroidProjectId());
      ode.getEditorManager().closeProjectEditor(projectId);
      if (isCurrentProject) {
        // If we're deleting the project that is currently open in the Designer we
        // need to clear the ViewerBox first.
        ViewerBox.getViewerBox().clear();
      }
      // Make sure that we delete projects even if they are not open.
      doDeleteProject(projectId);
    }

    private void doDeleteProject(final long projectId) {
      Ode.getInstance().getProjectService().deleteProject(projectId,
          new OdeAsyncCallback<Void>(
              // failure message
              MESSAGES.deleteProjectError()) {
        @Override
        public void onSuccess(Void result) {
          Ode.getInstance().getProjectManager().removeProject(projectId);
          // Show a welcome dialog in case there are no
          // projects saved.
          if (Ode.getInstance().getProjectManager().getProjects().size() == 0) {
            Ode.getInstance().createWelcomeDialog(true);
          }
        }
      });
    }

  }

  private static class DownloadSourceAction implements Command {
    @Override
    public void execute() {
      List<Project> selectedProjects =
        ProjectListBox.getProjectListBox().getProjectList().getSelectedProjects();
      if (selectedProjects.size() == 1) {
        downloadSource(selectedProjects.get(0));
      } else {
        // The user needs to select only one project.
        ErrorReporter.reportInfo(MESSAGES.wrongNumberProjectsSelected());
      }
    }

    private void downloadSource(Project project) {
      Tracking.trackEvent(Tracking.PROJECT_EVENT,
          Tracking.PROJECT_ACTION_DOWNLOAD_PROJECT_SOURCE_YA, project.getProjectName());

      Downloader.getInstance().download(ServerLayout.DOWNLOAD_SERVLET_BASE +
          ServerLayout.DOWNLOAD_PROJECT_SOURCE + "/" + project.getProjectId());
    }
  }

  private static class DownloadUserSourceAction implements Command {
    @Override
    public void execute() {
      new DownloadUserSourceWizard().center();
    }
  }

  private static class SwitchToDebugAction implements Command {
    @Override
    public void execute() {
      Ode.getInstance().switchToDebuggingView();
    }
  }

  private static class DownloadKeystoreAction implements Command {
    @Override
    public void execute() {
      Ode.getInstance().getUserInfoService().hasUserFile(StorageUtil.ANDROID_KEYSTORE_FILENAME,
          new OdeAsyncCallback<Boolean>(MESSAGES.downloadKeystoreError()) {
        @Override
        public void onSuccess(Boolean keystoreFileExists) {
          if (keystoreFileExists) {
            Tracking.trackEvent(Tracking.USER_EVENT, Tracking.USER_ACTION_DOWNLOAD_KEYSTORE);
            Downloader.getInstance().download(ServerLayout.DOWNLOAD_SERVLET_BASE +
                ServerLayout.DOWNLOAD_USERFILE + "/" + StorageUtil.ANDROID_KEYSTORE_FILENAME);
          } else {
            Window.alert(MESSAGES.noKeystoreToDownload());
          }
        }
      });
    }
  }

  private class UploadKeystoreAction implements Command {
    @Override
    public void execute() {
      Ode.getInstance().getUserInfoService().hasUserFile(StorageUtil.ANDROID_KEYSTORE_FILENAME,
          new OdeAsyncCallback<Boolean>(MESSAGES.uploadKeystoreError()) {
        @Override
        public void onSuccess(Boolean keystoreFileExists) {
          if (!keystoreFileExists || Window.confirm(MESSAGES.confirmOverwriteKeystore())) {
            KeystoreUploadWizard wizard = new KeystoreUploadWizard(new Command() {
              @Override
              public void execute() {
                Tracking.trackEvent(Tracking.USER_EVENT, Tracking.USER_ACTION_UPLOAD_KEYSTORE);
                updateKeystoreButtons();
              }
            });
            wizard.center();
          }
        }
      });
    }
  }

  private class DeleteKeystoreAction implements Command {
    @Override
    public void execute() {
      final String errorMessage = MESSAGES.deleteKeystoreError();
      Ode.getInstance().getUserInfoService().hasUserFile(StorageUtil.ANDROID_KEYSTORE_FILENAME,
          new OdeAsyncCallback<Boolean>(errorMessage) {
        @Override
        public void onSuccess(Boolean keystoreFileExists) {
          if (keystoreFileExists && Window.confirm(MESSAGES.confirmDeleteKeystore())) {
            Tracking.trackEvent(Tracking.USER_EVENT, Tracking.USER_ACTION_DELETE_KEYSTORE);
            Ode.getInstance().getUserInfoService().deleteUserFile(
                StorageUtil.ANDROID_KEYSTORE_FILENAME,
                new OdeAsyncCallback<Void>(errorMessage) {
                  @Override
                  public void onSuccess(Void result) {
                    updateKeystoreButtons();
                  }
                });
          }
        }
      });
    }
  }

  /**
   * Enables and/or disables buttons based on how many projects exist
   * (in the case of "Download All Projects") or are selected (in the case
   * of "Delete" and "Download Source").
   */
  public void updateButtons() {
    ProjectList projectList = ProjectListBox.getProjectListBox().getProjectList();
    int numProjects = projectList.getNumProjects();
    int numSelectedProjects = projectList.getNumSelectedProjects();

    //    setButtonEnabled(WIDGET_NAME_DOWNLOAD_ALL, numProjects > 0);

    setButtonEnabled(WIDGET_NAME_DELETE, numSelectedProjects > 0);

    setDropItemEnabled(WIDGET_NAME_MORE_ACTIONS, WIDGET_NAME_DOWNLOAD_SOURCE, 
        numSelectedProjects == 1);
  }

  /**
   * Enables or disables buttons based on whether the user has an android.keystore file.
   */
  public void updateKeystoreButtons() {
    Ode.getInstance().getUserInfoService().hasUserFile(StorageUtil.ANDROID_KEYSTORE_FILENAME,
        new AsyncCallback<Boolean>() {
      @Override
      public void onSuccess(Boolean keystoreFileExists) {
        setDropItemEnabled(WIDGET_NAME_MORE_ACTIONS, WIDGET_NAME_DOWNLOAD_KEYSTORE, 
            keystoreFileExists);
        setDropItemEnabled(WIDGET_NAME_MORE_ACTIONS, WIDGET_NAME_DELETE_KEYSTORE, 
            keystoreFileExists);
      }

      @Override
      public void onFailure(Throwable caught) {
        // Enable the buttons. If they are clicked, we'll check again if the keystore exists.
        setDropItemEnabled(WIDGET_NAME_MORE_ACTIONS, WIDGET_NAME_DOWNLOAD_KEYSTORE, true);
        setDropItemEnabled(WIDGET_NAME_MORE_ACTIONS, WIDGET_NAME_DELETE_KEYSTORE, true);
      }
    });
  }
}
