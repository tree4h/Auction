package models;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Version;

import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.validation.NotNull;

import play.db.ebean.Model;

@Entity
public class Item extends Model {
    @Id
    public Long id;
    @CreatedTimestamp
    public Date createDate;
    @Version
    public Date updateDate;
    @ManyToOne
    @JoinColumn(name = "member_id")
    public Member member;
    
    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    private List<Auction> auctions = new LinkedList<Auction>();
 
    @NotNull
    private String name;
	
	public Item(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public String getOwnerName() {
		return member.getName();
	}

	public Long getOwnerID() {
		return member.id;
	}

}
