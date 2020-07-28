package com.blsa.ezilog.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blsa.ezilog.dao.SelectionHistoryDao;
import com.blsa.ezilog.dao.SelectionPostDao;
import com.blsa.ezilog.model.post.SelectionHistory;
import com.blsa.ezilog.model.post.SelectionPost;

@Service
public class SelectionService{

    @Autowired
    SelectionPostDao selectionPostDao;
    
    @Autowired
    SelectionHistoryDao selectionHistoryDao;
    
    @Transactional
    public SelectionHistory removeMainPostandAddtoHistory(SelectionPost post) {
        Optional<SelectionPost> selectionPostOptional = selectionPostDao.findById(post.getId());
        
        if(selectionPostOptional.isPresent()) {
            SelectionHistory selectionHistory = new SelectionHistory(post.getId(), post.getNo(), post.getDescription());
            selectionHistoryDao.save(selectionHistory);
            selectionPostDao.deleteById(post.getId());
            
            return selectionHistory;
        }else {
            return null;
        }
        
    }

}
