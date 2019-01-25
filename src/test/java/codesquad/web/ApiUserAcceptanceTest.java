package codesquad.web;

import codesquad.domain.Issue;
import codesquad.domain.IssueTest;
import codesquad.domain.User;
import codesquad.domain.UserTest;
import codesquad.dto.UserDto;
import codesquad.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import support.test.AcceptanceTest;

import javax.annotation.Resource;
import java.util.List;

public class ApiUserAcceptanceTest extends AcceptanceTest {

    private Issue issue;

    @Resource(name = "userService")
    private UserService userService;

    @Before
    public void setUp() throws Exception {
        this.issue = IssueTest.FIRST_ISSUE;
    }

    @Test
    public void create() throws Exception {
        UserDto newUser = createUserDto("testuser1");
        String location = createResource("/api/users", newUser);

        UserDto dbUser = getResource(location, UserDto.class, findByUserId(newUser.getUserId()));
        softly.assertThat(dbUser).isEqualTo(newUser);

    }

    @Test
    public void create_invalid() throws Exception {
        UserDto newUser = new UserDto("1", "1", "1");
        ResponseEntity<String> response = template().postForEntity("/api/users", newUser, String.class);
        softly.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void show_다른_사람() throws Exception {
        UserDto newUser = createUserDto("testuser2");
        String location = createResource("/api/users", newUser);

        ResponseEntity<String> response = getResource(location, findDefaultUser());
        softly.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    private UserDto createUserDto(String userId) {
        return new UserDto(userId, "password", "name");
    }

    @Test
    public void update() throws Exception {
        UserDto newUser = createUserDto("testuser3");
        String location = createResource("/api/users", newUser);

        User loginUser = findByUserId(newUser.getUserId());
        UserDto updateUser = new UserDto(newUser.getUserId(), "password", "name2");
        basicAuthTemplate(loginUser).put(location, updateUser);

        UserDto dbUser = getResource(location, UserDto.class, findByUserId(newUser.getUserId()));
        softly.assertThat(dbUser).isEqualTo(updateUser);
    }

    @Test
    public void update_다른_사람() throws Exception {
        UserDto newUser = createUserDto("testuser4");
        String location = createResource("/api/users", newUser);

        UserDto updateUser = new UserDto(newUser.getUserId(), "password", "name2");
        basicAuthTemplate(findDefaultUser()).put(location, updateUser);

        UserDto dbUser = getResource(location, UserDto.class, findByUserId(newUser.getUserId()));
        softly.assertThat(dbUser).isEqualTo(newUser);
    }

    @Test
    public void assignee_show_list() {
        ResponseEntity<List> response = template()
                .getForEntity("/api/issues/" + issue.getId() + "/assignees", List.class);
        softly.assertThat(response.getBody()).isEqualTo(userService.findAll());
    }
}
