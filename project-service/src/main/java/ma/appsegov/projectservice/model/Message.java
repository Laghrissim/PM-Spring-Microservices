package ma.appsegov.projectservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "message")
@Data
public class Message {

    @JsonIgnore
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "channel")
    private String channel;

    @Column(name = "sender")
    private String sender;

    @Column(name = "content", length = 1000)
    private String content;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "timestamp")
    private Date timestamp;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "read_date")
    private Date readDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "project_id")
    private Project project;


}
