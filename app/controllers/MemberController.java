package controllers;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import java.util.Map;

import models.Auction;
import models.Item;
import models.Member;
import models.Bid;

import play.data.DynamicForm;
import play.data.Form;
import play.db.ebean.Model.Finder;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.top;
import views.html.manage;
import views.html.signin;
import views.html.member;
import views.html.item;

public class MemberController extends Controller {
	public static Result signin() {
		return ok(signin.render(""));
	}

	public static Result showMember() {
		String[] params = {"name"};
		DynamicForm input = Form.form();
		input = input.bindFromRequest(params);
		String member_id = input.data().get("member");

		Finder<Long, Member> f1 = new Finder<Long, Member>(Long.class, Member.class);
		Member mem = f1.byId(Long.decode(member_id));
		if (mem == null) {
			return ok(signin.render(Messages.ERROR_MEMBER_ID_NOEXIT.getMessage()));
		}
		
		List<Item> items = mem.getItems();
		return ok(member.render(mem,items));
	}
	
	public static Result showItem() {
		String[] params = {"name"};
		DynamicForm input = Form.form();
		input = input.bindFromRequest(params);
		String member_id = input.data().get("member");
		String item_id = input.data().get("item");
		
		Finder<Long, Member> f1 = new Finder<Long, Member>(Long.class, Member.class);
		Member mem = f1.byId(Long.decode(member_id));
		if (mem == null) {
			return ok(signin.render(Messages.ERROR_MEMBER_ID_NOEXIT.getMessage()));
		}
		
		Item target_item;
		if(item_id == "") {
			target_item = new Item("");
			mem.addItem(target_item);
			mem.save();
		}
		else {
			Finder<Long, Item> f2 = new Finder<Long, Item>(Long.class, Item.class);
			target_item = f2.byId(Long.decode(item_id));
		}
		return ok(item.render(mem,target_item));
	}

	public static Result bidAuction() {
		String[] params = {"name"};
		DynamicForm input = Form.form();
		input = input.bindFromRequest(params);
		
		String member_id = input.data().get("member");
		String auction_id = input.data().get("auction");
		String price = input.data().get("price");
		
		Finder<Long, Member> f1 = new Finder<Long, Member>(Long.class, Member.class);
		Member mem = f1.byId(Long.decode(member_id));
		
		Finder<Long, Auction> f2 = new Finder<Long, Auction>(Long.class, Auction.class);
		Auction auction = f2.byId(Long.decode(auction_id));
		
		if (mem == null) {
			return ok(manage.render(Messages.ERROR_MEMBER_ID_NOEXIT.getMessage()));
		}
		if (auction == null) {
			return ok(manage.render(Messages.ERROR_AUCTION_ID_NOEXIT.getMessage()));
		}
			
		Bid bid = new Bid(mem,Integer.parseInt(price));
		auction.bid(bid);
		auction.save();
		//bid.save();
		
		return ok(manage.render(""));
	}

	public static Result showManageMenu() {
		return ok(manage.render(""));
	}
	
	private static void printInput(Map<String,String> input) {
		for (String key : input.keySet()) {
			System.out.println("key=" + key + ", value=" + input.get(key));
		}
	}

	public static Result exhibit2() {
		String[] params = {"name"};
		DynamicForm input = Form.form();
		input = input.bindFromRequest(params);
		
		printInput(input.data());

		String member_id = input.data().get("member_id");
		String item_id = input.data().get("item_id");
		String item_name = input.data().get("item_name");
		String start = input.data().get("start");
		String end = input.data().get("end");
		String start_price = input.data().get("start_price");
		String min_price = input.data().get("min_price");
		String unit_price = input.data().get("unit_price");
		
		Finder<Long, Member> f1 = new Finder<Long, Member>(Long.class, Member.class);
		Member mem = f1.byId(Long.decode(member_id));
		Finder<Long, Item> f2 = new Finder<Long, Item>(Long.class, Item.class);
		Item item = f2.byId(Long.decode(item_id));
		
		Date d1 = DateFactory.makeDate(start);
		int range_second = Integer.parseInt(end);
		Calendar cal = Calendar.getInstance();
		cal.setTime(d1);
		cal.add(Calendar.SECOND,range_second);
		Date d2 = cal.getTime();
		
		item.setName(item_name);
		Auction auction = new Auction(d1,d2,mem,item);
		auction.save();
		item.save();
		
		List<Item> items = mem.getItems();
		return ok(member.render(mem,items));
	}

	public static Result exhibit() {
		String[] params = {"name"};
		DynamicForm input = Form.form();
		input = input.bindFromRequest(params);
		
		String member_id = input.data().get("member");
		String item_id = input.data().get("item");
		String start = input.data().get("start");
		String end = input.data().get("end");
		
		Finder<Long, Member> f1 = new Finder<Long, Member>(Long.class, Member.class);
		Member mem = f1.byId(Long.decode(member_id));
		
		Finder<Long, Item> f2 = new Finder<Long, Item>(Long.class, Item.class);
		Item item = f2.byId(Long.decode(item_id));
		
		if (mem == null) {
			return ok(manage.render(Messages.ERROR_MEMBER_ID_NOEXIT.getMessage()));
		}
		if (item == null) {
			return ok(manage.render(Messages.ERROR_ITEM_ID_NOEXIT.getMessage()));
		}
		
		Date d1 = DateFactory.makeDate(start);
		int range_second = Integer.parseInt(end);
		Calendar cal = Calendar.getInstance();
		cal.setTime(d1);
		cal.add(Calendar.SECOND,range_second);
		Date d2 = cal.getTime();
		
		Auction auction = new Auction(d1,d2,mem,item);
		auction.save();
		return ok(manage.render(""));
	}

	public static Result addMember() {
		String[] params = {"name"};
		DynamicForm input = Form.form();
		input = input.bindFromRequest(params);
		String name = input.data().get("name");
		Member m1 = new Member(name);
		m1.save();
		return ok(manage.render(""));
	}
	
	public static Result addItem() {
		String[] params = {"name"};
		DynamicForm input = Form.form();
		input = input.bindFromRequest(params);
		String name = input.data().get("name");
		String id = input.data().get("member");

		Finder<Long, Member> finder = new Finder<Long, Member>(Long.class, Member.class);
		Member mem = finder.byId(Long.decode(id));
		if (mem == null) {
			return ok(manage.render(Messages.ERROR_MEMBER_ID_NOEXIT.getMessage()));
		}
		
		Item item = new Item(name);
		mem.addItem(item);
		mem.save();
		return ok(manage.render(""));
	}
}
