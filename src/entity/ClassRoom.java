package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class ClassRoom implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String roomNo;
    private int noOfSeats;
    
    @OneToMany(mappedBy = "classRoom")
    private List<TimeBlock> timeBlocks = new ArrayList();

    public ClassRoom(String roomNo, int noOfSeats) {
        this.roomNo = roomNo;
        this.noOfSeats = noOfSeats;
    }

    public ClassRoom() {
    }
    
    public String getRoomNo() {
        return roomNo;
    }

    public int getNoOfSeats() {
        return noOfSeats;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "entity.ClassRoom[ id=" + id + " ]";
    }
    
}
