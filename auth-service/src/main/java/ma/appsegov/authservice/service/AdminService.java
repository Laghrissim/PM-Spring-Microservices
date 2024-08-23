package ma.appsegov.authservice.service;

import ma.appsegov.authservice.model.User;

import java.util.List;

public interface AdminService {
    List<User> getProjectManagers();
}
