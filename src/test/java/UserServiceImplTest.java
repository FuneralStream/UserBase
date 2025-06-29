import org.example.dao.UserDao;
import org.example.entity.User;
import org.example.service.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void saveTest() {
        User user = new User();
        user.setName("Daniil Medvedev");
        user.setEmail("medvedev@mail.ru");
        user.setAge(29);

        userService.save(user);

        verify(userDao, times(1)).save(user);
        verifyNoMoreInteractions(userDao);
    }

    @Test
    void findByIdTest() {
        Long userId = 1L;
        User expectedUser = new User();
        expectedUser.setId(1);
        expectedUser.setName("Andrey Rublev");
        expectedUser.setEmail("rublev@mail.ru");
        expectedUser.setAge(27);

        when(userDao.findById(userId)).thenReturn(Optional.of(expectedUser));

        Optional<User> result = userService.findById(userId);

        assertTrue(result.isPresent());
        assertEquals(expectedUser, result.get());
        verify(userDao, times(1)).findById(userId);
        verifyNoMoreInteractions(userDao);
    }

    @Test
    void findByIdTestEmpty() {
        Long userId = 999L;
        when(userDao.findById(userId)).thenReturn(Optional.empty());

        assertTrue(userService.findById(userId).isEmpty());
        verify(userDao, times(1)).findById(userId);
        verifyNoMoreInteractions(userDao);
    }

    @Test
    void findAllTest() {
        User user1 = new User();
        user1.setId(1);
        user1.setName("Alexander Bublik");
        user1.setEmail("bublik@mail.ru");
        user1.setAge(28);

        User user2 = new User();
        user2.setId(2);
        user2.setName("Novak Djokovic");
        user2.setEmail("djokovic@mail.ru");
        user2.setAge(36);

        List<User> expectedUsers = List.of(user1, user2);

        when(userDao.findAll()).thenReturn(expectedUsers);

        List<User> result = userService.findAll();

        assertEquals(2, result.size());
        assertEquals(expectedUsers, result);
        verify(userDao, times(1)).findAll();
        verifyNoMoreInteractions(userDao);
    }

    @Test
    void updateTest() {
        User userToUpdate = new User();
        userToUpdate.setId(1);
        userToUpdate.setName("Hamad Medjedovic");
        userToUpdate.setEmail("medjedovic@mail.ru");
        userToUpdate.setAge(21);

        userService.update(userToUpdate);

        verify(userDao, times(1)).update(userToUpdate);
        verifyNoMoreInteractions(userDao);
    }

    @Test
    void deleteTest() {
        User userToDelete = new User();
        userToDelete.setId(1);
        userToDelete.setName("Jannik Sinner");
        userToDelete.setEmail("sinner@mail.ru");
        userToDelete.setAge(23);

        userService.delete(userToDelete);

        verify(userDao, times(1)).delete(userToDelete);
        verifyNoMoreInteractions(userDao);
    }
}