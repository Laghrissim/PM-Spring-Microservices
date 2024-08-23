package ma.appsegov.authservice.service.impl;

import lombok.AllArgsConstructor;
import ma.appsegov.authservice.model.User;
import ma.appsegov.authservice.repository.UserRepository;
import ma.appsegov.authservice.service.AdminService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AdminServiceImpl implements AdminService {
    private UserRepository userRepository;

    @Override
    public List<User> getProjectManagers() {
        return userRepository.findByRoles_Nom("PROJECT_MANAGER");

    }
}
