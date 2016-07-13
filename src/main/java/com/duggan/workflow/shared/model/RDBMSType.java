package com.duggan.workflow.shared.model;

import com.wira.commons.shared.models.Listable;

public enum RDBMSType implements Listable{
	
	IBMDB2("IBM DB2",
	"jdbc:db2://<HOST>:<PORT>/<DB>",
	"COM.ibm.db2.jdbc.app.DB2Driver"),

	JDBCODBC("JDBC-ODBC Bridge",
	"jdbc:odbc:<DB>",
	"sun.jdbc.odbc.JdbcOdbcDriver"),

	MSSQLSERVER("Microsoft SQL Server",
	"jdbc:weblogic:mssqlserver4:<DB>@<HOST>:<PORT>",
	"weblogic.jdbc.mssqlserver4.Driver"),

	ORACLETHIN("Oracle Thin",
	"jdbc:oracle:thin:@<HOST>:<PORT>:<SID>",
	"oracle.jdbc.driver.OracleDriver"),

	FIREBIRDJCA("Firebird (JCA/JDBC Driver)",
	"jdbc:firebirdsql:[//<HOST>[:<PORT>]/]<DB>",
	"org.firebirdsql.jdbc.FBDriver"),

	MSSQLJTURBO("Microsoft SQL Server (JTurbo Driver)",
	"jdbc:JTurbo://<HOST>:<PORT>/<DB>",
	"com.ashna.jturbo.driver.Driver"),

	MSSQLSPRINTA("Microsoft SQL Server (Sprinta Driver)",
	"jdbc:inetdae:<HOST>:<PORT>?database=<DB>",
	"com.inet.tds.TdsDriver"),

	MSSQL2000("Microsoft SQL Server 2000 (Microsoft Driver)",
	"jdbc:microsoft:sqlserver://<HOST>:<PORT>[;DatabaseName=<DB>]",
	"com.microsoft.sqlserver.jdbc.SQLServerDriver"),

	MSSQL2008("Microsoft SQL Server 2008 and Later (Microsoft Driver)",
			"jdbc:sqlserver://<HOST>:<PORT>[;DatabaseName=<DB>]",
			"com.microsoft.sqlserver.jdbc.SQLServerDriver"),
			
	MYSQL("MySQL Driver",
			"jdbc:mysql://<HOST>:<PORT>/<DB>",
			"com.mysql.jdbc.Driver"),
			
	MYSQLMM("MySQL (MM.MySQL Driver)",
	"jdbc:mysql://<HOST>:<PORT>/<DB>",
	"org.gjt.mm.mysql.Driver"),

	ORACLEOCI8i("Oracle OCI 8i",
	"jdbc:oracle:oci8:@<SID>",
	"oracle.jdbc.driver.OracleDriver"),

	ORACLEOCI9i("Oracle OCI 9i",
	"jdbc:oracle:oci:@<SID>",
	"oracle.jdbc.driver.OracleDriver"),

	PostgreSQLv65("PostgreSQL (v6.5 and earlier)",
	"jdbc:postgresql://<HOST>:<PORT>/<DB>",
	"postgresql.Driver"),

	PostgreSQLv70("PostgreSQL (v7.0 and later)",
	"jdbc:postgresql://<HOST>:<PORT>/<DB>",
	"org.postgresql.Driver"),

	Sybase42("Sybase (jConnect 4.2 and earlier)",
	"jdbc:sybase:Tds:<HOST>:<PORT>",
	"com.sybase.jdbc.SybDriver"),

	Sybase52("Sybase (jConnect 5.2)",
	"jdbc:sybase:Tds:<HOST>:<PORT>",
	"com.sybase.jdbc2.jdbc.SybDriver");

	
	String display;
	String url;
	String driverClass;
	
	private RDBMSType(String display, String url, String driverClass) {
		this.display = display;
		this.url = url;
		this.driverClass= driverClass;
	}
	
	public String getUrl(){
		return url;
	}
	
	public String getDriverClass(){
		return driverClass;
	}
	
	@Override
	public String getName() {
		return this.name();
	}

	@Override
	public String getDisplayName() {

		return display;
	}

}
