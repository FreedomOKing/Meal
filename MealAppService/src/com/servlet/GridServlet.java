package com.servlet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;

import com.cm.bean.PagesHelper;

@SuppressWarnings({ "unchecked", "serial", "rawtypes" })
public class GridServlet extends HttpServlet {
	private Session session = null;

	public GridServlet() {
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
		session = HibernateSessionFactory.getSession();
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
		String action = request.getParameter("Action");
		System.out.println("ִ��GridServlet:" + action);
		String sqlString = "";
		ResultSet rs = null;
		List list = new ArrayList();
		int pageSize = 10;
		int currentpage = 0;
		currentpage = Integer.valueOf(request.getParameter("currentpage"));
		currentpage = Math.max(currentpage, 1);

		//
		if (action.equals("getlist")) {
			String msg = "";
			if (request.getParameter("msg") != null) {
				msg = request.getParameter("msg");
			}
			pageSize = 6;
			PagesHelper model = new PagesHelper();
			model.setTableName(" dishes INNER JOIN users ON dishes.businessid = users.id");
			model.setColumnName("dishes.*, users.name businessname");
			model.setOrder("dishes.id");
			model.setFilter(" and title like '%" + msg + "%'");
			// �ܹ�������
			int totalCount = Integer.valueOf(String
					.valueOf(HibernateSessionFactory.executeScalar(model
							.ToCountString())));
			// ����ҳ
			int pagecount = totalCount % pageSize == 0 ? (totalCount / pageSize)
					: (totalCount / pageSize + 1);
			currentpage = Math.min(currentpage, pagecount);
			int start = (currentpage - 1) * pageSize + 1;
			int limit = pageSize;
			model.setCurrentIndex(start);
			model.setPageSize(limit);

			rs = HibernateSessionFactory.queryBySql(model.ToListString());
			System.out.println(model.ToListString());
			request.setAttribute("datalist",
					HibernateSessionFactory.convertList(rs));
			request.setAttribute("currentpage", currentpage);
			request.setAttribute("pagecount", pagecount);
			request.setAttribute("total", totalCount);
			request.getRequestDispatcher("../index.jsp").forward(request,
					response);
		}

		if (action.equals("getuserlist")) {
			String msg = "";
			if (request.getParameter("msg") != null) {
				msg = request.getParameter("msg");
			}
			pageSize = 6;
			PagesHelper model = new PagesHelper();
			model.setTableName("users ");
			model.setColumnName("*");
			model.setOrder("id");
			model.setFilter(" and typename='�û�' and name like '%" + msg + "%'");
			// �ܹ�������
			int totalCount = Integer.valueOf(String
					.valueOf(HibernateSessionFactory.executeScalar(model
							.ToCountString())));
			// ����ҳ
			int pagecount = totalCount % pageSize == 0 ? (totalCount / pageSize)
					: (totalCount / pageSize + 1);
			currentpage = Math.min(currentpage, pagecount);
			int start = (currentpage - 1) * pageSize + 1;
			int limit = pageSize;
			model.setCurrentIndex(start);
			model.setPageSize(limit);

			rs = HibernateSessionFactory.queryBySql(model.ToListString());
			System.out.println(model.ToListString());
			request.setAttribute("datalist",
					HibernateSessionFactory.convertList(rs));
			request.setAttribute("currentpage", currentpage);
			request.setAttribute("pagecount", pagecount);
			request.setAttribute("total", totalCount);
			request.getRequestDispatcher("../userlist.jsp").forward(request,
					response);
		}

		if (action.equals("getbusinesslist")) {
			String msg = "";
			if (request.getParameter("msg") != null) {
				msg = request.getParameter("msg");
			}
			pageSize = 6;
			PagesHelper model = new PagesHelper();
			model.setTableName("users ");
			model.setColumnName("*");
			model.setOrder("id");
			model.setFilter(" and typename='�̼�' and name like '%" + msg + "%'");
			// �ܹ�������
			int totalCount = Integer.valueOf(String
					.valueOf(HibernateSessionFactory.executeScalar(model
							.ToCountString())));
			// ����ҳ
			int pagecount = totalCount % pageSize == 0 ? (totalCount / pageSize)
					: (totalCount / pageSize + 1);
			currentpage = Math.min(currentpage, pagecount);
			int start = (currentpage - 1) * pageSize + 1;
			int limit = pageSize;
			model.setCurrentIndex(start);
			model.setPageSize(limit);

			rs = HibernateSessionFactory.queryBySql(model.ToListString());
			System.out.println(model.ToListString());
			request.setAttribute("datalist",
					HibernateSessionFactory.convertList(rs));
			request.setAttribute("currentpage", currentpage);
			request.setAttribute("pagecount", pagecount);
			request.setAttribute("total", totalCount);
			request.getRequestDispatcher("../businesslist.jsp").forward(
					request, response);
		}

		if (action.equals("getorderlist")) {
			String msg = "";
			if (request.getParameter("msg") != null) {
				msg = request.getParameter("msg");
				System.out.println("msg  " + msg);
			}
			pageSize = 6;
			PagesHelper model = new PagesHelper();
			model.setTableName("orders INNER JOIN dishes ON orders.dishesid = dishes.id INNER JOIN users ON dishes.businessid = users.id  ");
			model.setColumnName("orders.*,orders.price*orders.amount total, dishes.title,dishes.img_url, users.`name` businessname");
			model.setOrder("orders.id");
			model.setFilter(" and orders.username like '%" + msg + "%'");
			// �ܹ�������
			int totalCount = Integer.valueOf(String
					.valueOf(HibernateSessionFactory.executeScalar(model
							.ToCountString())));
			// ����ҳ
			int pagecount = totalCount % pageSize == 0 ? (totalCount / pageSize)
					: (totalCount / pageSize + 1);
			currentpage = Math.min(currentpage, pagecount);
			int start = (currentpage - 1) * pageSize + 1;
			int limit = pageSize;
			model.setCurrentIndex(start);
			model.setPageSize(limit);

			rs = HibernateSessionFactory.queryBySql(model.ToListString());
			System.out.println(model.ToListString());
			request.setAttribute("datalist",
					HibernateSessionFactory.convertList(rs));
			request.setAttribute("currentpage", currentpage);
			request.setAttribute("pagecount", pagecount);
			request.setAttribute("total", totalCount);
			request.getRequestDispatcher("../orderlist.jsp").forward(request,
					response);
		}

	}

	/**
	 * ȡ������
	 * 
	 * @param ԭ�ַ�
	 * @return
	 */
	private String getChinese(String str) {
		if (str == null) {
			return "";
		}
		try {
			return new String(str.getBytes("ISO8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "";

		}
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("ִ��GridServlet");
	}

	@Override
	public void init() throws ServletException {

	}

}
