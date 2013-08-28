package com.duggan.workflow.client.gin;

import com.duggan.workflow.client.place.ClientPlaceManager;
import com.duggan.workflow.client.place.DefaultPlace;
import com.duggan.workflow.client.place.NameTokens;
import com.duggan.workflow.client.ui.MainPagePresenter;
import com.duggan.workflow.client.ui.MainPageView;
import com.duggan.workflow.client.ui.activityfeed.ActivitiesPresenter;
import com.duggan.workflow.client.ui.activityfeed.ActivitiesView;
import com.duggan.workflow.client.ui.document.GenericDocumentPresenter;
import com.duggan.workflow.client.ui.document.GenericDocumentView;
import com.duggan.workflow.client.ui.error.ErrorPagePresenter;
import com.duggan.workflow.client.ui.error.ErrorPageView;
import com.duggan.workflow.client.ui.error.ErrorPresenter;
import com.duggan.workflow.client.ui.error.ErrorView;
import com.duggan.workflow.client.ui.error.NotfoundPresenter;
import com.duggan.workflow.client.ui.error.NotfoundView;
import com.duggan.workflow.client.ui.header.HeaderPresenter;
import com.duggan.workflow.client.ui.header.HeaderView;
import com.duggan.workflow.client.ui.home.HomePresenter;
import com.duggan.workflow.client.ui.home.HomeView;
import com.duggan.workflow.client.ui.login.LoginPresenter;
import com.duggan.workflow.client.ui.login.LoginView;
import com.duggan.workflow.client.ui.notifications.NotificationsPresenter;
import com.duggan.workflow.client.ui.notifications.NotificationsView;
import com.duggan.workflow.client.ui.notifications.note.NotePresenter;
import com.duggan.workflow.client.ui.notifications.note.NoteView;
import com.duggan.workflow.client.ui.save.CreateDocPresenter;
import com.duggan.workflow.client.ui.save.CreateDocView;
import com.duggan.workflow.client.ui.task.perfomancereview.PersonnelReviewPresenter;
import com.duggan.workflow.client.ui.task.perfomancereview.PersonnelReviewView;
import com.duggan.workflow.client.ui.task.personalreview.PersonalReviewPresenter;
import com.duggan.workflow.client.ui.task.personalreview.PersonalReviewView;
import com.duggan.workflow.client.ui.tasklist.tabs.TabsPresenter;
import com.duggan.workflow.client.ui.tasklist.tabs.TabsView;
import com.duggan.workflow.client.ui.tasklistitem.DateGroupPresenter;
import com.duggan.workflow.client.ui.tasklistitem.DateGroupView;
import com.duggan.workflow.client.ui.tasklistitem.TaskItemPresenter;
import com.duggan.workflow.client.ui.tasklistitem.TaskItemView;
import com.duggan.workflow.client.ui.toolbar.ToolbarPresenter;
import com.duggan.workflow.client.ui.toolbar.ToolbarView;
import com.duggan.workflow.client.ui.upload.attachment.AttachmentPresenter;
import com.duggan.workflow.client.ui.upload.attachment.AttachmentView;
import com.duggan.workflow.client.ui.user.UserSelectionPresenter;
import com.duggan.workflow.client.ui.user.UserSelectionView;
import com.duggan.workflow.client.util.AppContext;
import com.duggan.workflow.client.util.Definitions;
import com.duggan.workflow.shared.model.CurrentUser;
import com.gwtplatform.dispatch.shared.SecurityCookie;
import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;
import com.gwtplatform.mvp.client.gin.DefaultModule;
import com.duggan.workflow.client.ui.comments.CommentPresenter;
import com.duggan.workflow.client.ui.comments.CommentView;

public class ClientModule extends AbstractPresenterModule {

	@Override
	protected void configure() {
		install(new DefaultModule(ClientPlaceManager.class));
		
		bindConstant().annotatedWith(DefaultPlace.class).to(NameTokens.home);
		
		bindConstant().annotatedWith(SecurityCookie.class).to(Definitions.AUTHENTICATIONCOOKIE);

		bindPresenter(MainPagePresenter.class, MainPagePresenter.MyView.class,
				MainPageView.class, MainPagePresenter.MyProxy.class);

		bindPresenter(HomePresenter.class,
				HomePresenter.MyView.class, HomeView.class,
				HomePresenter.MyProxy.class);

		bindPresenterWidget(TaskItemPresenter.class,
				TaskItemPresenter.MyView.class, TaskItemView.class);

		bindPresenterWidget(HeaderPresenter.class,
				HeaderPresenter.MyView.class, HeaderView.class);

		bindPresenterWidget(TabsPresenter.class, TabsPresenter.MyView.class,
				TabsView.class);

		bindPresenter(PersonalReviewPresenter.class,
				PersonalReviewPresenter.MyView.class, PersonalReviewView.class,
				PersonalReviewPresenter.MyProxy.class);

		bindPresenter(PersonnelReviewPresenter.class,
				PersonnelReviewPresenter.MyView.class,
				PersonnelReviewView.class,
				PersonnelReviewPresenter.MyProxy.class);
		
		bindPresenterWidget(ToolbarPresenter.class,
				ToolbarPresenter.MyView.class, ToolbarView.class);

		bindPresenterWidget(CreateDocPresenter.class,
				CreateDocPresenter.ICreateDocView.class, CreateDocView.class);
		
		bindPresenter(ErrorPagePresenter.class,
				ErrorPagePresenter.MyView.class, ErrorPageView.class,
				ErrorPagePresenter.MyProxy.class);
		
		bindPresenterWidget(ErrorPresenter.class, ErrorPresenter.MyView.class,
				ErrorView.class);

		bindPresenterWidget(GenericDocumentPresenter.class,
				GenericDocumentPresenter.MyView.class,
				GenericDocumentView.class);

		bindPresenter(LoginPresenter.class, LoginPresenter.ILoginView.class,
				LoginView.class, LoginPresenter.MyProxy.class);
		
		bind(CurrentUser.class).asEagerSingleton();
		
		requestStaticInjection(AppContext.class);
		

		bindPresenterWidget(DateGroupPresenter.class,
				DateGroupPresenter.MyView.class, DateGroupView.class);

		bindPresenter(NotfoundPresenter.class, NotfoundPresenter.MyView.class,
				NotfoundView.class, NotfoundPresenter.MyProxy.class);

		bindPresenterWidget(NotificationsPresenter.class,
				NotificationsPresenter.MyView.class, NotificationsView.class);

		bindPresenterWidget(NotePresenter.class,
				NotePresenter.MyView.class, NoteView.class);

		bindPresenter(ActivitiesPresenter.class,
				ActivitiesPresenter.MyView.class, ActivitiesView.class,
				ActivitiesPresenter.MyProxy.class);

		bindPresenterWidget(CommentPresenter.class,
				CommentPresenter.ICommentView.class, CommentView.class);
		
		bindPresenterWidget(AttachmentPresenter.class, 
				AttachmentPresenter.IAttachmentView.class, AttachmentView.class);
		
		bindPresenterWidget(UserSelectionPresenter.class,
				UserSelectionPresenter.MyView.class, UserSelectionView.class);
	}
}
