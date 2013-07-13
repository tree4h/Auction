package models;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Version;

import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.validation.NotNull;

import play.db.ebean.Model;

@Entity
public class Member extends Model {
	@Id
    public Long id;
    @CreatedTimestamp
    public Date createDate;
    @Version
    public Date updateDate;
       
    @NotNull
    private String name;
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Item> items = new LinkedList<Item>();
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Auction> auctions = new LinkedList<Auction>();
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Bid> bids = new LinkedList<Bid>();
    
	public Member(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public int getNumberOfItems() {
		return items.size();
	}

	public void addItem(Item item) {
		this.items.add(item);
	}

	public List<Item> getItems() {
		return this.items;
	}
	public int getNumberOfBids() {
		return bids.size();
	}
}
