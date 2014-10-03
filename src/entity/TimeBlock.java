package entity;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class TimeBlock implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Temporal(TemporalType.DATE)
    private Date date;
    @Temporal(TemporalType.TIME)
    private Calendar startTime;
    @Temporal(TemporalType.TIME)
    private Calendar endTime;
    
    @ManyToOne
    Course course;
    
    @ManyToOne
    private ClassRoom classRoom;

    public TimeBlock(Date date, Calendar startTime,Calendar endtime) {
        this.date = date;
        this.startTime = startTime;
        this.endTime= endtime;
    }

    public TimeBlock() {
    }

    public Date getDate() {
        return date;
    }

    public Calendar getStartTime() {
        return startTime;
    }

    public Calendar getEndTime() {
        return endTime;
    }

    public Course getCourse() {
        return course;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "entity.TimeBlock[ id=" + id + " ]";
    }
    
}
