package controller;

import java.util.ArrayList;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import dao.MessageDao;
import dao.RegisterDao;
import dao.UserDao;
import model.Register;
import model.RegisterLogBean;
import model.User;

public class RegisterController {

	private static DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-M-dd HH:mm:ss");
	
	public static void RegisterLog(int idMessage) {
		RegisterDao.registerLog(new Register(idMessage,	MessageDao.getMessage(idMessage).getMessage(),DateTime.now().toString(dtf)));	
	}
	
	public static void RegisterLog(int idMessage,String username) {
		User user = UserDao.getUserInfo(username);
		int idUser = (user!=null?user.getIdUser():0);
		RegisterDao.registerLog(
				new Register(idMessage,	idUser,
						MessageDao.getMessage(idMessage).getMessage().replaceAll("<login_name>", username)
						,DateTime.now().toString(dtf)
						));
	}

	public static void RegisterLog(int idMessage,String username,String arqname) {
		RegisterDao.registerLog(
				new Register(idMessage,	UserDao.getUserInfo(username).getIdUser(),
						MessageDao.getMessage(idMessage).getMessage().replaceAll("<login_name>", username).replaceAll("<arq_name>", arqname)
						,DateTime.now().toString(dtf)
			));
		
	}
	
	public static ArrayList<RegisterLogBean> getRegisters(String idUser,String idMessage){
		ArrayList<Register> temp = (idUser.equals("TODOS")
									?RegisterDao.getLogs(idMessage.split(" -")[0])
									:RegisterDao.getUserLogs(idUser.split(" -")[0], idMessage.split(" -")[0]));
		ArrayList<RegisterLogBean> registersBeans = new ArrayList<RegisterLogBean>(); 
		for(Register r:temp){
			registersBeans.add(new RegisterLogBean(r.getTimeRegister(), r.getIdMessage(), r.getMessage()) );
		}
		return registersBeans;
	}
	
	public static ArrayList<String> getUsers(){
		ArrayList<String> temp = new ArrayList<String>();
		temp.add("TODOS");
		temp.add("0");
		temp.addAll(UserDao.getAllUsers());
		return temp;
	}
	
	public static ArrayList<String> getMessages(){
		ArrayList<String> temp = new ArrayList<String>();
		temp.add("TODOS");
		temp.addAll(MessageDao.getAllMessages());
		return temp;
	}
}
