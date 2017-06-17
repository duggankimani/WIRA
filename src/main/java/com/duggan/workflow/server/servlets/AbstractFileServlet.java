package com.duggan.workflow.server.servlets;

import java.nio.file.FileStore;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFileAttributeView;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

public abstract class AbstractFileServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected static String BASE_PATH = "";

	@Override
	public void init() throws ServletException {
		super.init();

		synchronized (AbstractFileServlet.class) {
			if (BASE_PATH.isEmpty()) {
				// Not Set,
				// Assign from System Arguments
				BASE_PATH = System.getProperty("app.files.dir");

				if (BASE_PATH == null) {
					// If null, check Servlet Config
					ServletConfig config = this.getServletConfig();
					BASE_PATH = config.getInitParameter("app.files.dir");
				}

				if (BASE_PATH == null) {
					// If null try catalina base
					BASE_PATH = System.getProperty("catalina.base");
				}

				if (BASE_PATH == null) {
					// If null default to current folder
					BASE_PATH = "./";
				}

				// Root Folder Has to be given a name
				BASE_PATH = BASE_PATH + "/Files";

			}

		}

	}
	
	
	public static String getBASEPATH(){
		return BASE_PATH;
	}
	
	
	protected FileAttribute<?> getPermissions() {
		try {
			Path tempFolder = Paths.get(System.getProperty("java.io.tmpdir"));
			FileStore fs = Files.getFileStore(tempFolder);
			if (!fs.supportsFileAttributeView(PosixFileAttributeView.class)) {
				return null;
			}
		} catch (Exception e) {

		}

		return getPosixPermissions();
	}
	
	protected FileAttribute<Set<PosixFilePermission>> getPosixPermissions() {
		Set<PosixFilePermission> perms = new HashSet<>();
		// add permission as rw-r--r-- 644
		perms.add(PosixFilePermission.OWNER_WRITE);
		perms.add(PosixFilePermission.OWNER_READ);
		perms.add(PosixFilePermission.OWNER_EXECUTE);
		perms.add(PosixFilePermission.GROUP_READ);
		perms.add(PosixFilePermission.GROUP_WRITE);
		perms.add(PosixFilePermission.GROUP_EXECUTE);
//		perms.add(PosixFilePermission.OTHERS_READ);
//		perms.add(PosixFilePermission.OTHERS_EXECUTE);
		FileAttribute<Set<PosixFilePermission>> fileAttributes = PosixFilePermissions
				.asFileAttribute(perms);

		return fileAttributes;
	}
}
