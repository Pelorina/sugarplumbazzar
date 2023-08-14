package Ecommerce.sugar.plum.bazzar.Users;

import Ecommerce.sugar.plum.bazzar.Utils.Response;

import java.time.LocalDateTime;
import java.util.List;

public interface Service {
    Response create (Request request);
    public String VerifyToken (String verificationToken1);
    Response findBYId(Long id);
    List<Entity> getAll(int pageNo,int pageSize);
    Response resetPassword(ResetDto resetDto);
    Entity getCurrentUser();


}
