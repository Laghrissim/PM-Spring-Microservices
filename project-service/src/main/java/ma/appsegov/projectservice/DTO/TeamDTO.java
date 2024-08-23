package ma.appsegov.projectservice.DTO;

import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ma.appsegov.projectservice.model.*;

import java.util.ArrayList;
import java.util.List;

@Data
@RequiredArgsConstructor
public class TeamDTO {

    private Long id;

    private String name;

    /*private List<MemberDTO> members;*/

    private ProjectDTO project;

}
