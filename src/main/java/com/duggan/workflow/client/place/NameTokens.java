package com.duggan.workflow.client.place;

public class NameTokens {

	public static final String landingpage = "/landingpage";

	public static final String applicationslisting = "/applicationslisting";

	public static final String applicationsBase = "/applications";

	public static final String applicationsNew = "/applications/{processRefId}";

	public static final String applications = "/applications/{processRefId}/{docRefId}";

	public static final String activateacc = "/activateacc/{uid}/{reason}";

	public static final String splash = "/splash";

	public static final String signin = "/signin";

	public static final String explorer = "/explorer";
	public static final String processlist = "/processlist";
	public static final String reports = "/reports";
	public static final String reportsview = "/reports/{reportRefId}";
	public static final String payment = "/payment";
	public static final String home = "/home";
	public static final String error = "/error";
	public static final String loginWithRedirect = "/login/{redirect}";
	public static final String error404 = "/error404";
	public static final String profile = "/profile";
	public static final String settings = "/settings";
	public static final String usermgt = "/usermgt";
	public static final String usermgtwithparam = "/usermgt/{page}";
	public static final String dashboards = "/dashboards";
	public static final String datasources = "/datasources";
	public static final String formbuilder = "/formbuilder";
	public static final String processes = "/processes";
	public static final String processconf = "/processconf";
	public static final String tasks = "/tasks";
	public static final String drafts = "/drafts";
	public static final String participated = "/participated";
	public static final String inbox = "/inbox";
	public static final String inboxwithparams = "/inbox/{filter}";
	public static final String suspended = "/suspended";
	public static final String search = "/search/{docRefId}";
	public static final String outputdocs = "/outputdocs";
	public static final String triggers = "/triggers";
	public static final String unassigned = "/unassigned";
	public static final String registry = "/registry";
	public static final String registryview = "/registry/{docRefId}";
	public static final String datatable = "/datatable";
	public static final String caseview = "/caseview/{docRefId}";
	public static final String messages = "/messages";
	public static final String activitiesPerProcess = "/activities/{processRefId}";
	public static final String activitiesPerProcessAdd = "/activities/{processRefId}/{action}";
	

	public static final String loginPage = "login.html";

	public static String getHome() {
		return home;
	}

	public static String getError404() {
		return error404;
	}

	//	public static String getActivities() {
	//		return activities;
	//	}

	public static String getProfile() {
		return profile;
	}

	public static String getSettings() {
		return settings;
	}

	public static String getTasks() {
		return tasks;
	}

	public static String getOutputdocs() {
		return outputdocs;
	}

	public static String getPayment() {
		return payment;
	}

	public static String getOnLoginDefaultPage() {
		return home;
	}

	public static String getReports() {
		return reports;
	}

	public static String getProcesslist() {
		return processlist;
	}

	public static String getExplorer() {
		return explorer;
	}

	public static String getSignin() {
		return signin;
	}

	public static String getSplash() {
		return splash;
	}

	public static String getAcc() {
		return activateacc;
	}

	public static String getApplications() {
		return applications;
	}

	public static String getApplicationslisting() {
		return applicationslisting;
	}

	public static String getLandingpage() {
		return landingpage;
	}

}
