package controllers;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import models.Bid;
import models.Member;
import models.Item;
import models.Auction;
import play.data.DynamicForm;
import play.data.Form;
import play.db.ebean.Model.Finder;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.manage;
import views.html.memberlist;
import views.html.auctionlist;
import views.html.itemlist;
import views.html.auction;
import views.html.bidlist;

public class MarketController extends Controller {
	
	public static Result showAuction() {
		String[] params = {"name"};
		DynamicForm input = Form.form();
		input = input.bindFromRequest(params);
		String auction_id = input.data().get("auction");
		
		Finder<Long, Auction> f1 = new Finder<Long, Auction>(Long.class, Auction.class);
		Auction a1 = f1.byId(Long.decode(auction_id));
		
		if (a1 == null) {
			return badRequest(Messages.ERROR_AUCTION_ID_NOEXIT.getMessage());
		}
		List<Bid> bids = a1.getBids();
		List<Bid> sortBids = sortBids(bids);
		return ok(auction.render(a1,sortBids));
	}

	public static Result showMemberList() {
		Finder<Long, Member> finder = new Finder<Long, Member>(Long.class, Member.class);
		List<Member> members = finder.all();
		return ok(memberlist.render(members));
	}

	public static Result showBidList() {
		Finder<Long, Bid> finder = new Finder<Long, Bid>(Long.class, Bid.class);
		List<Bid> bids = finder.orderBy("bidDate desc").findList();
		return ok(bidlist.render(bids));
	}

	public static Result showItemList() {
		Finder<Long, Item> finder = new Finder<Long, Item>(Long.class, Item.class);
		List<Item> items = finder.all();
		return ok(itemlist.render(items));
	}

	public static Result showAuctionList() {
		Finder<Long, Auction> finder = new Finder<Long, Auction>(Long.class, Auction.class);
		List<Auction> auctions = finder.all();
		return ok(auctionlist.render(auctions));
	}
	
	public static Result deleteData() {
		String[] params = {"name"};
		DynamicForm input = Form.form();
		input = input.bindFromRequest(params);
		String id = input.data().get("member");
		
		if(id=="") {
			deleteAllData();
			return ok(manage.render(""));
		}

		Finder<Long, Member> finder = new Finder<Long, Member>(Long.class, Member.class);
		Member mem = finder.byId(Long.decode(id));
		if (mem == null) {
			return ok(manage.render(Messages.ERROR_MEMBER_ID_NOEXIT.getMessage()));
		}
		mem.delete();
		mem.save();
		return ok(manage.render(""));
	}
	
	public static Result setTestData() {
		String[] params = {"name"};
		DynamicForm input = Form.form();
		input = input.bindFromRequest(params);
		String pattern = input.data().get("pattern");
		if(pattern.equals("1")) {
			setTestData01();
		}
		return ok(manage.render(""));
	}
	
	private static List<Bid> sortBids(List<Bid> bids) {
		List<Bid> sortBids = new LinkedList();
		for(int i=bids.size()-1; i>=0; i--) {
			sortBids.add(bids.get(i));
		}
		return sortBids;
	}

	private static void deleteAllData() {
		Finder<Long, Member> finder = new Finder<Long, Member>(Long.class, Member.class);
		List<Member> members = finder.all();
		for(Member member : members) {
			member.delete();
			member.save();
		}
	}

	private static void setTestData01() {
		//会員登録
		Member m1 = new Member("moriki");
		Member m2 = new Member("morishita");
		Member m3 = new Member("hosozawa");
		//商品登録
		Item m1i1 = new Item("orange");
		Item m2i1 = new Item("apple");
		Item m3i1 = new Item("banana");
		m1.addItem(m1i1);
		m2.addItem(m2i1);
		m3.addItem(m3i1);
		
		m1.save();
		m2.save();
		m3.save();
		//出品
		Date start = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(start);
		int range = 30;
		cal.add(Calendar.SECOND,range);//30秒
		Date m3i1end = cal.getTime();
		cal.add(Calendar.SECOND,range*3);//120秒
		Date m1i1end = cal.getTime();
		cal.add(Calendar.SECOND,range*6);//300秒
		Date m2i1end = cal.getTime();//300秒
		Auction a1 = new Auction(start,m1i1end,m1,m1i1);
		Auction a2 = new Auction(start,m2i1end,m2,m2i1);
		Auction a3 = new Auction(start,m3i1end,m3,m3i1);

		a1.save();
		a2.save();
		a3.save();
		//入札
		Bid m2b1 = new Bid(m2,100);
		a1.bid(m2b1);
		Bid m3b1 = new Bid(m3,200);
		a1.bid(m3b1);
		Bid m2b2 = new Bid(m2,300);
		a1.bid(m2b2);
		Bid m3b2 = new Bid(m3,400);
		a1.bid(m3b2);
		Bid m2b3 = new Bid(m2,400);
		a1.bid(m2b3);
		
		a1.save();
	}
}
