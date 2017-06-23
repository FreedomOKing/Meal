package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.cm.bean.comments;
import com.cm.bean.dishes;
import com.cm.bean.orders;
import com.cm.bean.tb_addrs;
import com.cm.bean.tb_collect;
import com.cm.bean.users;

@SuppressWarnings({ "unchecked", "serial", "rawtypes" })
public class ServletService extends HttpServlet {

	java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	java.text.SimpleDateFormat formatter1 = new java.text.SimpleDateFormat(
			"HH:mm");
	java.text.SimpleDateFormat formatdate = new java.text.SimpleDateFormat(
			"yyyy-MM-dd");
	private String currentTime = "";// 得到当前系统时间
	private String currentDate = "";// 得到当前系统日期

	private final String write = "";
	private String sqlString;
	private Session session = null;
	private HttpServletRequest request;

	public ServletService() {
		super();
		session = HibernateSessionFactory.getSession();
	}

	@Override
	public void destroy() {
		super.destroy();
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		initRequest(request, response);

		PrintWriter out = response.getWriter();
		String action = request.getParameter("Action");
		System.out.println(action);
		String write = "";
		String sqlString = "";
		// 登录验证
		if (action.equals("login")) {
			write = login();

		}
		if (action.equals("getOneRow")) {
			write = getOneRow();
		}
		if (action.equals("Del")) {
			write = Del();
		}

		// 管理员登录验证
		if (action.equals("adminlogin")) {
			write = adminlogin();
		}
		if (action.equals("getdisheslist")) {
			write = getdisheslist();

		}

		if (action.equals("getmyorderslist")) {
			write = getmyorderslist();
		}
		if (action.equals("getaddrlist")) {
			write = getaddrlist();
		}
		if (action.equals("getuserslist")) {
			write = getuserslist();
		}

		if (action.equals("ChangeStatus")) {
			write = changestatus();
		}
		if (action.equals("getComment")) {
			write = getComment();

		}
		System.out.println(write);
		out.println(write);
		out.flush();
		out.close();

	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		initRequest(request, response);

		PrintWriter out = response.getWriter();
		String action = request.getParameter("Action");
		String write = "";
		// 注册
		if (action.equals("register")) {
			users model = new users();
			if (request.getParameter("id") == null
					|| request.getParameter("id").equals("0")) {
				model = new users();

			} else {
				model = (users) (session.createQuery(
						" from users where id=" + request.getParameter("id"))
						.list().get(0));
			}
			model.setTypename(request.getParameter("typename"));
			model.setLoginid(request.getParameter("loginid"));
			model.setPasswords(request.getParameter("password"));
			model.setName(getChinese(request.getParameter("name")));
			model.setPhone((request.getParameter("phone")));
			model.setAddr(getChinese(request.getParameter("addr")));
			model.setIsenable(Integer.valueOf(request.getParameter("isenable")));
			Transaction tran = session.beginTransaction();
			session.save(model);
			tran.commit();
			write = "1";
		}
		if (action.equals("updatePwd")) {
			write = updatePwd();
		}

		if (action.equals("createorder")) {
			write = createorder();
		}
		if (action.equals("collect")) {
			int userid = Integer.valueOf(request.getParameter("userid"));
			int proid = Integer.valueOf(request.getParameter("proid"));
			List list = session.createQuery(
					" from tb_collect where userid=" + userid + " and proid="
							+ proid).list();
			if (list.size() > 0) {
				write = "-1";
			} else {
				tb_collect model = new tb_collect();
				model.setUserid(Integer.valueOf(request.getParameter("userid")));
				model.setProid(Integer.valueOf(request.getParameter("proid")));
				model.setCreatetime(currentTime);
				Transaction tran = session.beginTransaction();
				session.save(model);
				tran.commit();
				write = "1";
			}

		}
		if (action.equals("edit")) {
			write = edit();
		}
		if (action.equals("edituser")) {
			write = edituser();
		}
		if (action.equals("editaddr")) {
			write = editaddr();
		}
		if (action.equals("createcomment")) {
			write = createcomment();
		}
		BaseUtil.LogII("write=" + write);
		out.println(write);
		out.flush();
		out.close();
	}

	private String login() {
		String write = "";
		String loginid = request.getParameter("loginid");
		String passwords = request.getParameter("passwords");
		String typename = request.getParameter("typename");
		sqlString = "select * from users where loginid='" + loginid
				+ "' and passwords='" + passwords + "' and typename='"
				+ typename + "'";
		write = HibernateSessionFactory.selectToJson(sqlString);
		return write;
	}

	private String adminlogin() {
		String write = "";
		String loginid = request.getParameter("loginid");
		String passwords = request.getParameter("passwords");
		List<users> list = session.createQuery(
				" from admins where loginid='" + loginid + "' and passwords='"
						+ passwords + "'").list();
		if (list.size() > 0) {
			write = "1";
		} else {
			write = "0";
		}
		return write;
	}

	private String getComment() {
		String write = "";
		String sqlString = "select * from comments where 1=1 ";
		if (request.getParameter("dishesid") != null) {
			sqlString += " and dishesid =" + request.getParameter("dishesid");
		}
		sqlString += " order by id desc";

		write = HibernateSessionFactory.selectToJson(sqlString);
		return write;
	}

	private String getmyorderslist() {

		String userid = request.getParameter("userid");
		String typename = request.getParameter("typename");

		String write = "";
		sqlString = "SELECT orders.*, dishes.title,dishes.img_url, users.`name` businessname FROM orders INNER JOIN dishes ON orders.dishesid = dishes.id INNER JOIN users ON dishes.businessid = users.id   ";
		sqlString += " where  1=1";

		if (typename.equals("用户") && !BaseUtil.isEmpty(userid)) {
			sqlString += " and orders.userid=" + userid;
		}
		if (typename.equals("商家") && !BaseUtil.isEmpty(userid)) {
			sqlString += " and  dishes.businessid=" + userid;
		}

		sqlString += " order by orders.id desc";

		write = HibernateSessionFactory.selectToJson(sqlString);
		return write;
	}

	private String getdisheslist() {
		String keyword = request.getParameter("keyword");
		String businessid = request.getParameter("businessid");
		String index = request.getParameter("index");
		String write = "";
		sqlString = "select  dishes.*, users.name businessname FROM dishes INNER JOIN users ON dishes.businessid = users.id   ";
		sqlString += " where  1=1";
		if (!BaseUtil.isEmpty(keyword)) {
			sqlString += " and title like '%" + keyword + "%'";
		}
		if (!BaseUtil.isEmpty(businessid)) {
			sqlString += " and dishes.businessid=" + businessid;
		}
		sqlString += " order by dishes.id desc";
		if (!BaseUtil.isEmpty(index)) {
			sqlString += " limit " + index + "," + index + 10 + "";
		}

		write = HibernateSessionFactory.selectToJson(sqlString);
		return write;
	}

	private String getaddrlist() {
		String userid = request.getParameter("userid");

		String write = "";
		String sqlString = "SELECT * from tb_addrs ";
		sqlString += " where 1=1 ";
		if (!BaseUtil.isEmpty(userid)) {
			sqlString += " and userid=" + userid;
		}

		sqlString += " order by id desc";

		write = HibernateSessionFactory.selectToJson(sqlString);
		return write;
	}

	private String getuserslist() {
		String typename = request.getParameter("typename");
		String userid = request.getParameter("userid");
		String iscollect = request.getParameter("iscollect");

		String write = "";
		sqlString = "SELECT * from users ";
		sqlString += " where  1=1";
		if (!BaseUtil.isEmpty(typename)) {
			sqlString += " and typename='" + typename + "'";
		}
		if (!BaseUtil.isEmpty(iscollect)) {
			sqlString += " and id in(select proid from tb_collect where userid="
					+ userid + ")";
		}
		sqlString += " order by id desc";
		write = HibernateSessionFactory.selectToJson(sqlString);
		return write;
	}

	private String edit() throws UnsupportedEncodingException {
		int id = Integer.valueOf((request.getParameter("id")));
		dishes model;
		if (id == 0) {
			model = new dishes();

		} else {
			model = (dishes) (session
					.createQuery(" from dishes where id=" + id).list().get(0));
		}

		if (request.getParameter("img_url") != null
				&& request.getParameter("img_url").length() > 0) {
			model.setImg_url(request.getParameter("img_url").trim());
		}

		model.setIntro(getChinese(request.getParameter("intro")));
		model.setTitle(getChinese(request.getParameter("title")));
		model.setPrice(Float.valueOf(request.getParameter("price")));
		model.setAmount(Float.valueOf(request.getParameter("amount")));
		model.setBusinessid(Integer.valueOf(request.getParameter("businessid")));
		model.setTypename(getChinese(request.getParameter("typename")));
		Transaction tran = session.beginTransaction();

		if (id != 0) {
			session.update(model);
		} else {
			session.save(model);
		}
		tran.commit();
		return "1";
	}

	// 修改订单状态
	private String changestatus() {
		int id = Integer.valueOf((request.getParameter("ID")));
		String status = request.getParameter("status");
		orders model;
		model = (orders) (session.createQuery(" from orders where id=" + id)
				.list().get(0));
		model.setStatus(status);
		Transaction tran = session.beginTransaction();
		session.save(model);
		tran.commit();
		return "1";
	}

	private String edituser() throws UnsupportedEncodingException {
		int id = Integer.valueOf((request.getParameter("ID")));
		users model;
		if (id == 0) {
			model = new users();

		} else {
			model = (users) (session.createQuery(" from users where id=" + id)
					.list().get(0));
		}

		model.setLoginid(getChinese(request.getParameter("loginid")));
		model.setName(getChinese(request.getParameter("name")));
		model.setPasswords(request.getParameter("passwords"));
		model.setAddr(getChinese(request.getParameter("addr")));
		model.setPhone((request.getParameter("phone")));
		Transaction tran = session.beginTransaction();
		if (id != 0) {
			session.update(model);
		} else {
			session.save(model);
		}
		tran.commit();
		return "1";
	}

	private String createcomment() {
		comments model = new comments();
		model.setBody(getChinese(request.getParameter("body")));
		model.setCreatetime(currentTime);
		model.setDishesid(Integer.valueOf(request.getParameter("dishesid")));
		model.setUsername(request.getParameter("username"));
		model.setUserid(Integer.valueOf(request.getParameter("userid")));
		Transaction tran = session.beginTransaction();
		session.save(model);
		session.flush();
		tran.commit();
		return "1";
	}

	private String editaddr() throws UnsupportedEncodingException {
		int id = Integer.valueOf((request.getParameter("id")));
		tb_addrs model;
		if (id == 0) {
			model = new tb_addrs();

		} else {
			model = (tb_addrs) (session.createQuery(
					" from tb_addrs where id=" + id).list().get(0));
		}
		model.setIsdefault(Integer.valueOf(request.getParameter("isdefault")));
		model.setUserid(Integer.valueOf(request.getParameter("userid")));
		model.setAddr(request.getParameter("addr"));
		model.setPerson(request.getParameter("person"));
		model.setPhone(request.getParameter("phone"));

		Transaction tran = session.beginTransaction();

		if (id != 0) {
			session.update(model);
		} else {
			session.save(model);
		}
		tran.commit();
		return "1";
	}

	// 提交订单
	private String createorder() throws UnsupportedEncodingException {
		orders model = null;
		List list = session.createQuery(
				" from orders where id=" + request.getParameter("id")).list();
		if (list.size() == 0) {
			model = new orders();
			model.setCreatetime(currentTime);
			model.setStatus("待接单");
		} else {
			model = (orders) list.get(0);
		}
		dishes dishesModel = (dishes) session
				.createQuery(
						" from dishes where id="
								+ request.getParameter("dishesid")).list()
				.get(0);
		model.setUserid(Integer.valueOf(request.getParameter("userid")));
		model.setUsername(getChinese(request.getParameter("username")));
		model.setAmount(Double.valueOf(request.getParameter("amount")));
		model.setPrice(dishesModel.getPrice());
		model.setDishesid(dishesModel.getId());
		model.setAddr(getChinese(request.getParameter("addr")));
		model.setPhone((request.getParameter("phone")));
		Transaction tran = session.beginTransaction();
		session.save(model);
		tran.commit();
		return "1";
	}

	/**
	 * 修改密码
	 * 
	 * @return
	 */
	public String updatePwd() throws UnsupportedEncodingException {
		List list = session.createQuery(
				" from users where loginid='" + request.getParameter("loginid")
						+ "' and passwords='"
						+ request.getParameter("passwords") + "'").list();
		if (list.size() == 0) {
			return "-1";// 账号或密码错误
		} else {
			users model = (users) list.get(0);
			model.setPasswords(request.getParameter("passwords_new"));
			Transaction tran = session.beginTransaction();
			session.save(model);
			tran.commit();
			return "1";// 修改成功
		}

	}

	/**
	 * 取得中文
	 * 
	 * @param 原字符
	 * @return
	 */
	private String getChinese(String str) {
		return str;
	}

	public String getOneRow() throws UnsupportedEncodingException {
		String write = "";
		if (request.getParameter("ID") == null) {
			sqlString = "select * from " + request.getParameter("Table");
		} else {
			sqlString = "select * from " + request.getParameter("Table")
					+ " where id=" + request.getParameter("ID");
		}

		write = HibernateSessionFactory.selectToJson(sqlString);
		return write;
	}

	public String Del() {
		int ID = Integer.valueOf(request.getParameter("ID"));
		String Table = request.getParameter("Table");
		String PK_Name = "id";
		String sql = "delete from " + Table + " where " + PK_Name + "=" + ID;
		HibernateSessionFactory.updateExecute(sql);
		return "1";

	}

	private void initRequest(HttpServletRequest request,
			HttpServletResponse response) {
		currentTime = formatter.format(new java.util.Date());
		currentDate = formatdate.format(new java.util.Date());

		this.request = request;

		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
	}

	@Override
	public void init() throws ServletException {

	}

}
