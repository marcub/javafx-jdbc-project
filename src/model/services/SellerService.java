package model.services;


import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Seller;

import java.util.List;

public class SellerService {

    private final SellerDao sellerDao = DaoFactory.createSellerDao();

    public List<Seller> findAll() {
        return sellerDao.findAll();
    }

    public void saveOrUpdate(Seller seller) {
        if (seller.getId() == null) {
            sellerDao.insert(seller);
        }
        else {
            sellerDao.update(seller);
        }
    }

    public void remove(Seller seller) {
        sellerDao.deleteById(seller.getId());
    }
}
