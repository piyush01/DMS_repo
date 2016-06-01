package org.dspace.administer;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;

import org.dspace.core.LogManager;
import org.dspace.eperson.EPerson;
import org.dspace.services.EmailService;
import org.dspace.storage.rdbms.DatabaseManager;
import org.dspace.storage.rdbms.TableRow;
import org.dspace.utils.DSpace;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.dspace.authorize.AuthorizeException;
import org.dspace.core.ConfigurationManager;
import org.dspace.core.Context;
import org.dspace.core.Email;
import org.dspace.core.I18nUtil;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
public class NewUserRegister {

	 private static final Logger log = LoggerFactory.getLogger(NewUserRegister.class);
	 UserBean userbean=new UserBean();
	 public UserBean getUserbean() {
			return userbean;
		}

		public void setUserbean(UserBean userbean) {
			this.userbean = userbean;
		}
	 //@ select All user who register it self!
	 public List<UserBean> getUser(Context context) throws SQLException{
			List<UserBean> list=new ArrayList<UserBean>();
			PreparedStatement ps=null;
			ResultSet rs=null;
			String name="";
			try{
				ps=context.getDBConnection().prepareStatement("select e.eperson_id,e.email,m.text_value as first_name,ln.text_value as last_name from eperson"
				+ " e left join metadatavalue m on m.resource_id = e.eperson_id and m.metadata_field_id =124 "+ 
		       " left join metadatavalue ln on ln.resource_id=e.eperson_id and ln.metadata_field_id=125 where e.can_log_in='false' and e.self_registered='true' and status='s'  order by e.eperson_id ");
				rs=ps.executeQuery();
					while(rs.next()){
						UserBean userbean=new UserBean();
						userbean.setUser_id(rs.getInt(1));
						userbean.setEmail(rs.getString(2));
						userbean.setFirst_name(rs.getString(3));
						userbean. setLast_name(rs.getString(4));
						list.add(userbean);
						log.info("inside newUSerregister==============================>"+list.size());
						}
				}catch(SQLException e){
					log.info("Error in getting all user"+e);
				}
			
		 finally {
					if (ps != null) {
						try {
							ps.close();
						} catch (SQLException s) {
							log.error("SQL QueryTable close Error - ", s);
							throw s;
						}
					}
					if (rs != null) {
						try {
							rs.close();
						} catch (SQLException s) {
							log.error("SQL QueryTable close Error - ", s);
							throw s;
						}
					}
				}
			
			return list;
		}
	 public UserBean getAllUser(Context context) throws SQLException{
		 UserBean userbean=new UserBean();
			PreparedStatement ps=null;
			ResultSet rs=null;
			String name="";
			try{
				ps=context.getDBConnection().prepareStatement("select e.eperson_id,e.email,e.password,e.can_log_in,e.self_registered,e.status,e.superior_name,e.superior_email,e.user_designation,m.text_value as first_name,ln.text_value as last_name,ph.text_value as phone,lan.text_value as language from eperson e left join metadatavalue m on m.resource_id = e.eperson_id and m.metadata_field_id =124 left join metadatavalue ln on ln.resource_id=e.eperson_id and ln.metadata_field_id=125 left join metadatavalue ph on ph.resource_id=e.eperson_id and ph.metadata_field_id=126 left join metadatavalue lan on lan.resource_id=e.eperson_id and lan.metadata_field_id=127 ");
				rs=ps.executeQuery();
					while(rs.next()){
						userbean.setUser_id(rs.getInt(1));
						userbean.setEmail(rs.getString(2));
						userbean.setPassword(rs.getString(3));
						userbean.setCan_log_in(rs.getBoolean(4));
						userbean.setSelf_registered(rs.getBoolean(5));
						userbean.setStatus(rs.getString(6));
						userbean.setSuperiorname(rs.getString(7));
						userbean.setSuperioremail(rs.getString(8));
						userbean.setUserdesignation(rs.getString(9));
						userbean.setFirst_name(rs.getString(10));
						userbean. setLast_name(rs.getString(11));
						userbean.setMobileno(rs.getString(12));
						userbean.setLanguage(rs.getString(13));
						
						}
				}catch(SQLException e){
					log.info("Error in getting all user"+e);
				}
			
		 finally {
					if (ps != null) {
						try {
							ps.close();
						} catch (SQLException s) {
							log.error("SQL QueryTable close Error - ", s);
							throw s;
						}
					}
					if (rs != null) {
						try {
							rs.close();
						} catch (SQLException s) {
							log.error("SQL QueryTable close Error - ", s);
							throw s;
						}
					}
				}
			
			return userbean;
		}
	 //checkuser
	 public boolean CheckUser(Context context,String email) throws SQLException
	 {

			PreparedStatement ps=null;
			ResultSet rs=null;
			boolean ispresent=false;
			try {
				ps=context.getDBConnection().prepareStatement("SELECT email FROM eperson WHERE email=?");
				ps.setString(1,email);
				rs=ps.executeQuery();
					if(rs.next())
					{
						ispresent=true;
					}

			} catch (Exception e) {
				log.info("Error in check user data"+e);
			}
			
			 finally {
					if (ps != null) {
						try {
							ps.close();
						} catch (SQLException s) {
							log.error("SQL QueryTable close Error - ", s);
							throw s;
						}
					}
					if (rs != null) {
						try {
							rs.close();
						} catch (SQLException s) {
							log.error("SQL QueryTable close Error - ", s);
							throw s;
						}
					}
				}
			log.info("is present==========>"+ispresent);
		return ispresent;
			
		
	 }
	//Approve User by admin 
	 public int updateUser(Context context,Integer user_id)throws SQLException
	 {
		 int a=0;
	 	PreparedStatement ps1=null;
	 
	 	try {
	 		ps1=context.getDBConnection().prepareStatement("update eperson set can_log_in ='true',status='a'  where eperson_id=?");
	 		ps1.setInt(1, user_id);
	 		 a =ps1.executeUpdate();
	 	} catch (Exception e) {
	 		// TODO: handle exception
	 		log.info("Error in Update user"+e);
	 	}
	 	 finally {
	 			if (ps1 != null) {
	 				try {
	 					ps1.close();
	 				} catch (SQLException s) {
	 					log.error("SQL QueryTable close Error - ", s);
	 					throw s;
	 				}
	 			}
	 			
	 		}
		return a;

	 } 
	
	 //Delete Data From Epersongroup2Eperson table
	 public int deletegroupID(Context context,Integer user_id)throws SQLException
	 {
		 int a=0;
	 	PreparedStatement ps1=null;
	 
	 	try {
	 		ps1=context.getDBConnection().prepareStatement(" DELETE FROM EPersonGroup2EPerson WHERE eperson_id= ? ");
	 		ps1.setInt(1, user_id);
	 		a=ps1.executeUpdate();
	 		log.info("UserId in NewUserRegister DeteteGroup==========>"+user_id);
	 		log.info("UserId in NewUserRegister DeteteGroup ExecuteUpdate ==========>"+a);
	 	} catch (Exception e) {
	 		// TODO: handle exception
	 		log.info("Error in Delete Group id"+e);
	 	}
	 	 finally {
	 			if (ps1 != null) {
	 				try {
	 					ps1.close();
	 				} catch (SQLException s) {
	 					log.error("SQL QueryTable close Error - ", s);
	 					throw s;
	 				}
	 			}
	 			
	 		}
	 	
	return a;

	 }
	 //DisApprove User By admin
	 public int UserDisApprove(Context context,Integer user_id)throws SQLException
	 {
		 int a=0;
	 	PreparedStatement ps1=null;
	 
	 	try {
	 		ps1=context.getDBConnection().prepareStatement("update eperson set status='d'  where eperson_id=?");
	 		ps1.setInt(1, user_id);
	 		 a =ps1.executeUpdate();
	 	} catch (Exception e) {
	 		// TODO: handle exception
	 		log.info("Error in Update user"+e);
	 	}
	 	 finally {
	 			if (ps1 != null) {
	 				try {
	 					ps1.close();
	 				} catch (SQLException s) {
	 					log.error("SQL QueryTable close Error - ", s);
	 					throw s;
	 				}
	 			}
	 			
	 		}
		return a;

	 }
	public void sendApproveEmail(Context context,Integer user_id) throws SQLException, IOException
	{
		 //String notifyRecipient = ConfigurationManager.getProperty("registration.notify");
		EPerson eperson = EPerson.find(context, user_id);
		String notifyRecipient = eperson.getEmail();// ConfigurationManager.getProperty("registration.notify");
		String superioremail=eperson.getSuperiorEmail();
         if (notifyRecipient == null) {
            notifyRecipient = "";
        }
         notifyRecipient = notifyRecipient.trim();
         if(!notifyRecipient.equals(""))
         {
             try
             {
                 
                 log.info("user id==================>"+user_id);
                 Email adminEmail = Email.getEmail(I18nUtil.getEmailFilename(context.getCurrentLocale(), "approve_notify"));
                 adminEmail.addRecipient(notifyRecipient);
                 adminEmail.addRecipient(superioremail);
                 log.info("superioremail================>"+superioremail);
                 log.info("notifyRecipient================>"+notifyRecipient);
                 //adminEmail.addRecipient(eperson.getEmail());
                 log.info("Email Id for addRecipient===================>"+eperson.getEmail());
                 adminEmail.addArgument(ConfigurationManager.getProperty("dspace.name"));
                 log.info("addArgument  dspace.name===========>"+ConfigurationManager.getProperty("dspace.name"));
                 adminEmail.addArgument(ConfigurationManager.getProperty("dspace.url"));
                 log.info("addArgument  dspace.url===========>"+ConfigurationManager.getProperty("dspace.url"));
                 adminEmail.addArgument(eperson.getFirstName() + " " + eperson.getLastName()); // Name
                 adminEmail.addArgument(eperson.getEmail());
                 log.info("    adminEmail.addArgument(eperson.getEmail());===============>"+eperson.getEmail());
                 adminEmail.addArgument(new Date());

                 adminEmail.setReplyTo(eperson.getEmail());

                 adminEmail.send();

                 log.info(LogManager.getHeader(context, "registerion_alert", "user="
                         + eperson.getEmail()));
                /* Properties mailServerProperties = System.getProperties();
                 
              	mailServerProperties.put("mail.smtp.port", "587");
         		mailServerProperties.put("mail.smtp.auth", "true");
         		mailServerProperties.put("mail.smtp.starttls.enable", "true");
         		mailServerProperties.put("mail.smtp.ssl.trust", "mail.cbsl-india.com");
         	 	Session getMailSession= new DSpace().getServiceManager().getServicesByType(EmailService.class).get(0).getSession();
              	MimeMessage generateMailMessage= new MimeMessage(getMailSession);
              	generateMailMessage.setFrom(new InternetAddress( ConfigurationManager.getProperty("mail.from.address")));
              	generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(eperson.getEmail()));
              	//generateMailMessage.addRecipient(Message.RecipientType.CC, new InternetAddress(notifyRecipient));
              	//generateMailMessage.addRecipient(Message.RecipientType.CC, new InternetAddress("sanjeev.kumar@cbsl-india.com"));
              	generateMailMessage.setSubject("Registration Approvel from"+ConfigurationManager.getProperty("dspace.name"));
              	String emailBody = "Hi There! " + "<br><br> <b>Approved Account!</b> "+"<br></br><b>Name:</b>"+""+eperson.getFirstName()+""+eperson.getLastName()+"<br>"+"<b>Email:</b>"+""+eperson.getEmail()+"<br><br> <b>Regards,</b><br>CBSL GROUP Admin";
         		generateMailMessage.setContent(emailBody, "text/html");
         		Transport transport = getMailSession.getTransport("smtp");
         		transport.connect("mail.cbsl-india.com", "sanjeev.kumar@cbsl-india.com", "cbsl@123");
         		transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
         		transport.close();*/
                  log.info(LogManager.getHeader(context, "registerion_alert", "user="
                          + eperson.getEmail()));
             }
             catch (MessagingException me)
             {
                 log.warn(LogManager.getHeader(context,
                     "error_emailing_administrator", ""), me);
             }
         }
	}
	public void sendDisApproveEmail(Context context,Integer user_id) throws SQLException, IOException
	{
		 //String notifyRecipient = ConfigurationManager.getProperty("registration.notify");
		 EPerson eperson = EPerson.find(context, user_id);
		 String notifyRecipient = eperson.getEmail();//ConfigurationManager.getProperty("registration.notify");
		 String superiorEmail =eperson.getSuperiorEmail();
         if (notifyRecipient == null) {
             notifyRecipient = "";
         }
         notifyRecipient = notifyRecipient.trim();
         if(!notifyRecipient.equals(""))
        {
             try
             {
                 
                 log.info("user id==================>"+user_id);
                 Email adminEmail = Email.getEmail(I18nUtil.getEmailFilename(context.getCurrentLocale(), "disapprove_notify"));
                adminEmail.addRecipient(notifyRecipient);
                 log.info("notifyRecipient================>"+notifyRecipient);
                 adminEmail.addRecipient(superiorEmail);
                 log.info("superioremail================>"+superiorEmail);
                 log.info("Email Id for addRecipient===================>"+eperson.getEmail());
                 adminEmail.addArgument(ConfigurationManager.getProperty("dspace.name"));
                 log.info("addArgument  dspace.name===========>"+ConfigurationManager.getProperty("dspace.name"));
                 adminEmail.addArgument(ConfigurationManager.getProperty("dspace.url"));
                 log.info("addArgument  dspace.url===========>"+ConfigurationManager.getProperty("dspace.url"));
                 adminEmail.addArgument(eperson.getFirstName() + " " + eperson.getLastName()); // Name
                 adminEmail.addArgument(eperson.getEmail());
                 log.info("    adminEmail.addArgument(eperson.getEmail());===============>"+eperson.getEmail());
                 adminEmail.addArgument(new Date());

                 adminEmail.setReplyTo(eperson.getEmail());

                 adminEmail.send();

              /*   Properties mailServerProperties = System.getProperties();
            
             	mailServerProperties.put("mail.smtp.port", "587");
        		mailServerProperties.put("mail.smtp.auth", "true");
        		mailServerProperties.put("mail.smtp.starttls.enable", "true");
        		mailServerProperties.put("mail.smtp.ssl.trust", "mail.cbsl-india.com");
        	 	Session getMailSession= new DSpace().getServiceManager().getServicesByType(EmailService.class).get(0).getSession();
             	MimeMessage generateMailMessage= new MimeMessage(getMailSession);
             	generateMailMessage.setFrom(new InternetAddress( ConfigurationManager.getProperty("mail.from.address")));
             	generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(eperson.getEmail()));
             	//generateMailMessage.addRecipient(Message.RecipientType.CC, new InternetAddress(notifyRecipient));
             	//generateMailMessage.addRecipient(Message.RecipientType.CC, new InternetAddress("sanjeev.kumar@cbsl-india.com"));
             	generateMailMessage.setSubject("Registration Approvel Cancel from"+ConfigurationManager.getProperty("dspace.name"));
             	String emailBody = "Hi There! " + "<br><br> <b>Disapproved Account</b> "+"<br><b>Name:</b>"+eperson.getFirstName()+""+eperson.getLastName()+"<br>"+"<b>Email:</b>"+eperson.getEmail()+"<br><br> <b>Regards,</b><br>CBSL GROUP Admin";
        		generateMailMessage.setContent(emailBody, "text/html");
        		Transport transport = getMailSession.getTransport("smtp");
        		transport.connect("mail.cbsl-india.com", "sanjeev.kumar@cbsl-india.com", "cbsl@123");
        		transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
        		transport.close();*/
                 log.info(LogManager.getHeader(context, "registerion_alert", "user="
                         + eperson.getEmail()));
             }
             catch (MessagingException me)
             {
                 log.warn(LogManager.getHeader(context,
                     "error_emailing_administrator", ""), me);
             }
         }
	}
	public void sendWelcomeEmail(Context context,Integer user_id) throws SQLException, IOException
	{
		 //String notifyRecipient = ConfigurationManager.getProperty("registration.notify");
		EPerson eperson = EPerson.find(context, user_id);
		String notifyRecipient = eperson.getEmail();// ConfigurationManager.getProperty("registration.notify");
		//String superioremail=eperson.getSuperiorEmail();
         if (notifyRecipient == null) {
            notifyRecipient = "";
        }
         notifyRecipient = notifyRecipient.trim();
         if(!notifyRecipient.equals(""))
         {
             try
             {
                 
               
                 Email adminEmail = Email.getEmail(I18nUtil.getEmailFilename(context.getCurrentLocale(), "welcome_notify"));
                 adminEmail.addRecipient(notifyRecipient);
                 //adminEmail.addRecipient(superioremail);
                 //log.info("superioremail================>"+superioremail);
                 log.info("notifyRecipient================>"+notifyRecipient);
                 //adminEmail.addRecipient(eperson.getEmail());
                 log.info("Email Id for addRecipient===================>"+eperson.getEmail());
                 adminEmail.addArgument(ConfigurationManager.getProperty("dspace.name"));
                 log.info("addArgument  dspace.name===========>"+ConfigurationManager.getProperty("dspace.name"));
                 adminEmail.addArgument(ConfigurationManager.getProperty("dspace.url"));
                 log.info("addArgument  dspace.url===========>"+ConfigurationManager.getProperty("dspace.url"));
                 adminEmail.addArgument(eperson.getFirstName() + " " + eperson.getLastName()); // Name
                 adminEmail.addArgument(eperson.getEmail());
                 log.info("    adminEmail.addArgument(eperson.getEmail());===============>"+eperson.getEmail());
                 adminEmail.addArgument(new Date());

                 adminEmail.setReplyTo(eperson.getEmail());

                 adminEmail.send();

                 log.info(LogManager.getHeader(context, "registerion_alert", "user="
                         + eperson.getEmail()));
                /* Properties mailServerProperties = System.getProperties();
                 
              	mailServerProperties.put("mail.smtp.port", "587");
         		mailServerProperties.put("mail.smtp.auth", "true");
         		mailServerProperties.put("mail.smtp.starttls.enable", "true");
         		mailServerProperties.put("mail.smtp.ssl.trust", "mail.cbsl-india.com");
         	 	Session getMailSession= new DSpace().getServiceManager().getServicesByType(EmailService.class).get(0).getSession();
              	MimeMessage generateMailMessage= new MimeMessage(getMailSession);
              	generateMailMessage.setFrom(new InternetAddress( ConfigurationManager.getProperty("mail.from.address")));
              	generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(eperson.getEmail()));
              	//generateMailMessage.addRecipient(Message.RecipientType.CC, new InternetAddress(notifyRecipient));
              	//generateMailMessage.addRecipient(Message.RecipientType.CC, new InternetAddress("sanjeev.kumar@cbsl-india.com"));
              	generateMailMessage.setSubject("Registration Approvel from"+ConfigurationManager.getProperty("dspace.name"));
              	String emailBody = "Hi There! " + "<br><br> <b>Approved Account!</b> "+"<br></br><b>Name:</b>"+""+eperson.getFirstName()+""+eperson.getLastName()+"<br>"+"<b>Email:</b>"+""+eperson.getEmail()+"<br><br> <b>Regards,</b><br>CBSL GROUP Admin";
         		generateMailMessage.setContent(emailBody, "text/html");
         		Transport transport = getMailSession.getTransport("smtp");
         		transport.connect("mail.cbsl-india.com", "sanjeev.kumar@cbsl-india.com", "cbsl@123");
         		transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
         		transport.close();*/
                  log.info(LogManager.getHeader(context, "registerion_alert", "user="
                          + eperson.getEmail()));
             }
             catch (MessagingException me)
             {
                 log.warn(LogManager.getHeader(context,
                     "error_emailing_administrator", ""), me);
             }
         }
	}
/*	 public static void sendEmailUser(Context context, String email)throws  IOException, SQLException
	    {
	        //String base = ConfigurationManager.getProperty("dspace.url");
	        String notifyRecipient = ConfigurationManager.getProperty("registration.notify");
	            try
	            {
	               
	                Properties mailServerProperties = System.getProperties();
	           
	            mailServerProperties.put("mail.smtp.port", "587");
	       		mailServerProperties.put("mail.smtp.auth", "true");
	       		mailServerProperties.put("mail.smtp.starttls.enable", "true");
	       		mailServerProperties.put("mail.smtp.ssl.trust", "mail.cbsl-india.com");
	       	 	Session getMailSession= new DSpace().getServiceManager().getServicesByType(EmailService.class).get(0).getSession();
	            MimeMessage generateMailMessage= new MimeMessage(getMailSession);
	            generateMailMessage.setFrom(new InternetAddress( ConfigurationManager.getProperty("mail.from.address")));
	            generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
	            //generateMailMessage.addRecipient(Message.RecipientType.CC, new InternetAddress("sanjeev.kumar@cbsl-india.com"));
	            generateMailMessage.setSubject("WelCome to "+""+ConfigurationManager.getProperty("dspace.name"));
	            String emailBody = "Hi There! "+"<br><br>Thanks for choosing DMS!" + "<br><br> <b>Your Account are not Active Now!</b> "+"<br><br>Get started on a simple and incredible experience on DMS"+"<br>Plese Contact site Admin :- "+""+notifyRecipient+"<br><br><br> <b>Regards,</b><br>CBSL GROUP SoftWare Team";
	       		generateMailMessage.setContent(emailBody, "text/html");
	       		Transport transport = getMailSession.getTransport("smtp");
	       		transport.connect("mail.cbsl-india.com", "sanjeev.kumar@cbsl-india.com", "cbsl@123");
	       		transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
	       		transport.close();
	            log.info(LogManager.getHeader(context, "registerion_alert", "user="+email));
	            }
	            catch (MessagingException me)
	            {
	                log.warn(LogManager.getHeader(context,"error_emailing_administrator", ""), me);
	            }
	        
	    }*/ 
	 }
