package controllers;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

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

public class MemberController extends Controller {
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
			return badRequest("指定された会員が存在しません。");
		}
		if (auction == null) {
			return badRequest("指定されたオークションが存在しません。");
		}
			
		Bid bid = new Bid(mem,Integer.parseInt(price));
		auction.bid(bid);
		auction.save();
		//bid.save();
		
		return ok(manage.render());
	}

	public static Result showManageMenu() {
		return ok(manage.render());
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
			return badRequest("指定された会員が存在しません。");
		}
		if (item == null) {
			return badRequest("指定された商品が存在しません。");
		}
		
		Date d1 = DateFactory.makeDate(start);
		int range_second = Integer.parseInt(end);
		Calendar cal = Calendar.getInstance();
		cal.setTime(d1);
		cal.add(Calendar.SECOND,range_second);
		Date d2 = cal.getTime();
		
		Auction auction = new Auction(d1,d2,mem,item);
		auction.save();
		return ok(manage.render());
	}

	public static Result addMember() {
		String[] params = {"name"};
		DynamicForm input = Form.form();
		input = input.bindFromRequest(params);
		String name = input.data().get("name");
		Member m1 = new Member(name);
		m1.save();
		return ok(manage.render());
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
			return badRequest("指定された会員が存在しません。");
		}
		
		Item item = new Item(name);
		mem.addItem(item);
		mem.save();
		return ok(manage.render());
	}
}
