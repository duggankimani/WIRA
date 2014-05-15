package com.duggan.workflow.client.gin;

import com.duggan.workflow.client.place.ClientPlaceManager;
import com.duggan.workflow.client.place.DefaultPlace;
import com.duggan.workflow.client.place.NameTokens;
import com.duggan.workflow.client.ui.AppManager;
import com.duggan.workflow.client.ui.MainPagePresenter;
import com.duggan.workflow.client.ui.MainPageView;
import com.duggan.workflow.client.ui.activityfeed.ActivitiesPresenter;
import com.duggan.workflow.client.ui.activityfeed.ActivitiesView;
import com.duggan.workflow.client.ui.addDoc.DocumentPopupPresenter;
import com.duggan.workflow.client.ui.addDoc.DocumentPopupView;
import com.duggan.workflow.client.ui.addDoc.doctypeitem.DocTypeItemPresenter;
import com.duggan.workflow.client.ui.addDoc.doctypeitem.DocTypeItemView;
import com.duggan.workflow.client.ui.admin.AdminHomePresenter;
import com.duggan.workflow.client.ui.admin.AdminHomeView;
import com.duggan.workflow.client.ui.admin.TabPanel;
import com.duggan.workflow.client.ui.admin.dashboard.DashboardPresenter;
import com.duggan.workflow.client.ui.admin.dashboard.DashboardView;
import com.duggan.workflow.client.ui.admin.dashboard.charts.PieChartPresenter;
import com.duggan.workflow.client.ui.admin.dashboard.charts.PieChartView;
import com.duggan.workflow.client.ui.admin.dashboard.linegraph.LineGraphPresenter;
import com.duggan.workflow.client.ui.admin.dashboard.linegraph.LineGraphView;
import com.duggan.workflow.client.ui.admin.dashboard.table.TableDataPresenter;
import com.duggan.workflow.client.ui.admin.dashboard.table.TableDataView;
import com.duggan.workflow.client.ui.admin.ds.DataSourcePresenter;
import com.duggan.workflow.client.ui.admin.ds.DataSourceView;
import com.duggan.workflow.client.ui.admin.ds.item.DSItemPresenter;
import com.duggan.workflow.client.ui.admin.ds.item.DSItemView;
import com.duggan.workflow.client.ui.admin.ds.save.DSSavePresenter;
import com.duggan.workflow.client.ui.admin.ds.save.DSSaveView;
import com.duggan.workflow.client.ui.admin.formbuilder.FormBuilderPresenter;
import com.duggan.workflow.client.ui.admin.formbuilder.FormBuilderView;
import com.duggan.workflow.client.ui.admin.formbuilder.propertypanel.PropertyPanelPresenter;
import com.duggan.workflow.client.ui.admin.formbuilder.propertypanel.PropertyPanelView;
import com.duggan.workflow.client.ui.admin.processes.ProcessPresenter;
import com.duggan.workflow.client.ui.admin.processes.ProcessView;
import com.duggan.workflow.client.ui.admin.processes.save.ProcessSavePresenter;
import com.duggan.workflow.client.ui.admin.processes.save.ProcessSaveView;
import com.duggan.workflow.client.ui.admin.processitem.ProcessItemPresenter;
import com.duggan.workflow.client.ui.admin.processitem.ProcessItemView;
import com.duggan.workflow.client.ui.admin.reports.ReportsPresenter;
import com.duggan.workflow.client.ui.admin.reports.ReportsView;
import com.duggan.workflow.client.ui.admin.settings.SettingsPresenter;
import com.duggan.workflow.client.ui.admin.settings.SettingsView;
import com.duggan.workflow.client.ui.admin.users.UserPresenter;
import com.duggan.workflow.client.ui.admin.users.UserView;
import com.duggan.workflow.client.ui.admin.users.groups.GroupPresenter;
import com.duggan.workflow.client.ui.admin.users.groups.GroupView;
import com.duggan.workflow.client.ui.admin.users.item.UserItemPresenter;
import com.duggan.workflow.client.ui.admin.users.item.UserItemView;
import com.duggan.workflow.client.ui.admin.users.save.UserSavePresenter;
import com.duggan.workflow.client.ui.admin.users.save.UserSaveView;
import com.duggan.workflow.client.ui.comments.CommentPresenter;
import com.duggan.workflow.client.ui.comments.CommentView;
import com.duggan.workflow.client.ui.docActivity.DocumentActivityPresenter;
import com.duggan.workflow.client.ui.docActivity.DocumentActivityView;
import com.duggan.workflow.client.ui.document.GenericDocumentPresenter;
import com.duggan.workflow.client.ui.document.GenericDocumentView;
import com.duggan.workflow.client.ui.error.ErrorPagePresenter;
import com.duggan.workflow.client.ui.error.ErrorPageView;
import com.duggan.workflow.client.ui.error.ErrorPresenter;
import com.duggan.workflow.client.ui.error.ErrorView;
import com.duggan.workflow.client.ui.error.NotfoundPresenter;
import com.duggan.workflow.client.ui.error.NotfoundView;
import com.duggan.workflow.client.ui.filter.FilterPresenter;
import com.duggan.workflow.client.ui.filter.FilterView;
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
import com.duggan.workflow.client.ui.popup.GenericPopupPresenter;
import com.duggan.workflow.client.ui.popup.GenericPopupView;
import com.duggan.workflow.client.ui.profile.ProfilePresenter;
import com.duggan.workflow.client.ui.profile.ProfileView;
import com.duggan.workflow.client.ui.save.CreateDocPresenter;
import com.duggan.workflow.client.ui.save.CreateDocView;
import com.duggan.workflow.client.ui.save.form.GenericFormPresenter;
import com.duggan.workflow.client.ui.save.form.GenericFormView;
import com.duggan.workflow.client.ui.task.perfomancereview.PersonnelReviewPresenter;
import com.duggan.workflow.client.ui.task.perfomancereview.PersonnelReviewView;
import com.duggan.workflow.client.ui.tasklistitem.DateGroupPresenter;
import com.duggan.workflow.client.ui.tasklistitem.DateGroupView;
import com.duggan.workflow.client.ui.tasklistitem.TaskItemPresenter;
import com.duggan.workflow.client.ui.tasklistitem.TaskItemView;
import com.duggan.workflow.client.ui.toolbar.ToolbarPresenter;
import com.duggan.workflow.client.ui.toolbar.ToolbarView;
import com.duggan.workflow.client.ui.upload.UploadDocumentPresenter;
import com.duggan.workflow.client.ui.upload.UploadDocumentView;
import com.duggan.workflow.client.ui.upload.attachment.AttachmentPresenter;
import com.duggan.workflow.client.ui.upload.attachment.AttachmentView;
import com.duggan.workflow.client.ui.upload.href.IFrameDataPresenter;
import com.duggan.workflow.client.ui.upload.href.IFrameDataView;
import com.duggan.workflow.client.ui.user.UserSelectionPresenter;
import com.duggan.workflow.client.ui.user.UserSelectionView;
import com.duggan.workflow.client.util.AppContext;
import com.duggan.workflow.client.util.Definitions;
import com.gwtplatform.dispatch.shared.SecurityCookie;
import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;
import com.gwtplatform.mvp.client.gin.DefaultModule;

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
				TaskItemPresenter.ITaskItemView.class, TaskItemView.class);

		bindPresenterWidget(HeaderPresenter.class,
				HeaderPresenter.IHeaderView.class, HeaderView.class);

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
		
		requestStaticInjection(AppContext.class);
		requestStaticInjection(AppManager.class);

		bindPresenterWidget(DateGroupPresenter.class,
				DateGroupPresenter.MyView.class, DateGroupView.class);

		bindPresenter(NotfoundPresenter.class, NotfoundPresenter.MyView.class,
				NotfoundView.class, NotfoundPresenter.MyProxy.class);

		bindPresenterWidget(NotificationsPresenter.class,
				NotificationsPresenter.MyView.class, NotificationsView.class);

		bindPresenterWidget(NotePresenter.class,
				NotePresenter.MyView.class, NoteView.class);

		bindPresenterWidget(ActivitiesPresenter.class,
				ActivitiesPresenter.MyView.class, ActivitiesView.class);

		bindPresenterWidget(CommentPresenter.class,
				CommentPresenter.ICommentView.class, CommentView.class);
		
		bindPresenterWidget(AttachmentPresenter.class, 
				AttachmentPresenter.IAttachmentView.class, AttachmentView.class);
		
		bindPresenterWidget(UserSelectionPresenter.class,
				UserSelectionPresenter.MyView.class, UserSelectionView.class);

		bindPresenterWidget(UploadDocumentPresenter.class,
				UploadDocumentPresenter.MyView.class, UploadDocumentView.class);

		bindPresenterWidget(FilterPresenter.class,
				FilterPresenter.MyView.class, FilterView.class);

		bindPresenter(AdminHomePresenter.class,
				AdminHomePresenter.MyView.class, AdminHomeView.class,
				AdminHomePresenter.MyProxy.class);

		bindPresenterWidget(ProcessSavePresenter.class,
				ProcessSavePresenter.IProcessSaveView.class, ProcessSaveView.class);

		bindPresenterWidget(ProcessItemPresenter.class,
				ProcessItemPresenter.MyView.class, ProcessItemView.class);
		
		bindPresenter(ProcessPresenter.class, ProcessPresenter.IProcessView.class,
				ProcessView.class,
				ProcessPresenter.MyProxy.class);


		bindPresenter(UserPresenter.class, UserPresenter.MyView.class,
				UserView.class,
				UserPresenter.MyProxy.class);

		bindPresenter(DashboardPresenter.class,
				DashboardPresenter.IDashboardView.class, DashboardView.class,
				DashboardPresenter.MyProxy.class);

		bindPresenterWidget(ReportsPresenter.class,
				ReportsPresenter.MyView.class, ReportsView.class);
		
		bindPresenterWidget(UserSavePresenter.class,
				UserSavePresenter.IUserSaveView.class, UserSaveView.class);
		
		bindPresenterWidget(UserItemPresenter.class, UserItemPresenter.MyView.class,
				UserItemView.class);
		
		bindPresenterWidget(GroupPresenter.class, GroupPresenter.MyView.class, GroupView.class);

		bindPresenter(FormBuilderPresenter.class,
				FormBuilderPresenter.IFormBuilderView.class, FormBuilderView.class,
				FormBuilderPresenter.MyProxy.class);

		bindPresenterWidget(PropertyPanelPresenter.class,
				PropertyPanelPresenter.MyView.class, PropertyPanelView.class);
		
		bindPresenterWidget(GenericPopupPresenter.class, GenericPopupPresenter.MyView.class,
				GenericPopupView.class);
		
		bindPresenterWidget(GenericFormPresenter.class, GenericFormPresenter.ICreateDocView.class,
				GenericFormView.class);

		bindPresenterWidget(DocumentPopupPresenter.class,
				DocumentPopupPresenter.MyView.class, DocumentPopupView.class);

		bindPresenterWidget(DocTypeItemPresenter.class,
				DocTypeItemPresenter.MyView.class, DocTypeItemView.class);

		bindPresenterWidget(DocumentActivityPresenter.class,
				DocumentActivityPresenter.MyView.class,
				DocumentActivityView.class);
		
		bindPresenterWidget(IFrameDataPresenter.class,
				IFrameDataPresenter.IFrameView.class, IFrameDataView.class);

		bindPresenter(DataSourcePresenter.class,
				DataSourcePresenter.IDataSourceView.class,
				DataSourceView.class, DataSourcePresenter.MyProxy.class);
		
		bindPresenterWidget(DSItemPresenter.class,
				DSItemPresenter.MyView.class,
				DSItemView.class);
		
		bindPresenterWidget(DSSavePresenter.class,
				DSSavePresenter.IDSSaveView.class,
				DSSaveView.class);

		bindPresenterWidget(ProfilePresenter.class, ProfilePresenter.IProfileView.class,
				ProfileView.class);
		
		bindPresenterWidget(PieChartPresenter.class, PieChartPresenter.IPieChartView.class,
				PieChartView.class);
		
		bindPresenterWidget(LineGraphPresenter.class, LineGraphPresenter.ILineGraphView.class,
				LineGraphView.class);

		bindPresenter(SettingsPresenter.class, SettingsPresenter.ISettingsView.class,
				SettingsView.class, SettingsPresenter.MyProxy.class);
		
		bindPresenterWidget(TableDataPresenter.class, TableDataPresenter.ITableDataView.class,
				TableDataView.class);
		
		 bind(TabPanel.class);

	}
}
