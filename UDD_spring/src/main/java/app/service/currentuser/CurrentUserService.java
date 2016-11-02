package app.service.currentuser;

import app.model.CurrentUser;


public interface CurrentUserService {

    boolean canAccessUser(CurrentUser currentUser, Long userId);

}
